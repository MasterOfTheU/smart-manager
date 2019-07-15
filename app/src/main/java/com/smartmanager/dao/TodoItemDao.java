package com.smartmanager.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartmanager.model.TodoItem;

import java.util.List;

/**
 * This interface provides general methods for data saving and modification.
 */
@Dao
public interface TodoItemDao {

    @Insert
    void insert(TodoItem todoItem);

    @Update
    void update(TodoItem todoItem);

    @Delete
    void delete(TodoItem todoItem);

    /**
     * Removes all tasks from database.
     */
    @Query("DELETE FROM todo_items")
    void deleteAllTodos();

    /**
     * Returns list of all tasks ordered by category and priority
     * from highest in category to the lowest. <br>
     * <b>1</b> - the highest priority    <br>
     * <b>3</b> - the lowest priority
     *
     * @return list of tasks
     */
    @Query("SELECT * FROM todo_items ORDER BY category DESC, priority ASC")
    LiveData<List<TodoItem>> getAllTodoItems();

    /**
     * Returns list of recently completed tasks.
     *
     * @return list of completed tasks
     */
    @Query("SELECT * FROM todo_items WHERE is_completed='true' ORDER BY created_date DESC")
    LiveData<List<TodoItem>> getCompletedTodoItems();

}
