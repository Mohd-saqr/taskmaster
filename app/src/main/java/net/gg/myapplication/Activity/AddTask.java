package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.gg.myapplication.MyModule.Task;

import net.gg.myapplication.R;
import net.gg.myapplication.db.AppDb;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        FunctionalityForBtn();
        setSupportActionBar("Add Task");


    }

    @Override
    protected void onResume() {
        /// set total task number
        TextView TotalTask = findViewById(R.id.text_view_total_task);
        TotalTask.setText("Total Task : " + String.valueOf(AppDb.getInstance(this).doaTask().getAll().size()));
        super.onResume();
    }

    void FunctionalityForBtn() {
        // add functionality for spinner

        Spinner spinner = findViewById(R.id.spiner_task_stats);
        ArrayAdapter<CharSequence> stats = ArrayAdapter.createFromResource(this, R.array.task_stats, R.layout.spinner_row);
        spinner.setAdapter(stats);

        // add task button
        Button addTask = findViewById(R.id.btn_submit_add_task);
        // task name input
        EditText taskTitleField = findViewById(R.id.Edit_text_task_name);
        // task description input
        EditText taskDescriptionInput = findViewById(R.id.Edit_text_task_description);
        addTask.setOnClickListener(v -> {
            if (taskTitleField.getText().length() < 3)
                taskTitleField.setError("Minimum 4 characters");
            else if (taskDescriptionInput.getText().length() < 5)
                taskDescriptionInput.setError("Minimum 6 characters");
            else {
                Task task = new Task(taskTitleField.getText().toString(), taskDescriptionInput.getText().toString(), spinner.getSelectedItem().toString());
                AppDb.getInstance(this).doaTask().AddTask(task);
                finish();
            }
        });

        Button saveButton =findViewById(R.id.btn_save_edit_task);
        Intent intent=getIntent();
        Task taskFromDb=AppDb.getInstance(this).doaTask().getTask(intent.getLongExtra("taskId",0));
        if (intent.hasExtra("taskId")){
            /// set text dor input
            taskTitleField.setText(taskFromDb.getTitle());
            taskDescriptionInput.setText(taskFromDb.getBody());
            /// set visibility for button
            saveButton.setVisibility(View.VISIBLE);
            addTask.setVisibility(View.INVISIBLE);
            TextView title =findViewById(R.id.text_view_add_task);
            title.setText("Edit Task");
            setSupportActionBar("Edit Task");
           // set on click for save
            saveButton.setOnClickListener(v -> {
                taskFromDb.setTitle(taskTitleField.getText().toString());
                taskFromDb.setBody(taskDescriptionInput.getText().toString());
                taskFromDb.setState(spinner.getSelectedItem().toString());

                AppDb.getInstance(this).doaTask().UpdateTask( taskFromDb);
                saveButton.setVisibility(View.INVISIBLE);
                addTask.setVisibility(View.VISIBLE);
                finish();
            });
        }

    }

    void setSupportActionBar(String title) {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_task);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}