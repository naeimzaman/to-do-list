package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.example.todolist.databinding.PopupWindowBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class AddTaskDialog {
    private PopupWindowBinding binding;
    private FragmentManager fragmentManager;
    private TaskRoomDatabase mDB;
    private boolean isAlarm;
    public int taskId;
    public AddTaskDialog(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
    public void showAddTaskDialog(Context context, LayoutInflater layoutInflater,
                                  OnSaveTaskData onSaveTaskData, OnUpdateTaskData onUpdateTaskData,
                                  Task task) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Task Information");
        binding = PopupWindowBinding.inflate(layoutInflater);
        builder.setView(binding.getRoot());

        updateTaskField(task);

        binding.dateButton.setOnClickListener(view -> {
            showDatePickerDialog(context);
        });
        binding.timeButton.setOnClickListener(view -> {
            //showTimePicker();
            showTimePickerDialog(context);
        });
        binding.alarmCheckbox.setOnClickListener(view -> {
            boolean checked = ((CheckBox)view).isChecked();
            if(checked){
                isAlarm = true;
                Log.d("AddTaskDialog","Alarm set");
            }
            else
                isAlarm = false;
        });


        if(task==null) {
            builder.setPositiveButton("Save", (dialog, which) -> {

            });
        }
        else{
            builder.setPositiveButton("Update", (dialog, which) -> {

            });
        }
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        handleDialogStatus(onSaveTaskData, onUpdateTaskData, task, dialog);
    }

    private void handleDialogStatus(OnSaveTaskData onSaveTaskData, OnUpdateTaskData onUpdateTaskData, Task task, AlertDialog dialog) {
        if(task ==null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String taskName = binding.taskTitle.getText().toString();
                String taskDate = binding.dateField.getText().toString();
                String taskTime = binding.timeField.getText().toString();
                if(validateDialogField(v, taskName, taskDate, taskTime)){
                    Task model = new Task(
                            taskName,
                            taskDate,
                            taskTime,
                            isAlarm
                    );
                    onSaveTaskData.saveTask(model);
                    dialog.dismiss();
                }

            });
        }
        else{
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String taskName = binding.taskTitle.getText().toString();
                String taskDate = binding.dateField.getText().toString();
                String taskTime = binding.timeField.getText().toString();
                if(validateDialogField(v, taskName, taskDate, taskTime)){
                    Task model = new Task(
                            task.id,
                            taskName,
                            taskDate,
                            taskTime,
                            isAlarm
                    );
                    onUpdateTaskData.updateTask(model);
                    dialog.dismiss();
                }

            });
        }
    }

    private boolean validateDialogField(View v, String taskName, String taskDate, String taskTime) {
        if(taskName.equals("") && taskDate.equals("") && taskTime.equals("")){
            Log.d("AddTaskDialog","Empty Title");
            String message = "Please fill up the task information!";
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();

        }
        else if(taskName.equals("")){
            String message = "Please give a task name!";
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        else if(taskDate.equals("")){
            String message = "Please select a task date";
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        else if(taskTime.equals("")){
            String message = "Please select a task time";
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        else{
            return true;
        }
        return false;
    }

    private void updateTaskField(Task task) {
        if(task !=null){
            Log.d("MainActivity", task.getTaskName());
            binding.taskTitle.setText(task.getTaskName());
            binding.dateField.setText(task.getDate());
            binding.timeField.setText(task.getTime());
            taskId = task.id;
            if(task.isAlarm){
                binding.alarmCheckbox.setChecked(true);
                isAlarm = true;
            }
        }
    }

    private void showDatePickerDialog(Context context) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(context, (datePicker, year1, month1, day1) -> {
            String month_string = Integer.toString(month1 +1);
            String day_string = Integer.toString(day1);
            String year_string = Integer.toString(year1);
            String dateMessage = (day_string +
                    "/" + month_string + "/" + year_string);
            binding.dateField.setText(dateMessage);
        },year,month,day).show();
    }

    private void showTimePickerDialog(Context context) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(context, (timePicker, hour1, min) -> {
            String hourS = Integer.toString(hour1);
            String minS = Integer.toString(min);
            binding.timeField.setText(hourS+":"+minS);
        }, hour, minute, DateFormat.is24HourFormat(context)).show();
    }
}
