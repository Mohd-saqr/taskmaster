package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.google.android.material.snackbar.Snackbar;

import net.gg.myapplication.R;

public class SignUp extends AppCompatActivity {

    ProgressBar progressBar ;
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        progressBar =(ProgressBar) findViewById(R.id.s_progress_bar);
        intent =new Intent(getApplicationContext(),ConfirmActivity.class);
        functionalityForm();
    }

    private void functionalityForm() {
        EditText userName =findViewById(R.id.s_edit_user_name);
        EditText email =findViewById(R.id.s_edit_text_email);
        EditText password =findViewById(R.id.s_edit_text_password);
        EditText reTypePassword =findViewById(R.id.edit_text_re_type_password);
        TextView goToLoginActivity =findViewById(R.id.s_text_view_dont_account);
        goToLoginActivity.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
        Button signUp =findViewById(R.id.btn_sign_up);
        signUp.setOnClickListener(v->{
            if (validation(userName,email,password,reTypePassword)){
                progressBar.setVisibility(View.VISIBLE);
             signUpFun(userName,email,password);

            }
        });



    }

    private void signUpFun(EditText userName, EditText email, EditText password) {
        AuthSignUpOptions Option = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email.getText().toString())
                .userAttribute(AuthUserAttributeKey.preferredUsername(),userName.getText().toString())
                .build();

        Amplify.Auth.signUp(email.getText().toString(), password.getText().toString(), Option,
                result -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    intent.putExtra("email",email.getText().toString());
                    saveUserName(userName.getText().toString());
                    startActivity(intent);


                },
                error -> {

                    Snackbar snackbar =  Snackbar.
                            make(findViewById(R.id.sign_up_activity),"Email is used Please try Another Email",Snackbar.LENGTH_LONG);
                            snackbar.show();

                    progressBar.setVisibility(View.INVISIBLE);
                }
        );
    }

    private void saveUserName(String username) {
        editor.putString("userName",username);
        editor.apply();
    }

    private boolean validation(EditText userName, EditText email, EditText password, EditText reTypePassword) {
        if (userName.length()==0||password.length()==0){
            userName.setError("This filed cannot be Empty");
            password.setError("This filed cannot be Empty");
            return false;
        }
        if (password.length()<7){
            password.setError("Please Enter at least 8 character");
            return false;
        }
        if (!email.getText().toString().matches("^(.+)@(.+)$")){
            email.setError("Please Enter Valid Email");
            return false;
        }
        if (!reTypePassword.getText().toString().equals(password.getText().toString())){
            reTypePassword.setError("Password didn't match");
            return false;
        }
        return true;
    }
}