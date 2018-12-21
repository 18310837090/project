package com.ncit.common;

public class Constants {
    public static final String UC03_02_LOG = "uc03_02_log";
    public static final String UC02_02_LOG = "uc02_02_log";
    public static final String UC01_03_LOG = "uc01_03_log";


    // 线程数量
    public static int THREAD_NUMBER_MAS = 2;
    public static int THREAD_NUMBER = 1;

    public static final String GENERATION_MAX = "generation-max";

    public static final String POPULATION_SIZE = "population-size";

    public static final String TOURNAMENT_SIZE = "tournament-size";

    public static final String SIM_DURATION = "sim_duration";

    public static final String GOAL = "goal";

    //用户活跃状态
    public static final String USER_ACTIVE = "yes";


    public static final String DB_FILE_PATH = "application.yml";
    public static final String TMP_PARAM_FILE_PATH = "temp_file/tmp.yml";
//    public static final String CONFIGFILEPATH = "/config.properties";
    public static final String PASSWORD = "PASSWORD";
    public static final String STATUS = "status";
    public static final String ENTER_PAGE = "enterPage";


    public static final String UC01_03_BEGINDATE = "uc01_03_beginDate";
    public static final String UC01_03_AFTERDATE = "uc01_03_afterDate";
    public static final String UC02_02_BEGINDATE = "uc02_02_beginDate";
    public static final String UC02_02_AFTERDATE = "uc02_02_afterDate";
    public static final String UC02_02_BEGINTIME = "uc02_02_beginTime";
    public static final String UC02_02_AFTERTIME = "uc02_02_afterTime";
    public static final String UC03_02_DATE = "uc03_02_date";
    public static final String UC03_02_TIMEFROM = "uc03_02_timeFrom";
    public static final String UC03_02_TIMETO = "uc03_02_timeTo";

    //用户名最大长度
    public static final int USER_ID_MAX_LENGTH = 12;

    //密码最大长度
    public static final int PASSWORD_MAX_LENGTH = 12;


    /**
     * UC01处理结果
     */

    public static final String Message_Module_01_010_OK_001 = "上传成功！";
    public static final String Message_Module_01_010_NG_001 = "上传失败，不能上传空文件！";

    public static final String Message_Module_01_020_NG_001 = "开始日期不能大于结束日期!";
    public static final String Message_Module_01_020_NG_002 = "开始日期不能为空!";
    public static final String Message_Module_01_020_NG_003 = "结束日期不能为空!";

    public static final String Message_Module_02_010_OK_001 = "设置成功！";
    public static final String Message_Module_02_010_NG_001 = "设置失败！";

    public static final String Message_module_INIT_OK_001 = "登录成功";
    public static final String Message_module_INIT_NG_001 = "用户名不能为空";
    public static final String Message_module_INIT_NG_002 = "用户名长度超过12位";
    public static final String Message_module_INIT_NG_003 = "密码不能为空";
    public static final String Message_module_INIT_NG_004 = "密码长度超过12位";
    public static final String Message_module_INIT_NG_005 = "该用户不存在";
    public static final String Message_module_INIT_NG_006 = "登录密码不正确";

    /**
     * UC03_01处理结果
     */
    public static final String Message_Module_03_010_NG_001 = "该目录下无记录";
    public static final String Message_Module_03_010_OK_001 = "查询成功";

    /**
     * UC03_03处理结果
     */
    public static final String Message_Module_03_030_OK_001 = "查询成功";
    public static final String Message_Module_03_030_NG_001 = "找不到指定的文件";
    public static final String Message_Module_03_030_NG_002 = "文件类型错误!";



    /**
     * UC04处理结果
     */
    public static final String Message_Module_04_010_OK_001 = "密码修改成功,请重新登录";
    public static final String Message_Module_04_010_NG_001 = "请输入旧密码";
    public static final String Message_Module_04_010_NG_002 = "请输入新密码";
    public static final String Message_Module_04_010_NG_003 = "请输入新密码确认";
    public static final String Message_Module_04_010_NG_004 = "密码不能超过12位";
    public static final String Message_Module_04_010_NG_005 = "新密码和新密码确认不一致";
    public static final String Message_Module_04_010_NG_006 = "密码加密失败";
    public static final String Message_Module_04_010_NG_007 = "密码更新失败";
    public static final String Message_Module_04_010_NG_008 = "旧密码输入不正确";

    /**
     * 页面跳转用
     */
    public static final String PAGE_MODULE_INIT_010 = "module_init/login";
    public static final String PAGE_MODULE_INIT_020 = "module_init/index";

    public static final String PAGE_MODULE_01_010 = "module_01/trafficDataUpload";
    public static final String PAGE_MODULE_01_020 = "module_01/trafficDataSearch";
    public static final String PAGE_MODULE_01_030 = "module_01/databaseImport";

    public static final String PAGE_MODULE_02_010 = "module_02/setParameter";
    public static final String PAGE_MODULE_02_020 = "module_02/systemStart";
    public static final String PAGE_MODULE_02_030 = "module_02/logReview";

    public static final String PAGE_MODULE_03_010 = "module_03/timingResultDownload";
    public static final String PAGE_MODULE_03_020 = "module_03/assessResult";
    public static final String PAGE_MODULE_03_030 = "module_03/assessResultDownload";

    public static final String PAGE_MODULE_04_010= "module_04/changePassword";

    /**
     * 线程存在
     */
    public static final String IF_NOT_HAVE="0";

    /**
     * 线程存在
     */
    public static final String IF_HAVE="1";

    /**
     * 是否-否
     */
    public static final Boolean IF_NO=false;

    /**
     * 是否-是
     */
    public static final Boolean IF_YES=true;

    /**
     * 年月日分隔符
     */
    public static final String SLASH = "/";

    /**
     * 年、月汉字
     */
    public static final String YEAR = "年";
    public static final String MONTH = "月";

    /**
     * 密码是否加密保存
     *  #  1 : 密文保存
     *  #  0 : 平稳保存
     */
    public static final String ENCRYPT_YES = "1";
    public static final String ENCRYPT_NO = "0";

    /**
     * 图片扩展名
     */
    public static final String[] IMAGE_TYPE = {"jpg","jpeg", "gif", "png", "bmp"};

    /**
     * 空白字符串
     */
    public static final String COMMAND_STR = "";

    /**
     * 文件路径
     */
    public static final String FILE_PATH_DIR = "file.path.dir";

    /**
     * 每页显示的条数
     */
    public static final int PAGE_SIZE = 20;
}
