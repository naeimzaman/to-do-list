package com.example.todolist;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

public class TaskRepository {

    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks =mTaskDao.loadAllTasks();
    }

    public LiveData<List<Task>> getloadAllTasks() {

        return mAllTasks;
    }
    public LiveData<Integer> insert(Task task) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            long id = mTaskDao.insert(task);
            liveData.postValue((int)id);
        });
        return liveData;
    }

    void updateAnExistingRow(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            int id = mTaskDao.updateAnExistingRow(task.id,task.taskName,task.date,task.time, task.isAlarm);
            Log.d("TaskRepository", "writing to database complete. id: " + id);
        });
    }
    void deleteTaskFromId(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            mTaskDao.deleteTaskFromId(task.id);
            Log.d("TaskRepository", "writing to database complete. id: ");
        });
    }
}
