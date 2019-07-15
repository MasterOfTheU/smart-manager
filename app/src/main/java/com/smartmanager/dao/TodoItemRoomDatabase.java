package com.smartmanager.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.smartmanager.model.TodoItem;
import com.smartmanager.util.Converters;

import java.util.Date;


@Database(entities = {TodoItem.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TodoItemRoomDatabase extends RoomDatabase {

    /**
     * The Room library is responsible for creation a subclass for DAO object
     * with the databaseBuilder method and generates the entity of interface.
     */
    public abstract TodoItemDao todoItemDao();

    private static TodoItemRoomDatabase INSTANCE;

    /**
     * Creates a database using Singleton pattern that helps to avoid
     * creation of database instances on each database query.
     *
     * @param context Application context
     * @return Room database instance
     */
    public static TodoItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoItemRoomDatabase.class, "todoitems_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback for database creation and opening.
     */
    private static Callback sRoomDatabaseCallback =
            new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsyncTask(INSTANCE).execute();
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

    /**
     * Populates the database when it is created for the first time.
     */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoItemDao mTodoItemDao;

        private PopulateDbAsyncTask(TodoItemRoomDatabase db) {
            mTodoItemDao = db.todoItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTodoItemDao.insert(new TodoItem(
                    "Title 1", "Description Example 1",
                    1, "Work", false, new Date()));
            mTodoItemDao.insert(new TodoItem(
                    "Title 2", "Description Example 2",
                    2, "Health", false, new Date()));
            mTodoItemDao.insert(new TodoItem(
                    "Title 3", "Description Example 3",
                    3, "Personal", false, new Date()));
            mTodoItemDao.insert(new TodoItem(
                    "Title 4", "Description Example 4",
                    2, "Personal", false, new Date()));
            mTodoItemDao.insert(new TodoItem(
                    "Title 5", "Description Example 5",
                    3, "Home", false, new Date()));
            return null;
        }
    }

}
