package de.oliver.logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {

    public static Logger logger;

    static {
        logger = new Logger(new ArrayList<>());
    }

    private final List<LogLevel> exclusiveLogLevels;

    private Logger(List<LogLevel> exclusiveLogLevels){
        this.exclusiveLogLevels = exclusiveLogLevels;
    }

    public void log(Class c, LogLevel logLevel, String message){

        if(exclusiveLogLevels.contains(logLevel)){
            return;
        }

        Date date = new Date(System.currentTimeMillis());
        String dateS = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

        String logPrefix = "[" + dateS + "]";

        if(c != null){
            logPrefix += " [" + c.getSimpleName() + "]";
        }

        if(logLevel != null){
            logPrefix += " [" + logLevel + "]";
        } else {
            logPrefix += " [INFO]";
        }

        System.out.println(logPrefix + ": " + message);
    }

    public void log(LogLevel logLevel, String message){
        log(null, logLevel, message);
    }

    public void log(String message){
        log(null, null, message);
    }


    public List<LogLevel> getExclusiveLogLevels() {
        return exclusiveLogLevels;
    }
}
