package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.R;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsPage extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ;
    String teamId = "";
    LoadingProgress progress = new LoadingProgress(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        setSupportActionBar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        FunctionalityForBtn();
        execution();

    }

    void setSupportActionBar() {
        // addToolbar
        Toolbar toolbar = findViewById(R.id.toolbar_setting_page);
        setSupportActionBar(toolbar);
        // addTitle to tool bar
        this.setTitle(this.getLocalClassName());
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    void FunctionalityForBtn() {
        // add username to SharedPreferences
        Button buttonSubmit = findViewById(R.id.submit_btn_your_name);
        TextView textView = findViewById(R.id.edit_text_enter_your_name);
        if (sharedPreferences.contains("userName"))
            textView.setText(sharedPreferences.getString("userName", ""));
        buttonSubmit.setOnClickListener(v -> {

            if (textView.getText().length() < 4) {
                textView.setError("Min length 4 required");
            } else {
                /**
                 *    save username
                  */

//                String username = textView.getText().toString();
//                editor.putString("userName", username);
//                editor.apply();

                /**
                 * alert  changed complete
                 */
                Message();
            }

        });

    }

    private void execution() {


        Spinner teamName = findViewById(R.id.spinner_team_tasks);

        teamName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Amplify.API.query(
                        ModelQuery.list(Team.class, Team.NAME.eq(teamName.getSelectedItem().toString())),
                        response -> {
                            for (Team todo : response.getData()) {

                                editor.putString("teamId", todo.getId());
                                editor.putString("teamName", teamName.getSelectedItem().toString());
                                editor.apply();

                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Query failure", error)
                );


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void Message() {
        new AlertDialog.Builder(this).
                setMessage("Success")
                .setTitle("Change Team Success")
                .setPositiveButton("Ok", (dialog, witch) -> {
                    startActivity(new Intent(SettingsPage.this, MainActivity.class));
                    finish();
                }).show();

    }


}
