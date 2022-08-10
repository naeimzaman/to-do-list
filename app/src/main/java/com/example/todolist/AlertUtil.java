package com.example.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class AlertUtil {

    public static void setAlarm(Context context, Date date, Date time,int reqCode) {
        long alarmTime = getCalendar(date, time);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime-30000, pendingIntent);

    }

    public static void cancelAlarm(Context context,int reqCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

    public static void setNotification(Context context,Date date, Date time,int reqCode){
        Log.d("alertUtil","set notification");

        long alarmTime = getCalendar(date, time)-300000;
        Intent intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
    }
    public static void cancelNotification(Context context, int reqCode){
        Log.d("alertUtil","set notification");

        Intent intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @NonNull
    private static long getCalendar(Date date, Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        Calendar targetCal = Calendar.getInstance();
        targetCal.set(year,month,day,hour,min);
        return targetCal.getTimeInMillis();
    }


}
