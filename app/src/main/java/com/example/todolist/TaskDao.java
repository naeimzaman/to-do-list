package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task_details")
    LiveData<List<Task>> loadAllTasks();
    @Insert
    long insert(Task task);

    @Query("UPDATE task_details SET task_name = :taskTitle, date = :taskDate, " +
            "time = :taskTime,is_alarm = :isAlarm WHERE id = :taskId")
    int updateAnExistingRow(int taskId, String taskTitle, String taskDate, String taskTime,
                             boolean isAlarm);

    @Query("DELETE FROM task_details WHERE id = :taskId")
    void deleteTaskFromId(int taskId);
}
