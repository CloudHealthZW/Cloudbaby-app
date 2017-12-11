package app.cloudbaby.co.zw.cloudybaby.biskypackage.utils;

import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Development on 3/31/2016.
 */
public class DatesFormarts {


    public static Date getDateForGraph(String createdAt) {
        Date newdate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {

            newdate = simpleDateFormat.parse(createdAt);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }


    public static long setDatePosted(String createdAt) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date newdate = simpleDateFormat.parse(createdAt);

            return newdate.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String currentDate() {

        GregorianCalendar calendar = new GregorianCalendar();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

        //add 180 days
        calendar.add(Calendar.DAY_OF_MONTH, 180);
        Log.e("Date To expire", dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime());
    }


    public static String getTodayDateSeen(long time) {


        String current = formatMydate(new Date(time), new Date(System.currentTimeMillis()));


        return current;
    }

    public static String getTodayDateAdapter(long time) {


        String current = formatMydate(new Date(time), new Date(System.currentTimeMillis()));


        return current;
    }

    private static String formatMydate(Date date, Date date2) {

        String date1 = null;


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat dateFormatDis = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        String formatedDate = dateFormat1.format(date);
        String formateddate = dateFormat1.format(date2);

        try {
            Date current = dateFormat1.parse(formateddate);
            Date serverDate = dateFormat1.parse(formatedDate);
            if (current.compareTo(serverDate) == 0) {

                date1 = dateFormatDis.format(date);


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date1;
    }

}
