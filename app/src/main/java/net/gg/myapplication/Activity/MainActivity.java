package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.hub.HubChannel;


import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.R;
import net.gg.myapplication.RecyclerView.MyAdapterForRecyclerView;
import net.gg.myapplication.db.AppDb;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    LoadingProgress progress = new LoadingProgress(MainActivity.this);
    Handler handler;
    MyAdapterForRecyclerView myAdapterForRecyclerView;
    static List<com.amplifyframework.datastore.generated.model.Task> tasksArray = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
        configureAwsAmplify();
        setSupportActionBar();
        getShearedPreference();
        runRecyclerView();

    }


    private void runRecyclerView() {
        // this line for room db
//        tasksArray = AppDb.getInstance(this).doaTask().getAll();
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        myAdapterForRecyclerView = new MyAdapterForRecyclerView(tasksArray, task ->
                TransferTaskName(task.getId()), task -> {
            /// delete icon
            Amplify.API.mutate(ModelMutation.delete(task),
                    response -> Log.i("MyAmplifyApp", "Todo with id: "),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );

        }, task -> {
            Intent intent = new Intent(getApplicationContext(), AddTask.class);
            intent.putExtra("taskId", task.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(myAdapterForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShearedPreference();
//        runRecyclerView();
//        progress.startLoading();
        fetchData();

//        progress.startLoading();


    }

    private void fetchData() {
        Amplify.API.query(ModelQuery.list(Task.class), done -> {
            tasksArray.clear();
//            progress.stopLoading();
            for (Task t : done.getData()) tasksArray.add(t);
            runOnUiThread(() -> {
                myAdapterForRecyclerView.notifyDataSetChanged();
                // no task text
                TextView NoTaskText = findViewById(R.id.text_view_no_task);
                if (tasksArray.size() == 0) {
                    NoTaskText.setText("No Task Yet ... ! ");
                    NoTaskText.setVisibility(View.VISIBLE);
                } else {
                    NoTaskText.setVisibility(View.INVISIBLE);

                }

            });

        }, error -> {
        });

    }

    void setSupportActionBar() {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle("Task Master");
    }

    void FunctionalityForBtn() {
        // start activity add task
        Button mAddTaskBtn = findViewById(R.id.addTaskBtn);
        mAddTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddTask.class);
            startActivity(intent);
        });
        // start activity All task
        Button mAllTaskBtn = findViewById(R.id.allTaskBtn);
        mAllTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AllTasks.class);
            startActivity(intent);

        });


    }

    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_menu:
                Intent intent = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    void getShearedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = findViewById(R.id.text_view_task_by);
//        TextView textView1 = findViewById(R.id.text_view_username2);
//        TextView textView2 = findViewById(R.id.text_view_username3);
        textView.setText(sharedPreferences.getString("userName", "Anonymous"));
//        textView1.setText((sharedPreferences.getString("userName","username")));
//        textView2.setText((sharedPreferences.getString("userName","username")));
    }

    void TransferTaskName(String id) {
        Intent intent = new Intent(getApplicationContext(), TaskDetailsPage.class);
        intent.putExtra("taskId", id);
        startActivity(intent);
    }

    void configureAwsAmplify() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());


        } catch (AmplifyException e) {
            Log.e("TAG", "Could not initialize Amplify", e);
        }

    }


}







