package com.phongvan.bomberman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final DateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    public static final String INFO  = "INFO";
    public static final String ERROR = "ERROR";
    public static final String WARN  = "WARN";

    private static String createMessages(final String level, String location, String msg) {
        Date date = new Date(System.currentTimeMillis());
        return String.format("[%s][%s][%s] : %s", format.format(date), level, location, msg);
    }

    public static void log(final String level, String location, String msg) {
        if (!level.equals(INFO) && !level.equals(WARN) && !level.equals(ERROR)) {
            System.err.println(createMessages(ERROR, location,"Unknown level"));
        } else {
            if (level.equals(ERROR))
                System.err.println(createMessages(level, location, msg));
            else
                System.out.println(createMessages(level, location, msg));
        }
    }
}
