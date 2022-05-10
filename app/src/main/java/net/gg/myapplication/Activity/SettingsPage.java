package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import net.gg.myapplication.R;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        setSupportActionBar();
        FunctionalityForBtn();
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
        buttonSubmit.setOnClickListener(v -> {
            TextView textView = findViewById(R.id.edit_text_enter_your_name);
            if (textView.getText().length() < 4) {
                textView.setError("Min length 4 required");
            } else {
                String username = textView.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", username);
                editor.apply();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                finish();
            }

        });

    }
}