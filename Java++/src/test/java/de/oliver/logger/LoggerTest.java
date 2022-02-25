package de.oliver.logger;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class LoggerTest {

    private final String EXAMPLE_MESSAGE = "Hello, world!";

    @Test
    void log() {
        Logger logger = new Logger(new ArrayList<>());
        String logS = logger.log(LoggerTest.class, LogLevel.INFO, EXAMPLE_MESSAGE);
        assert logS != null && logS.length() > 0;
        assert logS.contains("[INFO]") && logS.contains(EXAMPLE_MESSAGE) && logS.contains(LoggerTest.class.getSimpleName());

        logS = logger.log(LogLevel.INFO, EXAMPLE_MESSAGE);
        assert logS != null && logS.length() > 0;

        logS = logger.log(EXAMPLE_MESSAGE);
        assert logS != null && logS.length() > 0 && logS.contains("[INFO]");

    }

    @Test
    void log2(){
        Logger logger = new Logger(Arrays.asList(LogLevel.DEBUG));
        String logS = logger.log(LoggerTest.class, LogLevel.DEBUG, EXAMPLE_MESSAGE);
        assert logS == null;
    }

}