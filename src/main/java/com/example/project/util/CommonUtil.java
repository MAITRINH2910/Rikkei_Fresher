package com.example.project.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;


public class CommonUtil {
    public static Double KelvintoCelsius(double Kel) {
//        NumberFormat degreeFormat =new DecimalFormat("#0.00");
        return  Kel - 273.15;
    }

    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        Date dateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        return dateDate;
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
