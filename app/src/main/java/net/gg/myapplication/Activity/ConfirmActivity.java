package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.amplifyframework.core.Amplify;
import com.google.android.material.snackbar.Snackbar;

import net.gg.myapplication.R;



public class ConfirmActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();
        String email=intent.getStringExtra("email");
        Button confirm =findViewById(R.id.btn_confirm_con);
        confirm.setOnClickListener(v -> {

            confirm(email);

        });
    }

    private void confirm(String email) {

        EditText code1 =findViewById(R.id.edit_text_codeee);
            if (code1.getText().length()<5){
                code1.setError("Please Enter code");

            }else{
                Amplify.Auth.confirmSignUp(
                        email,
                        code1.getText().toString(),
                        result -> {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            finish();
                        },err->{
                            Snackbar snackbar =  Snackbar.
                                    make(findViewById(R.id.confirm_layout),"Incorrect code",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                );
            }



    }
}