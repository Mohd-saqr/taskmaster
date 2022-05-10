package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import net.gg.myapplication.R;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        setSupportActionBar();
    }

    void setSupportActionBar() {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_all_task);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(this.getLocalClassName());

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}