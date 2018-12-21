package com.ncit.module_01.UC01_03.service;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.model.TmpTimeParamConfig;
import com.ncit.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Scope("singleton")
public class UC01_03_serviceImp implements UC01_03_service {
    @Resource
    RemoteProperties remoteProperties;

    @Resource
    TmpTimeParamConfig tmpTimeParamConfig;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public static final List<Process> processList = new ArrayList<Process>();
    public static final List<Runnable> runnableList = new ArrayList<Runnable>();

    public void startThread(String _beforeDate, String _afterDate){
        String rootPath = remoteProperties.getRootLogDirectory();
        String dirPath = rootPath + "/" + Constants.UC01_03_LOG;

        fileUtil.CreateAndDeleteFile(dirPath + "/log1");
        fileUtil.CreateAndDeleteFile(dirPath + "/log2");

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        DatabaseImportRunnable runnableImport = new DatabaseImportRunnable(_beforeDate, _afterDate, new File(dirPath+"/log1"),new File(dirPath+"/log2"));
        executorService.submit(runnableImport);
        runnableList.add(runnableImport);

        //添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
//        try {
            //修改application.yml
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_HAVE);
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/databaseImportInit");
            //修改tmp.yml
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC01_03_BEGINDATE,_beforeDate);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC01_03_AFTERDATE,_afterDate);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //读入缓存
        remoteProperties.setStatus("1");
        remoteProperties.setEnterPage("/simulaterWebApp/databaseImportInit");
        tmpTimeParamConfig.setUc01_03_beginDate(_beforeDate);
        tmpTimeParamConfig.setUc01_03_afterDate(_afterDate);
    }
    //线程类
    class DatabaseImportRunnable implements Runnable {
        private boolean stopThreadID = false;

        private String beforeDate;
        private String afterDate;
        private File file1;
        private File file2;

        public DatabaseImportRunnable(String _beforeDate, String _afterDate,File file1,File file2) {
            this.beforeDate = _beforeDate;
            this.afterDate = _afterDate;
            this.file1 = file1;
            this.file2 = file2;
        }

        public void stopThread() {
            stopThreadID = true;
        }

        @Override
        public void run() {
            String parameter = beforeDate;

            boolean stopflag = false;
            String shellCommand = remoteProperties.getHe_ShellCommand3();

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
                String line;
                try {
                    process = Runtime.getRuntime().exec(shellCommand + " " + parameter);
                    processList.add(process);
                    bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(),"Shift_JIS"));
                    bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(),"Shift_JIS"));
                    // 读取输出
                    while ((line = bufrIn.readLine()) != null || (line = bufrError.readLine()) != null) {
                        line = String.format("%s %s",line,"\r\n");
                        messagingTemplate.convertAndSend("/ws/uc01_03Log2", line);
                        ps1.append(line);
//                        Thread.currentThread().sleep(10);
                    }
                    if (stopThreadID) {
                        throw new Exception("stopThread 终止进程");
                    }
                    process.waitFor();

                    if (process.exitValue() == 0){
                        line = String.format("%s %s %s",parameter,"执行成功!","\r\n");
                    }else {
                        line = String.format("%s %s %s",parameter,"执行失败！","\r\n");
                    }
                    messagingTemplate.convertAndSend("/ws/uc01_03Log1", line);
                    ps2.append(line);

                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    KillProcessUtil.closeStream(bufrIn);
                    KillProcessUtil.closeStream(bufrError);
                    KillProcessUtil.destroyProcess(process);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(parameter);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR,1);
                parameter = sdf.format(calendar.getTime());

                if (parameter.compareTo(afterDate) > 0) {
                    stopflag = true;
                }

            }
            KillProcessUtil.closeStream(ps1);
            KillProcessUtil.closeStream(ps2);
            messagingTemplate.convertAndSend("/ws/uc01_03Log1", "End");
            messagingTemplate.convertAndSend("/ws/uc01_03Log2", "End");
        }
    }
    @Override
    public void stopThread() {
        for (Runnable runnable : runnableList) {
            if (runnable instanceof DatabaseImportRunnable) {
                ((DatabaseImportRunnable) runnable).stopThread();
            }
        }
        for (Process process : processList) {
            // 杀进程
            KillProcessUtil.killProcessTreeCmd(KillProcessUtil.getPid(process));
        }

        //添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
//        try {
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_NOT_HAVE);
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/index");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //读入缓存
        remoteProperties.setStatus("0");
        remoteProperties.setEnterPage("/simulaterWebApp/index");
    }

    @Override
    public Map init() {
        Map map = new HashMap();
        //初始化log日志
        String dirPath = remoteProperties.getRootLogDirectory() + "/" + Constants.UC01_03_LOG;
        //没有文件夹则创建
        fileUtil.existDirectory(dirPath);
        String log1 = readLogUtil.readLogFile(dirPath+"/log1");
        String log2 = readLogUtil.readLogFile(dirPath+"/log2");
        map.put("log1",log1);
        map.put("log2",log2);

        //初始化时间日期
        String beginDate = null,afterDate = null;
        //初始化过程中判断线程是否执行，0没有执行则赋予今天日期，1在执行则赋予该线程执行时的参数
        if (remoteProperties.getStatus().equals("0")){
            beginDate = GetTimeUtil.getNowDate();
            afterDate = beginDate;
        }else if (remoteProperties.getStatus().equals("1")){
            beginDate = tmpTimeParamConfig.getUc01_03_beginDate();
            afterDate = tmpTimeParamConfig.getUc01_03_afterDate();
        }
        map.put("beginDate",beginDate);
        map.put("afterDate",afterDate);
        //获取线程执行状态
        map.put("status",remoteProperties.getStatus());
        return map;
    }
}

