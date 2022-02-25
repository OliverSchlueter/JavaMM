package de.oliver.javapp.main;

import de.oliver.logger.LogLevel;
import de.oliver.logger.Logger;

public class Main {

    public static void main(String[] args) {
        Logger.logger.log(Main.class, LogLevel.DEBUG, "Hello world");
    }

}
