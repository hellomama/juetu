package com.tony.juetu.manager;

import android.content.Context;

import com.tony.juetu.App;
import com.tony.juetu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dev on 7/3/18.
 */

public class ChatTimeHelper {
    private Context context;
    private static ChatTimeHelper sInstance;

    public static ChatTimeHelper getInstance(){
        if (sInstance == null)
        {
            synchronized (ChatTimeHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new ChatTimeHelper();
                }
            }
        }
        return sInstance;
    }

    public ChatTimeHelper() {
        context = App.getAppContext();
    }

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    /**
     *  时间戳格式转换
     */
    public String getNewChatTime(long timeSamp) {
        String result = "";
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timeSamp);

        String dayTimeFormat = context.getString(R.string.day_format);
        String weekTimeFormat = context.getString(R.string.week_format)
                + " " + context.getString(R.string.day_format);
        String yearTimeFormat = context.getString(R.string.year_format)
                + " " + context.getString(R.string.day_format);

        long temp = (getTodayEndTime() - timeSamp) / 1000;
        if(temp >= 7 * 24 * 60 * 60){
            result = getTime(timeSamp, yearTimeFormat);
        }
        else if(temp < 7 * 24 * 60 * 60 && temp >= 2 * 24 * 60 * 60){
            result = getWeekTime(timeSamp, weekTimeFormat);
        }
        else if(temp < 2* 24 * 60* 60 && temp >= 24 * 60 * 60){
            result = context.getString(R.string.yesterday) + " " + getHourAndMin(timeSamp, dayTimeFormat);
        }
        else{
            result = getHourAndMin(timeSamp, dayTimeFormat);
        }
        return result;
    }

    private long getTodayEndTime() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 999);
        Date todayDate = today.getTime();
        return todayDate.getTime();
    }

    public String tranferDayFormat(long timeSamp){
        String dayTimeFormat = "HH:mm";
        return getHourAndMin(timeSamp, dayTimeFormat);
    }

    /**
     * 当天的显示时间格式
     * @param time
     * @return
     */
    private String getHourAndMin(long time, String dayTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dayTimeFormat);
        return format.format(new Date(time));
    }

    /**
     * 同一周的显示时间格式
     * @param time
     * @return
     */
    private String getWeekTime(long time, String weekTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(weekTimeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     * @param time
     * @param yearTimeFormat
     * @return
     */
    private String getTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }
}
