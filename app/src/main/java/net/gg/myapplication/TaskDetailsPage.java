package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
        setSupportActionBar();
        TextView taskDetails = findViewById(R.id.text_view_task_body);
        taskDetails.setText(getBody());
    }


    void setSupportActionBar() {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_task_details_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(getTaskName());
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    //// get task name from main activity and body
    String getTaskName() {
        Intent intent = getIntent();
        return intent.getStringExtra("taskName");
    }
    /// get the task body
    String getBody() {
        Intent intent = getIntent();
        return intent.getStringExtra("TaskBody");
    }
}