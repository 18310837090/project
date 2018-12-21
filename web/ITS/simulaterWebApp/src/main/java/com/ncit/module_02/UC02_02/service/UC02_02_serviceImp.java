package com.ncit.module_02.UC02_02.service;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.model.TmpTimeParamConfig;
import com.ncit.common.util.*;
import com.sun.jna.Platform;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.scanner.ScannerException;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ncit.common.util.StringUtil.getOldValueStr;

@Service
public class UC02_02_serviceImp implements UC02_02_service {
    @Resource
    RemoteProperties remoteProperties;
    @Resource
    TmpTimeParamConfig tmpTimeParamConfig;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    // 线程池
    public static final ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_NUMBER_MAS + Constants.THREAD_NUMBER);

    public static final List<Process> processList = new ArrayList<Process>();

    public static final List<Runnable> runnableList = new ArrayList<Runnable>();

    public void startThread(String _beginDate, String _afterDate, String _beginTime,String _afterTime){
        String dirPath = remoteProperties.getRootLogDirectory() + "/" + Constants.UC02_02_LOG;

        // 启动 msa 服务器 run-sim-server.bat
        for (int i = 1; i <= Constants.THREAD_NUMBER_MAS; i++) {
            SystemStartUpRunnableMas runnableMas = new SystemStartUpRunnableMas(i, new File(dirPath+"/log"+i));
            executorService.submit(runnableMas);
            runnableList.add(runnableMas);
        }
        SystemStartUpRunnableGA runnableGA = new SystemStartUpRunnableGA(_beginDate, _afterDate,_beginTime,_afterTime, new File(dirPath+"/"+GetTimeUtil.getNowDate()),new File(dirPath+"/log4"));
        executorService.submit(runnableGA);
        runnableList.add(runnableGA);

        //添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
        remoteProperties.setStatus("1");
        remoteProperties.setEnterPage("/simulaterWebApp/systemStartInit");
        //读入缓存
        tmpTimeParamConfig.setUc02_02_beginDate(_beginDate);
        tmpTimeParamConfig.setUc02_02_afterDate(_afterDate);
        tmpTimeParamConfig.setUc02_02_beginTime(_beginTime);
        tmpTimeParamConfig.setUc02_02_afterTime(_afterTime);
//        try {
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_HAVE);
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/systemStartInit");
            //修改tmp.yml
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC02_02_BEGINDATE,_beginDate);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC02_02_AFTERDATE,_afterDate);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC02_02_BEGINTIME,_beginTime);
//            PasswordencryptUtil.changePW(Constants.TMP_PARAM_FILE_PATH,Constants.UC02_02_AFTERTIME,_afterTime);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void stopThread1() {
        for (Runnable runnable : runnableList) {
            if (runnable instanceof SystemStartUpRunnableMas) {
                ((SystemStartUpRunnableMas) runnable).stopThread();
            } else if (runnable instanceof SystemStartUpRunnableGA) {
                ((SystemStartUpRunnableGA) runnable).stopThread();
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

    class SystemStartUpRunnableGA implements Runnable {
        //成员变量
        private boolean stopThreadID = false;

        private String beginDate;
        private String afterDate;
        private String beginTime;
        private String afterTime;
        private File file1;
        private File file2;

        //构造函数
        public SystemStartUpRunnableGA(String _beginDate, String _afterDate,String _beginTime,String _afterTime,File file1,File file2) {

            this.beginDate = _beginDate;
            this.afterDate = _afterDate;
            this.beginTime = _beginTime;
            this.afterTime = _afterTime;
            this.file1 = file1;
            this.file2 = file2;
        }

        public void stopThread() {
            stopThreadID = true;
        }

        @Override
        public void run() {
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
            String shellCommand_tmp = remoteProperties.getHe_ShellCommand2();

            String parameter_tmp ;
            PrintStream ps1 = null;
            PrintStream ps2 = null;
            try {
                ps1 = new PrintStream(new FileOutputStream(file1));
                ps2 = new PrintStream(new FileOutputStream(file2));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int begin = Integer.parseInt(beginTime);
            int after = Integer.parseInt(afterTime);

            while (beginDate.compareTo(afterDate) <= 0 && stopThreadID == false){
                for (int i = begin;i <= after;i++){
                    Process process = null;
                    BufferedReader bufrIn = null;
                    BufferedReader bufrError = null;
                    String line ;
                    try {
                        parameter_tmp = beginDate+" "+i;
                        String shellCommand = shellCommand_tmp+" "+parameter_tmp;

                        process = Runtime.getRuntime().exec(shellCommand);
                        processList.add(process);
                        bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "Shift_JIS"));
                        bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "Shift_JIS"));
                        // 读取输出
                        while ((line = bufrIn.readLine()) != null || (line = bufrError.readLine()) != null) {
                            messagingTemplate.convertAndSend("/ws/gaLog", line + "\r\n");
                            ps1.append(line+"\r\n");
                            Thread.currentThread().sleep(5);
                            if (stopThreadID) {
                                throw new Exception("stopThread 终止进程");
                            }
                        }
                        //判断脚本退出码，返回值。0代表脚本执行正常，1代表脚本执行失败
                        process.waitFor();
                        if (process.exitValue() == 0){
                            line = String.format("%s %s %s",parameter_tmp,"执行成功！","\r\n");
                        }else if (process.exitValue() == 1){
                            line = String.format("%s %s %s",parameter_tmp,"执行失败！","\r\n");
                        }
                        messagingTemplate.convertAndSend("/ws/identityLog", line);
                        ps2.append(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        KillProcessUtil.closeStream(bufrIn);
                        KillProcessUtil.closeStream(bufrError);
                        KillProcessUtil.destroyProcess(process);
                    }
                }
                // 时间加算
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(beginDate));
                    calendar.add(Calendar.DAY_OF_YEAR,1);
                    beginDate = sdf.format(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        KillProcessUtil.closeStream(ps1);
        KillProcessUtil.closeStream(ps2);
        messagingTemplate.convertAndSend("/ws/identityLog", "End");
        stopThread1();
        //linux无法获取到GA线程的GrpcServer的pid，因此手动关闭
        if (Platform.isLinux() || Platform.isAIX()){
            try {
                Runtime.getRuntime().exec("ps -ef|grep GrpcServer |grep -v grep|cut -c 9-15|xargs kill -9").waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
    }


    //mas线程类
    class SystemStartUpRunnableMas implements Runnable {
        private boolean stopThreadID = false;

        private int index;
        private File file;

        public SystemStartUpRunnableMas(int _index,File file) {
            this.index = _index;
            this.file = file;
        }

        public void stopThread() {
            stopThreadID = true;
        }

        @Override
        public void run() {
            String shellCommand = remoteProperties.getHe_ShellCommand1() + " " + index;
            Process process = null;
            BufferedReader bufrIn = null;
            BufferedReader bufrError = null;
            PrintStream ps = null;
            String line;
            try {
                ps = new PrintStream(new FileOutputStream(file));
                process = Runtime.getRuntime().exec(shellCommand);
                processList.add(process);
                bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "Shift_JIS"));
                bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "Shift_JIS"));
                // 读取输出
                while ((line = bufrIn.readLine()) != null || (line = bufrError.readLine()) != null) {
//                    messagingTemplate.convertAndSend("/ws/masLog"+index,line + "\r\n");
                    ps.append(line + "\r\n");
//                    Thread.currentThread().sleep(5);
                }
                if (stopThreadID) {
                    throw new Exception("stopThread 终止进程");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                KillProcessUtil.closeStream(bufrIn);
                KillProcessUtil.closeStream(bufrError);
                KillProcessUtil.destroyProcess(process);
            }
            KillProcessUtil.closeStream(ps);
        }
    }

    @Override
    public Map init() {
        Map map = new HashMap();

        //初始化世代数...等参数
        // 世代数
        String generation_max = "";
        // 人口数
        String population_size = "";
        // 人口择优范围
        String tournament_size = "";
        // 模拟时间
        String sim_duration = "";
        // 评价指标
        String goal = "";
        // mas数量
        int mas_num = 2;
        map.put("mas_num",mas_num);


        // 读文件　guiyang-1sim_pg.yml
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(remoteProperties.getPATH_1());

            FileInputStream fis = new FileInputStream(dumpFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line ;
            while ((line = br.readLine()) != null) {

                // 世代数，人口数，人口择优范围 有一个为空字符串，就解析读入的文件行
                try {
                    Object obj = yaml.load(line);
                    if (obj instanceof Map) {
                        Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                        for (String key : objMap.keySet()) {
                            if (StringUtils.equals(key, Constants.GENERATION_MAX)) {
                                // 世代数
                                generation_max = getOldValueStr(line);
                                map.put("generation_max",generation_max);
                            } else if (StringUtils.equals(key, Constants.POPULATION_SIZE)) {
                                // 人口数
                                population_size = getOldValueStr(line);
                                map.put("population_size",population_size);
                            } else if (StringUtils.equals(key, Constants.TOURNAMENT_SIZE)) {
                                // 人口择优范围
                                tournament_size = getOldValueStr(line);
                                map.put("tournament_size",tournament_size);
                            }
                        }
                    }

                    // 都有值就退出循
                    if (StringUtils.isNotBlank(generation_max) && StringUtils.isNotBlank(population_size) && StringUtils.isNotBlank(tournament_size)) {
                        break;
                    }
                } catch (ScannerException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            br.close();
            isr.close();
            fis.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 读文件　sim-configuration.yml
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(remoteProperties.getPATH_2());

            FileInputStream fis = new FileInputStream(dumpFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line ;
            while ((line = br.readLine()) != null) {
                try {
                    Object obj = yaml.load(line);
                    if (obj instanceof Map) {
                        Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                        for (String key : objMap.keySet()) {
                            if (StringUtils.equals(key, Constants.SIM_DURATION)) {
                                // 模拟时间
                                sim_duration = getOldValueStr(line);
                                map.put("sim_duration",sim_duration);
                            } else if (StringUtils.equals(key, Constants.GOAL)) {
                                // 评价指标
                                goal = getOldValueStr(line);
                                map.put("goal",goal);

                            }
                        }
                    }

                    // 都有值就退出循环
                    if (StringUtils.isNotBlank(sim_duration)
                            && StringUtils.isNotBlank(goal)) {
                        break;
                    }
                } catch (ScannerException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            br.close();
            isr.close();
            fis.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //初始化log日志
        String dirPath = remoteProperties.getRootLogDirectory() + "/" + Constants.UC02_02_LOG;

        fileUtil.existDirectory(dirPath);
        String sb1 = readLogUtil.readLogFile(dirPath+"/log1");
        String sb2 = readLogUtil.readLogFile(dirPath+"/log2");
        String sb3 = readLogUtil.readLogFile(dirPath+"/"+GetTimeUtil.getNowDate());
        String sb4 = readLogUtil.readLogFile(dirPath+"/log4");
        map.put("sb1",sb1);
        map.put("sb2",sb2);
        map.put("sb3",sb3);
        map.put("sb4",sb4);

        //初始化日期时间
        String beginDate = null,afterDate = null,beginTime = null,afterTime = null;
        if (remoteProperties.getStatus().equals("0")){
            // 开始日期
            beginDate = GetTimeUtil.getNowDate();
            // 结束日期
            afterDate = beginDate;
            beginTime = "0";
            afterTime = "0";

        }else if (remoteProperties.getStatus().equals("1")){
            beginDate = tmpTimeParamConfig.getUc02_02_beginDate();
            afterDate = tmpTimeParamConfig.getUc02_02_afterDate();
            beginTime = tmpTimeParamConfig.getUc02_02_beginTime();
            afterTime = tmpTimeParamConfig.getUc02_02_afterTime();
        }
        map.put("beginDate",beginDate);
        map.put("afterDate",afterDate);
        map.put("beginTime",beginTime);
        map.put("afterTime",afterTime);
        //获取线程是否启动标识
        map.put("status",remoteProperties.getStatus());

        return map;
    }
}
