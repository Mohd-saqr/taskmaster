package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
         setSupportActionBar();



    }



    void setSupportActionBar(){
        // addToolbar
        Toolbar toolbar =findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle("Task Master");
    }

    void FunctionalityForBtn(){
        // start activity add task
        Button mAddTaskBtn =findViewById(R.id.addTaskBtn);
        mAddTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),AddTask.class);
            startActivity(intent);
        });
        // start activity All task
        Button mAllTaskBtn =findViewById(R.id.allTaskBtn);
        mAllTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),AllTasks.class);
            startActivity(intent);
        });
        // add
    }

 }
