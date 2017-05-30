package com.example.quickreminder.Activity;

/**
 * Created by Dell on 8/6/2016.
 */
public class ReminderModel {

    private String taskId;

    private String taskTitle;

    private String TaskDescription;

    private long taskTimestamp;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTaskTimestamp() {
        return taskTimestamp;
    }

    public void setTaskTimestamp(long taskTimestamp) {
        this.taskTimestamp = taskTimestamp;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }
}
