package org.example.utils;

import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CommonTools {
    private static Logger logger = Logger.getLogger("CommonTools");
    private static DateFormatter dateFormatter = new DateFormatter("yyyyMMdd");
    private static DateFormatter dateFormatter2 = new DateFormatter("yyyy-MM-dd HH:mm:ss");

    public static String generateUuid() {
        UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(new Date().getTime()).getBytes());
        return uuid.toString();
    }

    public static Date parse(String strDate) {
        Date result = null;
        try {
            result = dateFormatter.parse(strDate, Locale.CHINA);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "parsing date exception, ex=" + e.getMessage());
        }
        return result;
    }

    public static Date parse(Date date, int diffDays, String unit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (TimeUnit.DAY.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.DAY_OF_MONTH, diffDays);
        } else if (TimeUnit.SECOND.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.SECOND, diffDays);
        } else if (TimeUnit.MINUTE.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.MINUTE, diffDays);
        } else if (TimeUnit.HOUR.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.HOUR, diffDays);
        } else if (TimeUnit.MONTH.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.MONTH, diffDays);
        } else if (TimeUnit.YEAR.name().equalsIgnoreCase(unit)) {
            calendar.add(Calendar.YEAR, diffDays);
        }
        Date result = calendar.getTime();
        return result;
    }

    public static String formatDate(Date date) {
        return dateFormatter2.print(date, Locale.CHINA);
    }

    public static void main(String[] args) {
        System.out.println(formatDate(new Date()));
    }
}
