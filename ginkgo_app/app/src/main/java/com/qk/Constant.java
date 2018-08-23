package com.qk;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 常量
 */
public class Constant {
    /**
     * 服务器地址
     */
    private static String server_url="http://10.2.72.112:5000/ginkgo/";
    //private static String server_url="http://10.2.72.155:8080/app_server/";
//    private static String server_url="http://10.2.72.112:8761/";
    /**
     * 检查token是否有效接口
     */
    public final static String TOKEN_VALID_URL = server_url+"sys/checkToken";
    /**
     * 修改密码接口
     */
    public final static String CHANGE_PASSWORD = server_url+"user/modifyPwd";
    /**
     * 测试图片上传地址
     */
    public static String UPPHTOT="http://10.2.72.105:8080/app_server/file/upphoto";
    /**
     * 刷新token接口
     */
    public static final String NEW_TOKEN= server_url+"user/refreshToken";
    /**
     * 订单检索
     */
    public static final String ORDER_SEARCH= server_url+"orders/findOrdersSimpleInfoList";
    /**
     * 订单待审批列表接口
     * */
    public static final String ORDER_APPORVAL= server_url+"ordermodify/findOrdermodifyShList";
    /**
     *获取验证码接口
     */
    public static final String GET_CODE= "http://10.2.72.105:8080/HOWO/a/sys/register/getcode";
    /**
     * 获取列表接口
     */
    public static final String STOCK= server_url+"stock/findStockList";
    /**
     * 登录接口
     */
    public static final String LOGIN = server_url+"user/login";
    /**
     * 退出登录接口
     */
    public static final String LOGOUT = server_url+"user/logout";
    /**
     * 获取个人信息接口
     */
    public static final String USER_MESSAGE = server_url+"user/getUserInfo";
    /**
     * 获取个人信息接口
     */
    public static final String USER_MESSAGE_UPDATE = server_url+"user/modifyUserInfo";
    /**
     * 检查更新接口
     */
    public static final String GET_VERSION = server_url+"sys/getVersion";


    /**********************************用户中心提示******************************************/

    /**
     * 用户名为空时提醒
     */
    public final static String EMPTY_USERNAME = "请输入账号";
    /**
     * 旧密码为空时提醒
     */
    public final static String EMPTY_OLD_PASSWORD = "请输入原密码";
    /**
     * 密码为空时提醒
     */
    public final static String EMPTY_PASSWORD = "请输入密码";
    /**
     * 新密码为空时提醒
     */
    public final static String EMPTY_NEW_PASSWORD = "请输入新密码";
    /**
     * 确认密码为空时提醒
     */
    public final static String EMPTY_CONFIRM_PASSWORD = "再次输入新密码";
    /**
     * 确认密码与新密码不一致时提醒
     */
    public final static String DIFFER_CONFIRM_PASSWORD = "确认密码与新密码不一致";
    /**
     * 密码需要是6到16位数字或字母
     */
    public final static String PASSWORD_FORMAT_ERROR = "请输入6到16位数字或字母密码";

    /**********************************静态常量******************************************/

    /**
     * 默认每页请求的个数
     */
    public static final int PAFESIZE=10;
    /**
     * 日志TAG
     */
    public static final String LOGTAG="CNBLOG";
    /**
     * OKGO连接超时的时间
     */
    public static final long OKGO_CONNECT_TIME = 3000;
    /**
     * token有效code码
     */
    public final static String TOKEN_VALID_CODE = "11";
    /**
     * 成功code码
     */
    public final static String SUCCESS_CODE = "0";
    /**
     * viewpager数量
     */
    public static final int VIEWPAGER_NUM = 5;
    /**
     * 未登录或登录超时常量
     */
    public static  final  String RELOGIN_CODE = "-4";
    /**
     * token过期标志
     */
    public static  final  String TOKEN_OVERTIME = "-3";
    /**
     * 手机号码长度
     */
    public static  final   int PHONE_LENGTH = 11;
    /**
     * 邮箱格式 正则表达式
     */
    public static  final   String EMAIL_FORMAT = ".+@.+\\.[a-z]+";

    /********************************************* 版本号******************************/
    public static final  String VERSION = "V1.0.0";

}
