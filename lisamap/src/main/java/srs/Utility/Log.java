package srs.Utility;

/**
 * Created by WANT on 2018/1/18.
 * 日志控制
 */
public class Log {

    /**
     * true：打印日志，
     * false：否则不打印
     */
    public static boolean ISLog = false;

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag,String msg){
        if(ISLog){ Log.i(tag,msg);}
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag,String msg){
        if(ISLog){ Log.e(tag,msg);}
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag,String msg){
        if(ISLog){ Log.w(tag,msg);}
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void a(String tag,String msg){
        if(ISLog){ Log.a(tag,msg);}
    }
}
