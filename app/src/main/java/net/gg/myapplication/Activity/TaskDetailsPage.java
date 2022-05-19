package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amplifyframework.api.aws.GsonVariablesSerializer;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.R;

import java.util.Collections;
import java.util.regex.Matcher;

public class TaskDetailsPage extends AppCompatActivity {

    LoadingProgress loadingProgress = new LoadingProgress(TaskDetailsPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
        Fetch();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    void setSupportActionBar(String s) {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_task_details_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(s);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }


    Task t;

    private void Fetch() {
        loadingProgress.startLoading();
        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");

        Amplify.API.query(
                ModelQuery.get(Task.class, taskId),
                response -> {
                    t = response.getData();
                    runOnUiThread(() -> {
                        TextView taskDetails = findViewById(R.id.text_view_task_body);
                        TextView taskStats = findViewById(R.id.text_view_task_stats);
                        taskDetails.setText(t.getBody());
                        taskStats.setText("Stats : " + t.getState());
                        setSupportActionBar(t.getTitle());
                        loadingProgress.stopLoading();
                    });
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );
    }


}