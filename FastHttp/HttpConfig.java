package cn.haodian.demowidget.fasthttp;

/**
 * Created by 立才 on 2017/1/17.
 */

public class HttpConfig {

    private static MyLogger logger = MyLogger.getLogger("HttpTools");
    public static final int POST=1;
    public static final int GET=2;
    public static final int REQUEST_SUCCESS = 200;//成功
    public static final int NET_ERROR = 400;//网络问题
    public static final int SERVER_ERROR = 500;//服务端异常

    private static int TIME_OUT= 10;//默认超时时间(秒)


    private static String code="code";
    private static String msg="message";

    public static boolean IS_PRINT_LOG=false;
    public static void setPrintLog(boolean set){
        IS_PRINT_LOG=true;
    }

    public static boolean isPrintlog(){
        return IS_PRINT_LOG;
    }

    public static MyLogger logger(){
        return logger;
    }

    public static void setTimeOut(int second){
        TIME_OUT=second;
    }

    public static int getTimeOut(){
        return TIME_OUT*1000;
    }

    public static void setCodeTag(String code_tag){
        code=code_tag;
    }

    public static String getCodeTag(){
        return code;
    }

    public static void setMsgTag(String code_tag){
        code=code_tag;
    }

    public static String getMsgTag(){
        return code;
    }



}
