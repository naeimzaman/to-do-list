package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TaskListAdapter.ItemClickListener, OnSaveTaskData, OnUpdateTaskData {

    private ActivityMainBinding binding;

    private TaskViewModel mTaskViewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else {
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
    }

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

    private List<Task> mTaskEntries = null;

    private TaskListAdapter mAdapter;

    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mContext = this;
        setContentView(binding.getRoot());
        initiateViewModel();
        initView();
        swipeDeleteItem();
//        registerReceiver(alarmReceiver);
    }

    private void swipeDeleteItem() {
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        Log.d("ItemTouchHelper", "onMove");
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        List<Task> tasks = mAdapter.getTasks();
                        Log.d("ItemTouchHelper",String.valueOf(position));
                        Task task = tasks.get(position);

                        mTaskViewModel.deleteTaskFromId(task);
                        AlertUtil.cancelAlarm(mContext, task.id);
                        AlertUtil.cancelNotification(mContext, task.id);
                        mAdapter.notifyDataSetChanged();
                    }
                });

        helper.attachToRecyclerView(binding.recyclerview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskList();
    }

    private void getTaskList() {
        mTaskViewModel.getloadAllTasks().observe(this, tasks -> {
            new SortTaskByDate(tasks);
            mAdapter.setTasks(tasks);
        });
    }

    private void initView() {
        mAdapter = new TaskListAdapter(this, this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerview.setAdapter(mAdapter);
        binding.fab.setOnClickListener(view -> {
            showAddTaskDialog();
        });
    }

    private void initiateViewModel() {
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
    }

    private void showAddTaskDialog() {
        AddTaskDialog ad = new AddTaskDialog(getSupportFragmentManager());
        ad.showAddTaskDialog(this, getLayoutInflater(), this, this, null);
    }

    @Override
    public void onItemClickListener(Task task) {
        Log.d("MainActivity", "OnItemClickLister");
        AddTaskDialog ad = new AddTaskDialog(getSupportFragmentManager());
        ad.showAddTaskDialog(this, getLayoutInflater(), this, this, task);
    }

    @Override
    public void deleteClickListener(Task task) {
        AlertUtil.cancelAlarm(this, task.id);
        AlertUtil.cancelNotification(this, task.id);
        mTaskViewModel.deleteTaskFromId(task);

    }

    @Override
    public void saveTask(Task task) {
        String taskTime = task.getTime();
        String taskDate = task.getDate();
        Date date = getDate(taskDate);
        Date time = getTime(taskTime);
        mTaskViewModel.insert(task).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (task.isAlarm) {
                    if (mContext != null) {
                        AlertUtil.setAlarm(mContext, date, time, integer);
                        Log.d("MainActivity", "setAlarm");
                    }

                }
                if (mContext != null) {
                    AlertUtil.setNotification(mContext, date, time, integer);
                }
            }
        });
    }

    @Override
    public void updateTask(Task task) {
        String taskTime = task.getTime();
        String taskDate = task.getDate();
        Date date = getDate(taskDate);
        Date time = getTime(taskTime);

        AlertUtil.cancelNotification(this, task.id);
        AlertUtil.setNotification(this, date, time, task.id);

        AlertUtil.cancelAlarm(this, task.id);
        if (task.isAlarm) {
            AlertUtil.setAlarm(this, date, time, task.id);
            Log.d("MainActivity", "setAlarm");
        }

        mTaskViewModel.updateAnExistingRow(task);
    }

    @Nullable
    private Date getTime(String taskTime) {
        Date time = null;
        try {
            time = timeFormat.parse(taskTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Nullable
    private Date getDate(String taskDate) {
        Date date = null;

        try {
            date = dateFormat.parse(taskDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the correct item was clicked
        if (item.getItemId() == R.id.night_mode) {
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            }
            else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
        }
        return true;
    }
//    public void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            CharSequence name = "todoAppChannel";
//            String description = "temp";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("todoAppChannel",name,importance);
//            channel.setDescription(description);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }
}