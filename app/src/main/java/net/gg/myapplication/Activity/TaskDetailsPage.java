package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import net.gg.myapplication.MyModule.Task;
import net.gg.myapplication.R;
import net.gg.myapplication.db.AppDb;

public class TaskDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
        setSupportActionBar();
        TextView taskDetails = findViewById(R.id.text_view_task_body);
        TextView taskStats=findViewById(R.id.text_view_task_stats);
        taskDetails.setText(getTaskDetails().getBody());
        taskStats.setText("Stats : "+ getTaskDetails().getState());

    }


    void setSupportActionBar() {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_task_details_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(getTaskDetails().getTitle());
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    //// get task name from main activity and body
    Task getTaskDetails() {
        Intent intent = getIntent();
        Long taskId=intent.getLongExtra("taskId",0);
        return AppDb.getInstance(this).doaTask().getTask(taskId);
    }

}