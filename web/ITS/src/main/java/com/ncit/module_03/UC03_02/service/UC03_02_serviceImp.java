package com.ncit.module_03.UC03_02.service;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.model.TmpTimeParamConfig;
import com.ncit.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UC03_02_serviceImp implements UC03_02_service{
    @Resource
    RemoteProperties remoteProperties;
    @Resource
    TmpTimeParamConfig tmpTimeParamConfig;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public static final List<Process> processList = new ArrayList<Process>();
    public static final List<Runnable> runnableList = new ArrayList<Runnable>();

    public void startThread(String date, String timeFrom, String timeTo){
        String dirPath = remoteProperties.getRootLogDirectory() + "/" + Constants.UC03_02_LOG;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        SystemStartUpRunnableAssess runnableAssess = new SystemStartUpRunnableAssess(date, timeFrom, timeTo,new File(dirPath+"/log1"),new File(dirPath+"/log2"));
        executorService.submit(runnableAssess);
        runnableList.add(runnableAssess);
        //添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
        remoteProperties.setStatus("1");
        remoteProperties.setEnterPage("/simulaterWebApp/assessResult");
        tmpTimeParamConfig.setUc03_02_date(date);
        tmpTimeParamConfig.setUc03_02_timeFrom(timeFrom);
        tmpTimeParamConfig.setUc03_02_timeTo(timeTo);
//        try {
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_HAVE);
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/assessResult");
            //修改tmp.yml
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC03_02_DATE,date);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC03_02_TIMEFROM,timeFrom);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC03_02_TIMETO,timeTo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public void stopThread1() {
        for (Runnable runnable : runnableList) {
            if (runnable instanceof SystemStartUpRunnableAssess) {
                ((SystemStartUpRunnableAssess) runnable).stopThread();
            }
        }
        for (Process process : processList) {
            // 杀进程
            KillProcessUtil.killProcessTreeCmd(KillProcessUtil.getPid(process));
        }
        //添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
        remoteProperties.setStatus("0");
        remoteProperties.setEnterPage("/simulaterWebApp/index");
//        try {
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_NOT_HAVE);
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/index");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    //线程类
    class SystemStartUpRunnableAssess implements Runnable {
        private boolean stopThreadID = false;

        private String date;
        private int timeFrom;
        private int timeTo;
        private File file1;
        private File file2;

        public SystemStartUpRunnableAssess(String date, String timeFrom, String timeTo,File file1,File file2) {
            this.date = date;
            this.timeFrom = Integer.parseInt(timeFrom);
            this.timeTo = Integer.parseInt(timeTo);
            this.file1 = file1;
            this.file2 = file2;
        }

        public void stopThread() {
            stopThreadID = true;
        }

        @Override
        public void run() {

            String parameter = String.format("%s %s",date,timeFrom);
            boolean stopflag = false;
            String shellCommand = remoteProperties.getRESULTASSESS_ShellCommand();
            PrintStream ps1 = null;
            PrintStream ps2 = null;
            try {
                ps1 = new PrintStream(new FileOutputStream(file1));
                ps2 = new PrintStream(new FileOutputStream(file2));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (stopflag != true && stopThreadID != true) {

                Process process = null;
                BufferedReader bufrIn = null;
                BufferedReader bufrError = null;
                String line = null;
                try {
                    process = Runtime.getRuntime().exec(shellCommand + " " + parameter);
                    processList.add(process);
                    bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(),"Shift_JIS"));
                    bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(),"Shift_JIS"));
                    // 读取输出
                    while ((line = bufrIn.readLine()) != null || (line = bufrError.readLine()) != null) {
                        line = String.format("%s %s",line,"\r\n");
                        messagingTemplate.convertAndSend("/ws/uc03_02Log1", line);
                        ps1.append(line);
                        Thread.currentThread().sleep(5);
                        if (stopThreadID) {
                            throw new Exception("stopThread 终止进程");
                        }
                    }
                    process.waitFor();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (process.exitValue() == 0){
                        line = String.format("%s %s %s",parameter,"执行成功！","\r\n");
                    }else if (process.exitValue() == 1){
                        line = String.format("%s %s %s",parameter,"执行失败！","\r\n");
                    }
                    messagingTemplate.convertAndSend("/ws/uc03_02Log2", line);
                    ps2.append(line);
                    KillProcessUtil.closeStream(bufrIn);
                    KillProcessUtil.closeStream(bufrError);
                    KillProcessUtil.destroyProcess(process);
                }

                //判断是否超过时间
                timeFrom++;
                if (timeFrom>timeTo){
                    stopflag = true;
                }
                parameter = date + " " + timeFrom;
            }
            KillProcessUtil.closeStream(ps1);
            KillProcessUtil.closeStream(ps2);
            messagingTemplate.convertAndSend("/ws/uc03_02Log2", "End");
            stopThread1();
        }
    }

    @Override
    public Map init() {
        Map map = new HashMap();
        //初始化log日志
        //没有文件夹则创建

        String dirPath = remoteProperties.getRootLogDirectory() + "/" + Constants.UC03_02_LOG;

        fileUtil.existDirectory(dirPath);
        String log1 = readLogUtil.readLogFile(dirPath+"/log1");
        String log2 = readLogUtil.readLogFile(dirPath+"/log2");
        map.put("log1",log1);
        map.put("log2",log2);

        //初始化过程中判断线程是否执行，0没有执行则赋予今天日期，1在执行则赋予该线程执行时的参数
        String beginDate = null;
        String timeFrom = null;
        String timeTo = null;
        if (remoteProperties.getStatus().equals("0")){
            beginDate = GetTimeUtil.getNowDate();
            timeFrom = "0";
            timeTo = "0";
        }else if (remoteProperties.getStatus().equals("1")){
            beginDate = tmpTimeParamConfig.getUc03_02_date();
            timeFrom = tmpTimeParamConfig.getUc03_02_timeFrom();
            timeTo = tmpTimeParamConfig.getUc03_02_timeTo();
        }
        map.put("beginDate",beginDate);
        map.put("timeFrom",timeFrom);
        map.put("timeTo",timeTo);
        //获取线程执行状态
        map.put("status",remoteProperties.getStatus());
        return map;
    }
}

