package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        FunctionalityForBtn();
        setSupportActionBar();
    }

    void FunctionalityForBtn() {
        // add toast to btn

        Button addTask = findViewById(R.id.btn_submit);
        addTask.setOnClickListener(v -> {
            Toast.makeText(this, "submitted!", Toast.LENGTH_SHORT).show();
        });
    }

    void setSupportActionBar(){
        // addToolbar
        Toolbar toolbar =findViewById(R.id.toolbar_add_task);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(this.getLocalClassName());

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}