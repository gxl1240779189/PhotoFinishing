package utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateUtils {

    public static String ConverToString(Date date)  
    {  
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd");  
          
        return df.format(date);  
    }  

    public static Date ConverToDate(String strDate) throws Exception  
    {  
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd");  
        return df.parse(strDate);  
    }  
}
