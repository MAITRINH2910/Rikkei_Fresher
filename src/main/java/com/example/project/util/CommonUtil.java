package com.example.project.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;


public class CommonUtil {
    public static String FahtoCelsius(double Fah) {
        NumberFormat degreeFormat =new DecimalFormat("#0.00");
        return  degreeFormat.format((Fah - 32) * 5/9);
    }

    public static String formatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
        return simpleDateFormat.format(new Date());
    }

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
