package net.gg.myapplication;

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
import android.widget.Toast;

import net.gg.myapplication.MyModule.Task;
import net.gg.myapplication.RecyclerView.MyAdapterForRecyclerView;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Task>tasks = Arrays.asList(new Task("First task","this is first task", Task.StateEnum.ASSIGNED),
            new Task("second task","Nam pharetra turpis eu diam pharetra commodo.", Task.StateEnum.COMPLETE),
                        new Task("third task","Nam pharetra turpis eu diam pharetra commodo.", Task.StateEnum.IN_PROGRESS),
            new Task("forth task","Vestibulum commodo mi at semper viverra.", Task.StateEnum.ASSIGNED),
            new Task("five task","Praesent lacinia ante tincidunt facilisis condimentum.", Task.StateEnum.IN_PROGRESS),
            new Task("random task","Praesent lacinia ante tincidunt facilisis condimentum.", Task.StateEnum.COMPLETE),
            new Task("random task","Vivamus bibendum neque finibus eros malesuada, ut finibus risus luctus.", Task.StateEnum.IN_PROGRESS),
            new Task("random task","Vestibulum aliquet nibh eu neque rutrum, ut fringilla odio fermentum..", Task.StateEnum.COMPLETE),
            new Task("random task","Cras nec neque ac nisi ultrices convallis eget in turpis..", Task.StateEnum.ASSIGNED),
            new Task("random task","In cursus justo eget scelerisque tempor..", Task.StateEnum.ASSIGNED),
            new Task("random task","Vivamus bibendum neque finibus eros malesuada, ut finibus risus luctus.", Task.StateEnum.IN_PROGRESS),
            new Task("random task","Vivamus bibendum neque finibus eros malesuada, ut finibus risus luctus.", Task.StateEnum.IN_PROGRESS)
            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
         setSupportActionBar();
        getShearedPreference();
        runRecyclerView();


    }

    private void runRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        MyAdapterForRecyclerView myAdapterForRecyclerView = new MyAdapterForRecyclerView(tasks, new MyAdapterForRecyclerView.itemClickL() {
            @Override
            public void OnItemClick(Task task) {
                TransferTaskName(task.getTitle(),task.getBody());
            }
        });
        recyclerView.setAdapter(myAdapterForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
//        Button task1Btn = findViewById(R.id.task1_btn);
//        Button task2Btn = findViewById(R.id.task2_btn);
//        Button task3Btn = findViewById(R.id.task3_btn);
//        task1Btn.setOnClickListener(v -> {
//            TransferTaskName(task1Btn.getText().toString());
//        });
//        task2Btn.setOnClickListener(v -> {
//            TransferTaskName(task2Btn.getText().toString());
//        });
//        task3Btn.setOnClickListener(v -> {
//            TransferTaskName(task3Btn.getText().toString());
//        });


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
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        TextView textView = findViewById(R.id.text_view_taskBy);
//        TextView textView1 = findViewById(R.id.text_view_username2);
//        TextView textView2 = findViewById(R.id.text_view_username3);
//        textView.setText(sharedPreferences.getString("userName","username"));
//        textView1.setText((sharedPreferences.getString("userName","username")));
//        textView2.setText((sharedPreferences.getString("userName","username")));
    }

    void TransferTaskName(String taskName ,String taskBody){
        Intent intent = new Intent(getApplicationContext(),TaskDetailsPage.class);
        intent.putExtra("taskName",taskName);
        intent.putExtra("TaskBody",taskBody);
        startActivity(intent);
    }
}
