package com.ncit.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeUtil {

    //获取当前日期
    public static String getNowDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        // 开始日期
        String beginDate = sdf.format(new Date());
        return beginDate;
    }
}
