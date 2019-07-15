package com.smartmanager.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;


/**
 * Represents a task in a list.
 */
@Entity(tableName = "todo_items")
public class TodoItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;

    private String description;

    private int priority;

    @NonNull
    private String category;

    @ColumnInfo(name = "has_reminder")
    private boolean hasReminder;

    @ColumnInfo(name = "created_date")
    private Date createdDate;

    @ColumnInfo(name = "reminder_date")
    private Date reminderDate;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @Ignore
    public TodoItem(@NonNull String title, Date reminderDate) {
        this.title = title;
        this.description = "";
        this.reminderDate = reminderDate;
    }

    @Ignore
    public TodoItem(@NonNull String title, String description, Date reminderDate) {
        this.title = title;
        this.description = description;
        this.reminderDate = reminderDate;
    }

    @Ignore
    public TodoItem(@NonNull String title, int priority, String category) {
        this.title = title;
        this.priority = priority;
        this.category = category;
    }

    public TodoItem(@NonNull String title, String description, int priority,
                    String category, boolean hasReminder, Date reminderDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.hasReminder = hasReminder;
        this.reminderDate = reminderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean hasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
