package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import net.gg.myapplication.MyModule.Task;
import net.gg.myapplication.R;
import net.gg.myapplication.RecyclerView.MyAdapterForRecyclerView;
import net.gg.myapplication.db.AppDb;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Task>tasks= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
         setSupportActionBar();
        getShearedPreference();





    }

    private void runRecyclerView() {
        tasks  = AppDb.getInstance(this).doaTask().getAll();
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        MyAdapterForRecyclerView myAdapterForRecyclerView = new MyAdapterForRecyclerView(tasks, task ->
                TransferTaskName(task.getId()),task -> {
            /// delete icon
            AppDb.getInstance(this).doaTask().DeleteTask(task);
            onResume();
        },task -> {
            Intent intent = new Intent(getApplicationContext(),AddTask.class);
            intent.putExtra("taskId",task.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(myAdapterForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // no task text
        TextView NoTaskText=findViewById(R.id.text_view_no_task);
        if(tasks.size()==0){
            NoTaskText.setText("No Task Yet ... ! ");
            NoTaskText.setVisibility(View.VISIBLE);
        }else {
            NoTaskText.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    protected void onResume() {
        getShearedPreference();
        runRecyclerView();
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
        TextView textView = findViewById(R.id.text_view_task_by);
//        TextView textView1 = findViewById(R.id.text_view_username2);
//        TextView textView2 = findViewById(R.id.text_view_username3);
        textView.setText(sharedPreferences.getString("userName","Anonymous"));
//        textView1.setText((sharedPreferences.getString("userName","username")));
//        textView2.setText((sharedPreferences.getString("userName","username")));
    }

    void TransferTaskName(Long id  ){
        Intent intent = new Intent(getApplicationContext(),TaskDetailsPage.class);
        intent.putExtra("taskId",id);
        startActivity(intent);
    }


}
