package com.smartmanager.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.smartmanager.dao.TodoItemDao;
import com.smartmanager.dao.TodoItemRoomDatabase;
import com.smartmanager.model.TodoItem;

import java.util.List;


/**
 * A communication bridge between the data and the model levels.
 * Repository is responsible for creating an additional
 * level of abstraction of querying the database source.
 */
public class TodoItemRepository {

    private TodoItemDao mTodoItemDao;

    private LiveData<List<TodoItem>> mTodoItemsList;
    private LiveData<List<TodoItem>> mCompletedTodoItemsList;

    public TodoItemRepository(Application application) {
        TodoItemRoomDatabase database = TodoItemRoomDatabase.getDatabase(application);
        mTodoItemDao = database.todoItemDao();
        mTodoItemsList = mTodoItemDao.getAllTodoItems();
        mCompletedTodoItemsList = mTodoItemDao.getCompletedTodoItems();
    }

    public LiveData<List<TodoItem>> getTodoItemsList() {
        return mTodoItemsList;
    }

    public void insert(TodoItem todoItem) {
        new InsertItemAsyncTask(mTodoItemDao).execute(todoItem);
    }

    public void update(TodoItem todoItem) {
        new UpdateItemAsyncTask(mTodoItemDao).execute(todoItem);
    }

    public void delete(TodoItem todoItem) {
        new DeleteItemAsyncTask(mTodoItemDao).execute(todoItem);
    }

    public void deleteAllTodos() {
        new DeleteAllItemsAsyncTask(mTodoItemDao).execute();
    }

    public LiveData<List<TodoItem>> getCompletedTodoItemsList() {
        return mCompletedTodoItemsList;
    }

    //region Async tasks
    private static class InsertItemAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private TodoItemDao mTodoItemDao;

        private InsertItemAsyncTask(TodoItemDao todoItemDao) {
            this.mTodoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            mTodoItemDao.insert(todoItems[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private TodoItemDao mTodoItemDao;

        private UpdateItemAsyncTask(TodoItemDao todoItemDao) {
            this.mTodoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            mTodoItemDao.update(todoItems[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private TodoItemDao mTodoItemDao;

        private DeleteItemAsyncTask(TodoItemDao todoItemDao) {
            this.mTodoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            mTodoItemDao.delete(todoItems[0]);
            return null;
        }
    }

    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoItemDao mTodoItemDao;

        private DeleteAllItemsAsyncTask(TodoItemDao todoItemDao) {
            this.mTodoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTodoItemDao.deleteAllTodos();
            return null;
        }
    }
    //endregion

}
