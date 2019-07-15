package com.smartmanager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.smartmanager.model.TodoItem;
import com.smartmanager.repository.TodoItemRepository;

import java.util.List;


/**
 * A bridge between the Repository and the View level. Provides View level with
 * data querying methods.
 */
public class TodoItemViewModel extends AndroidViewModel {

    private TodoItemRepository mRepository;
    private LiveData<List<TodoItem>> todoItemsList;
    private LiveData<List<TodoItem>> mCompletedTodoItemsList;


    public TodoItemViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TodoItemRepository(application);
        todoItemsList = mRepository.getTodoItemsList();
        mCompletedTodoItemsList = mRepository.getCompletedTodoItemsList();
    }

    public LiveData<List<TodoItem>> getTodoItemsList() {
        return todoItemsList;
    }

    public void insert(TodoItem todoItem) {
        mRepository.insert(todoItem);
    }

    public void update(TodoItem todoItem) {
        mRepository.update(todoItem);
    }

    public void delete(TodoItem todoItem) {
        mRepository.delete(todoItem);
    }

    public void deleteAllTodos() {
        mRepository.deleteAllTodos();
    }

    public LiveData<List<TodoItem>> getCompletedTodoItemsList() {
        return mCompletedTodoItemsList;
    }
}
