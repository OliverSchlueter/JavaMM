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

    public Logger(List<LogLevel> exclusiveLogLevels){
        this.exclusiveLogLevels = exclusiveLogLevels;
    }

    public String log(Class c, LogLevel logLevel, String message){

        if(exclusiveLogLevels.contains(logLevel)){
            return null;
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

        String finalLog = logPrefix + ": " + message;
        System.out.println(finalLog);

        return finalLog;
    }

    public String log(LogLevel logLevel, String message){
        return log(null, logLevel, message);
    }

    public String log(String message){
        return log(null, null, message);
    }


    public List<LogLevel> getExclusiveLogLevels() {
        return exclusiveLogLevels;
    }
}
