package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.hub.HubChannel;

import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.MyModule.Task;

import net.gg.myapplication.R;
import net.gg.myapplication.db.AppDb;

public class AddTask extends AppCompatActivity {
    LoadingProgress progress = new LoadingProgress(AddTask.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        FunctionalityForBtn();
        setSupportActionBar("Add Task");
        getTeamId();


    }
    String TeamId = "";
    private void getTeamId() {
       SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this);
       TeamId =sharedPreferences.getString("teamId","Noteam");

    }


    @Override
    protected void onResume() {
        /// set total task number
        TextView TotalTask = findViewById(R.id.text_view_total_task);
        Intent intent=getIntent();
        TotalTask.setText("Total Task : " + getIntent().getStringExtra("totalTask"));
        super.onResume();
    }

    public void FunctionalityForBtn() {
        // add functionality for spinner

        Spinner spinner = findViewById(R.id.spiner_task_stats);
        ArrayAdapter<CharSequence> stats = ArrayAdapter.createFromResource(this, R.array.task_stats, R.layout.spinner_row);
        spinner.setAdapter(stats);

        // add task button
        Button addTask = findViewById(R.id.btn_submit_add_task);
        // save button
        Button saveButton = findViewById(R.id.btn_save_edit_task);
        // task name input
        EditText taskTitleField = findViewById(R.id.Edit_text_task_name);
        // task description input
        EditText taskDescriptionInput = findViewById(R.id.Edit_text_task_description);
        addTaskOnAWS(taskTitleField, taskDescriptionInput, spinner, addTask);
        updateTask(taskTitleField, taskDescriptionInput, spinner, saveButton);
        /**
         * this method for previous lab
         */
//        // save using room data base
//        addTaskRoom(spinner, taskTitleField, taskDescriptionInput, addTask);
//        /////  set edit page
//        editTaskRoom(spinner, addTask, taskTitleField, taskDescriptionInput, saveButton);

    }

    /**
     * this method for save on aws
     *
     * @param taskTitleField
     * @param taskDescriptionInput
     * @param spinner
     */

    private void updateTask(EditText taskTitleField, EditText taskDescriptionInput, Spinner spinner, Button save) {

        save.setOnClickListener(v -> {
            if (taskTitleField.getText().length() < 3)
                taskTitleField.setError("Minimum 4 characters");
            else if (taskDescriptionInput.getText().length() < 5)
                taskDescriptionInput.setError("Minimum 6 characters");
            else {
                progress.startLoading();
                com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder().
                        title(taskTitleField.getText().toString())
                        .state(spinner.getSelectedItem().toString()).
                        body(taskDescriptionInput.getText().toString())
                        .build();
                progress.stopLoading();
                Amplify.API.mutate(
                        ModelMutation.update(task),
                        success ->{

                            finish();
                        },
                        error -> Log.e("TAG", "Could not save item to API", error)
                );
            }
        });
    }

    /**
     * this method for save on aws
     *
     * @param taskTitleField
     * @param taskDescriptionInput
     * @param spinner
     */

    private void addTaskOnAWS(EditText taskTitleField, EditText taskDescriptionInput, Spinner spinner, Button addTask) {


        addTask.setOnClickListener(v -> {
            if (taskTitleField.getText().length() < 3)
                taskTitleField.setError("Minimum 4 characters");
            else if (taskDescriptionInput.getText().length() < 5)
                taskDescriptionInput.setError("Minimum 6 characters");
            else {

                progress.startLoading();
                com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder().
                        title(taskTitleField.getText().toString())
                        .state(spinner.getSelectedItem().toString()).
                        body(taskDescriptionInput.getText().toString())
                        .teamTasksId(TeamId)
                        .build();
                // add task to data storage aws
                AddDataStorageAws(task);




            }

        });


    }



    private void AddDataStorageAws(com.amplifyframework.datastore.generated.model.Task task) {
        Amplify.API.query(ModelMutation.create(task),success->{
            progress.stopLoading();
            finish();
        },error->{

        });
    }

    private void editTaskRoom(Spinner spinner, Button addTask, EditText taskTitleField, EditText taskDescriptionInput, Button saveButton) {
        Intent intent = getIntent();
        Task taskFromDb = AppDb.getInstance(this).doaTask().getTask(intent.getLongExtra("taskId", 0));
        if (intent.hasExtra("taskId")) {
            /// set text dor input
            taskTitleField.setText(taskFromDb.getTitle());
            taskDescriptionInput.setText(taskFromDb.getBody());
            /// set visibility for button
            saveButton.setVisibility(View.VISIBLE);
            addTask.setVisibility(View.INVISIBLE);
            TextView title = findViewById(R.id.text_view_add_task);
            title.setText("Edit Task");
            setSupportActionBar("Edit Task");
            // set on click for save
            saveButton.setOnClickListener(v -> {
                taskFromDb.setTitle(taskTitleField.getText().toString());
                taskFromDb.setBody(taskDescriptionInput.getText().toString());
                taskFromDb.setState(spinner.getSelectedItem().toString());

                AppDb.getInstance(this).doaTask().UpdateTask(taskFromDb);
                saveButton.setVisibility(View.INVISIBLE);
                addTask.setVisibility(View.VISIBLE);
                finish();

            });


        }


    }

    private void addTaskRoom(Spinner spinner, EditText taskTitleField, EditText taskDescriptionInput, Button addTsk) {

        addTsk.setOnClickListener(v -> {
            if (taskTitleField.getText().length() < 3)
                taskTitleField.setError("Minimum 4 characters");
            else if (taskDescriptionInput.getText().length() < 5)
                taskDescriptionInput.setError("Minimum 6 characters");
            else {
                Task task = new Task(taskTitleField.getText().toString(), taskDescriptionInput.getText().toString(), spinner.getSelectedItem().toString());
                AppDb.getInstance(this).doaTask().AddTask(task);
                finish();
            }
        });

    }

    void setSupportActionBar(String title) {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_task);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });


    }



}