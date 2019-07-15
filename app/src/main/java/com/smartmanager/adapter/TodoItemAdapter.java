package com.smartmanager.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartmanager.R;
import com.smartmanager.model.TodoItem;
import com.smartmanager.util.Converters;


/**
 * This adapter is responsible for making a view for each task in the RecyclerView on main screen.
 */
public class TodoItemAdapter extends ListAdapter<TodoItem, TodoItemAdapter.TodoItemViewHolder> {

    private OnItemClickListener mListener;

    public TodoItemAdapter() {
        super(DIFF_CALLBACK);

    }

    /**
     * Callback is used for recyclerview's items indexing and differentiating.
     */
    private static final DiffUtil.ItemCallback<TodoItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TodoItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull TodoItem oldTodoItem,
                                               @NonNull TodoItem newTodoItem) {
                    return oldTodoItem.getId() == newTodoItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull TodoItem oldTodoItem,
                                                  @NonNull TodoItem newTodoItem) {
                    return oldTodoItem.getTitle().equals(newTodoItem.getTitle())
                            && oldTodoItem.getDescription().equals(newTodoItem.getDescription())
                            && oldTodoItem.getPriority() == newTodoItem.getPriority()
                            && oldTodoItem.getCategory().equals(newTodoItem.getCategory());
                }
            };

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder todoItemHolder, int position) {
        TodoItem todoItem = getItem(position);
        todoItemHolder.mTodoItemTitle.setText(todoItem.getTitle());
        todoItemHolder.mTodoItemDescription.setText(todoItem.getDescription());
        todoItemHolder.mTodoItemPriority.setText(String.valueOf(todoItem.getPriority()));
        todoItemHolder.mTodoItemCategory.setText(todoItem.getCategory());

        if (todoItem.hasReminder()) {
            todoItemHolder.mTodoItemReminderIcon.setVisibility(View.VISIBLE);
            todoItemHolder.mTodoItemReminderTextView.setText
                    (Converters.jodaDateTimeToString
                            (Converters.jodaDateTimeToStringArray
                                    (todoItem.getReminderDate().toString())));
        }

        if (todoItem.isCompleted()) {
            todoItemHolder.mCardView.setBackgroundColor(Color.parseColor("#FFF0FFF1"));
            todoItemHolder.mTodoItemTitle.setTextColor
                    (Color.parseColor("#63333333"));
            if (!(todoItem.getDescription().isEmpty())) {
                todoItemHolder.mTodoItemDescription.setTextColor
                        (Color.parseColor("#63333333"));
            }
            todoItemHolder.mTodoItemReminderTextView.setVisibility(View.INVISIBLE);
            todoItemHolder.mTodoItemReminderIcon.setVisibility(View.INVISIBLE);
        }

        // Setting background color for category labels
        String category = (String) todoItemHolder.mTodoItemCategory.getText();
        switch (category) {
            case "Work":
                todoItemHolder.mTodoItemCategory.setBackgroundResource(R.drawable.label_work);
                return;
            case "Home":
                todoItemHolder.mTodoItemCategory.setBackgroundResource(R.drawable.label_home);
                return;
            case "Health":
                todoItemHolder.mTodoItemCategory.setBackgroundResource(R.drawable.label_health);
                return;
            case "Personal":
                todoItemHolder.mTodoItemCategory.setBackgroundResource(R.drawable.label_personal);

        }

    }

    public TodoItem getTodoItemAt(int position) {
        return getItem(position);
    }

    class TodoItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTodoItemTitle;
        private TextView mTodoItemDescription;
        private TextView mTodoItemPriority;
        private TextView mTodoItemCategory;

        private TextView mTodoItemReminderTextView;
        private CardView mCardView;

        private ImageButton mTodoItemReminderIcon;

        TodoItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.cardview_todo);
            mTodoItemTitle = itemView.findViewById(R.id.todo_item_title);
            mTodoItemDescription = itemView.findViewById(R.id.todo_item_description);
            mTodoItemPriority = itemView.findViewById(R.id.todo_item_priority);
            mTodoItemCategory = itemView.findViewById(R.id.todo_item_category);
            mTodoItemReminderIcon = itemView.findViewById(R.id.label_reminder_todoitem);
            mTodoItemReminderTextView = itemView.findViewById(R.id.todo_item_reminder_placeholder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // Position is checked because the animation on deleting the item
                    // may be slow and to avoid crashing we have to check it
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(getItem(position));
                    }
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(TodoItem todoItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
