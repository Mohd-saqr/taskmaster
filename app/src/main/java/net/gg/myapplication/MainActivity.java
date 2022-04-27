package net.gg.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
         setSupportActionBar();
        getShearedPreference();


    }

    @Override
    protected void onResume() {
        getShearedPreference();
        super.onResume();
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

        /// task btn
        Button task1Btn = findViewById(R.id.task1_btn);
        Button task2Btn = findViewById(R.id.task2_btn);
        Button task3Btn = findViewById(R.id.task3_btn);
        task1Btn.setOnClickListener(v -> {
            TransferTaskName(task1Btn.getText().toString());
        });
        task2Btn.setOnClickListener(v -> {
            TransferTaskName(task2Btn.getText().toString());
        });
        task3Btn.setOnClickListener(v -> {
            TransferTaskName(task3Btn.getText().toString());
        });


    }
    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_menu:
                Intent intent = new Intent(getApplicationContext(),SettingsPage.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    void getShearedPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = findViewById(R.id.text_view_username);
        TextView textView1 = findViewById(R.id.text_view_username2);
        TextView textView2 = findViewById(R.id.text_view_username3);
        textView.setText(sharedPreferences.getString("userName","username"));
        textView1.setText((sharedPreferences.getString("userName","username")));
        textView2.setText((sharedPreferences.getString("userName","username")));
    }

    void TransferTaskName(String taskName){
        Intent intent = new Intent(getApplicationContext(),TaskDetailsPage.class);
        intent.putExtra("taskName",taskName);
        startActivity(intent);
    }
}
