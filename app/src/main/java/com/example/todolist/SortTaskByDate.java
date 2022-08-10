package com.example.todolist;

import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SortTaskByDate {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

    public SortTaskByDate(List<Task> tasks){
        Collections.sort(tasks, (item1, item2) -> {
            String taskTime = item1.getTime();
            String taskDate = item1.getDate();
            String sDateAndTime = taskDate + " " + taskTime;
            Log.d("MainActivity", sDateAndTime);
            Date date1 = getDateAndTime(sDateAndTime);
            taskTime = item2.getTime();
            taskDate = item2.getDate();
            sDateAndTime = taskDate + " " + taskTime;
            Date date2 = getDateAndTime(sDateAndTime);
            if (date1 != null && date2 != null) {
                boolean b1;
                boolean b2;
                b1 = date1.after(date2);
                b2 = date1.before(date2);
                if (b1 != b2) {
                    if (b1) {
                        return -1;
                    }
                    if (!b1) {
                        return 1;
                    }
                }
            }
            return 0;
        });
    }
    @Nullable
    private Date getDateAndTime(String strTime) {
        Date date = null;
        try {
            date = dateTimeFormat.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
