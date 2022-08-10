package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_details")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "task_name")
    public String taskName;
    @ColumnInfo(name="date")
    public String date;
    @ColumnInfo(name="time")
    public String time;
    @ColumnInfo(name="is_alarm")
    public boolean isAlarm;

    @Ignore
    public Task(String taskName,String date,String time,boolean isAlarm){
        this.taskName=taskName;
        this.date=date;
        this.time=time;
        this.isAlarm=isAlarm;
    }
    public Task(int id,String taskName,String date,String time,boolean isAlarm){
        this.id=id;
        this.taskName=taskName;
        this.date=date;
        this.time=time;
        this.isAlarm=isAlarm;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }
}
