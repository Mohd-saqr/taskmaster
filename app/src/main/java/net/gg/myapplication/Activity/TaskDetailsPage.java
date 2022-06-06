package net.gg.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.aws.GsonVariablesSerializer;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.LocationTask;
import com.amplifyframework.datastore.generated.model.Task;

import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.regex.Matcher;

public class TaskDetailsPage extends AppCompatActivity {
    Intent intent;
    String taskId ;
    LoadingProgress loadingProgress = new LoadingProgress(TaskDetailsPage.this);
    File file ;
    LocationTask location;
    Button seeLocation;
    MediaPlayer mediaPlayer = new MediaPlayer();
    TextView taskDetails;
    boolean translated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_page);
        taskDetails = findViewById(R.id.text_view_task_body);
         intent = getIntent();
         taskId = intent.getStringExtra("taskId");
         seeLocation=findViewById(R.id.btn_see_task_location);
         file = new File(getApplicationContext().getFilesDir() + "/"+taskId+".jpg");
        Fetch();

        seeLocation.setOnClickListener(v->{
            if (location==null){
                Toast.makeText(getApplicationContext(), "No Location added", Toast.LENGTH_SHORT).show();
            }else {
                Intent ll = new Intent(getApplicationContext(),MapsActivity.class);
                System.out.println(location+"testrrrr");
                ll.putExtra("latitude_D",location.getLatitude());
                ll.putExtra("longitude_D",location.getLongitude());
                startActivity(ll);
            }

        });

        ImageView hearing =findViewById(R.id.icon_hearing);
        hearing.setOnClickListener(v->{

            Amplify.Predictions.convertTextToSpeech(
                    t.getBody(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp", "Conversion failed", error)

            );
        });

        ImageView translate =findViewById(R.id.icon_Translate);
        translate.setOnClickListener(v->{

            if (!translated){
                Amplify.Predictions.translateText(taskDetails.getText().toString(),
                        result ->{
                    translated=true;
                            runOnUiThread(() -> {
                                taskDetails.setText(result.getTranslatedText());
                            });
                        },
                        error -> Log.e("MyAmplifyApp", "Translation failed", error)
                );
            }else {
                taskDetails.setText(t.getBody());
                translated=false;
            }

        });










    }

    private void playAudio(InputStream data) {

        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mediaPlayer.reset();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setDataSource(new FileInputStream(mp3File).getFD());
            mediaPlayer.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
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

        if (!file.exists()){
            Amplify.Storage.downloadFile(
                    "image"+taskId,
                    file,
                    result -> {
                        convertFileToBitMab(file);
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "No image for this task", Toast.LENGTH_SHORT).show();
                    }
            );
        }

        Amplify.API.query(
                ModelQuery.get(Task.class, taskId),
                response -> {
                    t = response.getData();
                    runOnUiThread(() -> {

                        TextView taskStats = findViewById(R.id.text_view_task_stats);
                        taskDetails.setText(t.getBody());
                        taskStats.setText("Stats : " + t.getState());
                        setSupportActionBar(t.getTitle());

                        if (file.exists()){
                            convertFileToBitMab(file);
                        }
                        loadingProgress.stopLoading();

                        Amplify.API.query(
                                ModelQuery.get(LocationTask.class, t.getTaskLocationId()),
                                respons -> {
                                    if (respons.hasData()){
                                        location = respons.getData();

                                    }



                                },
                                error -> Log.e("MyAmplifyApp", error.toString(), error)
                        );
                    });
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );







    }

    private void convertFileToBitMab(File file){
        if (file!=null){
            ImageView imageView =findViewById(R.id.image_view_task_details);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            imageView.setImageBitmap(bitmap);
        }


    }


}