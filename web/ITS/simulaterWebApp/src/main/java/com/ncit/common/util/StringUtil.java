package com.ncit.common.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    /**
     * 取字符串中的值
     *
     * @param _oldStr
     * @return
     */
    public static String getOldValueStr(String _oldStr) {
        String value = "";

        int startIndex = StringUtils.indexOf(_oldStr, ":");
        int bIndex = StringUtils.indexOf(_oldStr, "#");

        if (startIndex != -1) {

            if (bIndex != -1) {
                value = StringUtils.substring(_oldStr, startIndex + 1, bIndex);
            } else {
                value = StringUtils.substring(_oldStr, startIndex + 1);
            }
        }
        return StringUtils.trim(value);
    }

}
