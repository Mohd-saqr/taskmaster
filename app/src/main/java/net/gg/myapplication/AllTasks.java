package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        Button addTask = findViewById(R.id.addTaskBtn);
        addTask.setOnClickListener(v -> {
            Toast.makeText(this, "submitted!", Toast.LENGTH_SHORT).show();
        });
    }
}