package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class TaskDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
        setSupportActionBar();
    }



    void setSupportActionBar(){
        // addToolbar
        Toolbar toolbar =findViewById(R.id.toolbar_task_details_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(getTaskName());
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
    //// get task name from main activity
    String getTaskName(){
        Intent intent = getIntent();
      return   intent.getStringExtra("taskName");
    }
}