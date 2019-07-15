package com.smartmanager.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smartmanager.R;
import com.smartmanager.adapter.TodoItemAdapter;
import com.smartmanager.model.TodoItem;
import com.smartmanager.viewmodel.TodoItemViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TODO_REQUEST = 1;
    public static final int EDIT_TODO_REQUEST = 2;

/*  private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.smartmanager.ui.ACTION_UPDATE_NOTIFICATION";
    private NotificationManager mNotifyManager;
    */

    private TodoItemViewModel mTodoItemViewModel;
    final TodoItemAdapter adapter = new TodoItemAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTodoItemViewModel = ViewModelProviders.of(this).get(TodoItemViewModel.class);
        mTodoItemViewModel.getTodoItemsList()
                .observe(this, new Observer<List<TodoItem>>() {
                    @Override
                    public void onChanged(@Nullable List<TodoItem> todoItems) {
                        // Update RecyclerView
                        adapter.submitList(todoItems);
                    }
                });

        FloatingActionButton buttonAddTodo = findViewById(R.id.button_add_todo);
        buttonAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(MainActivity.this, AddEditTodoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQUEST);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        TodoItem todoItem = adapter.getTodoItemAt(position);
                        if (direction == ItemTouchHelper.LEFT) {
                            mTodoItemViewModel.delete(todoItem);
                        } else {
                            todoItem.setCompleted(true);
                            adapter.notifyItemChanged(position);
                            mTodoItemViewModel.update(todoItem);
                        }
                    }

                }
        );

        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TodoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TodoItem todoItem) {
                Intent intent =
                        new Intent(MainActivity.this, AddEditTodoActivity.class);
                intent.putExtra(AddEditTodoActivity.EXTRA_ID, todoItem.getId());
                intent.putExtra(AddEditTodoActivity.EXTRA_TITLE, todoItem.getTitle());
                intent.putExtra(AddEditTodoActivity.EXTRA_DESCRIPTION, todoItem.getDescription());
                intent.putExtra(AddEditTodoActivity.EXTRA_PRIORITY, todoItem.getPriority());
                intent.putExtra(AddEditTodoActivity.EXTRA_CATEGORY, todoItem.getCategory());
                intent.putExtra(AddEditTodoActivity.EXTRA_HAS_REMINDER, todoItem.hasReminder());
                intent.putExtra(AddEditTodoActivity.EXTRA_REMINDER_DATE,
                        todoItem.getReminderDate());
                startActivityForResult(intent, EDIT_TODO_REQUEST);
            }
        });

    }

    //region Menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_tasks:
                mTodoItemViewModel.deleteAllTodos();
                displayToast("All tasks deleted");
                return true;
            case R.id.start_timer:
                launchTimerActivity();
                return true;
            case R.id.stop_timer:
                launchTimerActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TODO_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTodoActivity.EXTRA_PRIORITY,
                    R.integer.priority_default_value);
            String category = data.getStringExtra(AddEditTodoActivity.EXTRA_CATEGORY);
            boolean hasReminder =
                    data.getBooleanExtra(AddEditTodoActivity.EXTRA_HAS_REMINDER, false);
            Date date = (Date) data.getSerializableExtra(AddEditTodoActivity.EXTRA_REMINDER_DATE);

            TodoItem todoItem =
                    new TodoItem(title, description, priority, category, hasReminder, date);

            if (todoItem.hasReminder() && todoItem.getReminderDate() != null) {
                // TODO: Create pending intent
                /*
                ReminderDate must be converted milliseconds beforehand.
                Set time with AlarmService e.g:
                    delay = ReminderDate (convert to milliseconds) - Now
                    showNotificationInMinutes = delay
                */
            }

            mTodoItemViewModel.insert(todoItem);
            displayToast("Item saved");
        } else if (requestCode == EDIT_TODO_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTodoActivity.EXTRA_ID, R.integer.no_value_id);
            if (id == R.integer.no_value_id) {
                displayToast("Item can't be updated");
                return;
            }
            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTodoActivity.EXTRA_PRIORITY,
                    R.integer.priority_default_value);
            String category = data.getStringExtra(AddEditTodoActivity.EXTRA_CATEGORY);
            boolean hasReminder =
                    data.getBooleanExtra(AddEditTodoActivity.EXTRA_HAS_REMINDER, false);
            Date date = (Date) data.getSerializableExtra(AddEditTodoActivity.EXTRA_REMINDER_DATE);
            TodoItem todoItem =
                    new TodoItem(title, description, priority, category, hasReminder, date);
            todoItem.setId(id);

            mTodoItemViewModel.update(todoItem);
            displayToast("Item updated");
        } else {
            displayToast("Item wasn't saved");
        }
    }

    private void launchTimerActivity() {
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }

    /**
     * Displays the actual message in a toast message.
     *
     * @param message Message to display.
     */
    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
