package com.example.project.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;


public class CommonUtil {
    public static String KelvintoCelsius(double Kel) {
        NumberFormat degreeFormat =new DecimalFormat("#0.00");
        return  degreeFormat.format(Kel - 273.15);
    }

    public static String formatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
        return simpleDateFormat.format(new Date());
    }

//    public static Date formatCurrentDate(){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        return dtf.format(now);
//    }
    public static String formatToString( String ts) {
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(ts);
        return formattedDate;
    }

    /**
     * Parse curTime to String
     * @return
     */
    public static String curTimeToString() {
        Timestamp ts= new Timestamp(System.currentTimeMillis());
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(ts);
        return formattedDate;
    }

}
