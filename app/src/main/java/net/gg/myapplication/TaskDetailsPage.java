package net.gg.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class TaskDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
    }

    void setSupportActionBar(){
        // addToolbar
        Toolbar toolbar =findViewById(R.id.toolbar_task_details_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(this.getLocalClassName());
    }
}