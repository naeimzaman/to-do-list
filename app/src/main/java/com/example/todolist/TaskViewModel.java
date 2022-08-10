package com.example.todolist;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository mRepository;

    public TaskViewModel(Application application) {
        super(application);
        mRepository = new TaskRepository(application);
    }

    LiveData<List<Task>> getloadAllTasks() {
        return mRepository.getloadAllTasks();
    }

    LiveData<Integer> insert(Task task) {
        return  mRepository.insert(task);
    }
    void updateAnExistingRow(Task task){
        mRepository.updateAnExistingRow(task);
    }
    void deleteTaskFromId(Task task){
        mRepository.deleteTaskFromId(task);
    }
}
