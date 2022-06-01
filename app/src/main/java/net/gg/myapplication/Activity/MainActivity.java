package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.hub.HubChannel;


import net.gg.myapplication.Helper.Dialog;
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
    MyAdapterForRecyclerView myAdapterForRecyclerView;
    static List<com.amplifyframework.datastore.generated.model.Task> tasksArray = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
        setSupportActionBar();
        getShearedPreference();
        runRecyclerView();
        startActivityIntent();


    }


    private void runRecyclerView() {
        // this line for room db
//        tasksArray = AppDb.getInstance(this).doaTask().getAll();
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        myAdapterForRecyclerView = new MyAdapterForRecyclerView(task -> {
            //on item click
            TransferTaskName(task.getId());

        },tasksArray,((popupMenu, task) -> {
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    //edit
                    case R.id.eit_task:
                    {
                        Intent intent = new Intent(getApplicationContext(), AddTask.class);
                        intent.putExtra("taskId", task.getId());
                        startActivity(intent);
                    }break;

                    case R.id.delete_task:
                    {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Delete Task ")
                                .setMessage("Are you sure to delete " + task.getTitle())
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    /// delete icon
                                    Amplify.API.mutate(ModelMutation.delete(task), rs -> {
                                                System.out.println("done");
                                                runOnUiThread(() -> {
                                                    fetchData();
                                                });
                                            }
                                            , err -> {
                                            });
                                }).setNegativeButton("Cancel", (dialog, which) -> {
                                    dialog.cancel();
                                }).setIcon(R.drawable.ic_warning)
                                .show();
                    }
                    break;
                }
                return false;
            });
        }));
        recyclerView.setAdapter(myAdapterForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShearedPreference();
        fetchData();


    }

    private void fetchData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamId = preferences.getString("teamId", "Noteam");

        Amplify.API.query(

                ModelQuery.list(Task.class, Task.TEAM_TASKS_ID.eq(teamId)),
                response -> {
                    tasksArray.clear();
                    if (response.hasData()) {
                        for (Task Task : response.getData()) tasksArray.add(Task);

                    }
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


                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

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
            checkTeamName();

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
                finish();
                return true;
            case R.id.Logout_menu:
            {
                Amplify.Auth.signOut(
                        () -> {
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    void getShearedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = findViewById(R.id.text_view_task_by);
        TextView teamName = findViewById(R.id.text_view_team);
        // set the team name on main activity
        teamName.setText(sharedPreferences.getString("teamName", "No team"));
//        TextView textView2 = findViewById(R.id.text_view_username3);
        textView.setText(sharedPreferences.getString("userName", "Anonymous"));
//        textView1.setText((sharedPreferences.getString("userName","username")));
//        textView2.setText((sharedPreferences.getString("userName","username")));
    }

    // using intent pass the task id to display task page
    void TransferTaskName(String id) {
        Intent intent = new Intent(getApplicationContext(), TaskDetailsPage.class);
        intent.putExtra("taskId", id);
        startActivity(intent);
    }



    /// before add task check if the user choose him team
    private void checkTeamName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.contains("teamId")) {
            Intent intent = new Intent(getApplicationContext(), SettingsPage.class);

            new AlertDialog.Builder(this)
                    .setTitle("Select your team")
                    .setMessage("please select your team first")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        startActivity(intent);


                    }).setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();

                    }).show();


        } else {
            Intent intent = new Intent(getApplicationContext(), AddTask.class);
            intent.putExtra("totalTask", String.valueOf(tasksArray.size()));

            startActivity(intent);
        }

    }

    private void startActivityIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

    }






}







