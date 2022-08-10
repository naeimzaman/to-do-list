package com.example.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>{
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    private SimpleDateFormat dateOutputFormat = new SimpleDateFormat("dd MMMM, yyyy");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    final private ItemClickListener mItemClickListener;
    private Calendar calendar;
    private List<Task> mTaskEntries = null;
    private Context mContext;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    public TaskListAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }
    @NonNull
    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recyclerview_item, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskViewHolder holder, int position) {
        Task task = mTaskEntries.get(position);
        String taskName = task.getTaskName();
        String taskTime = task.getTime();
        String taskDate = task.getDate();
        Date date = getDate(taskDate);
        String sDateAndTime = taskDate+" "+taskTime;

        Log.d("MainActivity",sDateAndTime);

        Date dateAndTime = getDateAndTime(sDateAndTime);
        Date nowTime = getTodayDate();

        setLayoutColor(holder,dateAndTime,nowTime);

        String dateStr = dateOutputFormat.format(date != null ? date : null);
//        boolean isAlarm = task.isAlarm();

        if(task.isAlarm()){
            holder.alarmIcon.setVisibility(View.VISIBLE);
        }
        else{
            holder.alarmIcon.setVisibility(View.GONE);
        }
        Log.d("TaskListAdapter",dateStr);

        holder.taskName.setText(taskName);
        holder.taskDate.setText(dateStr);
        holder.taskTime.setText(taskTime);

    }
    private Date getTodayDate()  {
        calendar = Calendar.getInstance();
        String strTime = dateTimeFormat.format(calendar.getTime());
        return getDateAndTime(strTime);
    }

    @Nullable
    private Date getDateAndTime(String strTime) {
        Date todayDate=null;
        try {
            todayDate = dateTimeFormat.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return todayDate;
    }
    private void setLayoutColor(@NonNull TaskViewHolder holder, Date time, Date nowTime) {
        if(time.before(nowTime)){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFF794F"));
            Log.d("TaskAdapterList","beforeDate");
        }
        else{

            holder.linearLayout.setBackgroundColor(Color.parseColor("#FF4CAF50"));
            Log.d("TaskAdapterList","afterDate");
        }
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
    public int getItemCount() {
        if (mTaskEntries == null) {
            return 0;
        }
        return mTaskEntries.size();
    }

    public void setTasks(List<Task> taskEntries) {
        this.mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }

    public List<Task> getTasks() {
        return mTaskEntries;
    }
    public interface ItemClickListener {
        void onItemClickListener(Task task);
        void deleteClickListener(Task task);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
        TextView taskName;
        TextView taskDate;
        TextView taskTime;
        ImageView alarmIcon;
        ImageButton deleteButton;
        LinearLayout linearLayout;
        public TaskViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.recyclerviewLayout);
            taskName = itemView.findViewById(R.id.showTaskTitle);
            taskDate = itemView.findViewById(R.id.showTaskDate);
            taskTime = itemView.findViewById(R.id.showTaskTime);
            alarmIcon = itemView.findViewById(R.id.showAlarmAlert);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(view -> {
                mItemClickListener.deleteClickListener(mTaskEntries.get(getAdapterPosition()));
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task element = mTaskEntries.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(element);
        }
//        public void deleteTaskFromId(){
//            Task task = mTaskEntries.get(getAdapterPosition());
//            TaskViewModel taskViewModel = null;
//            taskViewModel.deleteTaskFromId(task);
//        }
    }
}
