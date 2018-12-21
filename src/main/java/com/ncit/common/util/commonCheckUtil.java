package com.ncit.common.util;

import com.sun.jna.Platform;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class commonCheckUtil {
    /***
     * 判断操作系统
     * @return true:Linux,Aix false:Windows
     */
    public static boolean isLinux(){
        boolean b = false;
        if (Platform.isLinux()){
            b = true;
        }else if (Platform.isWindows()){
            b = false;
        }else if (Platform.isAIX()){
            b = true;
        }
        return b;
    }

    /***
     * 判断是否为空
     * @param inValue 输入参数
     * @return true:空 false:非空
     */
    public static boolean isEmpty(String inValue) {
        return StringUtils.isEmpty(inValue);
    }

    /***
     * 判断是否为数字
     * @param inValue 输入参数
     * @return true:数字 false:非数字
     */
    public static boolean isAllNumber(String inValue) {
        return inValue.matches("^[0-9]*$");
    }
    /***
     * 判断是否为指定长度
     * @param inValue 输入参数
     * @return true:是6位 false:不是6位
     */
    public static boolean lengthCheck(String inValue) {
        return !inValue.isEmpty() && inValue.length() == 6;
    }

    /***
     * 判断是否超过指定长度
     * @param inValue 输入参数
     * @param maxLength 最大长度
     * @return true:超长 false:未超长
     */
    public static boolean isOverLength(String inValue, int maxLength) {
        return !inValue.isEmpty() && inValue.length() > maxLength;
    }

    /**
     * 检索日期期间判断
     * @param timeFrom
     * @param timeTo
     * @return
     */
    public static boolean timeCheck(String timeFrom, String timeTo){
        boolean result = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date eventTimeFrom = null;
        Date eventTimeTo = null;
        try{
            eventTimeFrom = sdf.parse(timeFrom);
            eventTimeTo = sdf.parse(timeTo);
        } catch(Exception e) {
            e.printStackTrace();
            result = false;
            return result;
        }
        //期间FROM>期间TO
        if (eventTimeTo.before(eventTimeFrom)) {
            result = false;
            return result;
        }
        return result;
    }
}
