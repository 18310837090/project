package com.ncit.common.util;

import com.ncit.common.base.Kernel32;
import com.sun.jna.Platform;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

public class KillProcessUtil {

    // 获取的 pid
    public static long getPid(Process process) {
        long pid = -1;
        Field field ;
        if (Platform.isWindows()) {
            try {
                field = process.getClass().getDeclaredField("handle");
                field.setAccessible(true);
                pid = Kernel32.INSTANCE.GetProcessId((Long) field.get(process));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (Platform.isLinux() || Platform.isAIX()) {
            try {
                Class<?> clazz = Class.forName("java.lang.UNIXProcess");
                field = clazz.getDeclaredField("pid");
                field.setAccessible(true);
                pid = (Integer) field.get(process);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
        }

        return pid;
    }

    // 杀进程
    public static void killProcessTreeCmd(Long Pid) {
        String shellCommand = null;
        if(Pid !=null) {
            if (Platform.isWindows()) {
                shellCommand = "cmd.exe /c taskkill /PID "+ Pid + " /F /T ";
            } else if (Platform.isLinux() || Platform.isAIX()) {
                shellCommand = "kill -s -9 "+ Pid;
            }
            try {
                Runtime.getRuntime().exec(shellCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //关闭流方法
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
                e.printStackTrace();
            }
        }
    }

    //关闭子进程方法
    public static void destroyProcess(Process process) {
        // 销毁子进程
        if (process != null) {
            try {
                process.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            process.destroy();
        }
    }
}
