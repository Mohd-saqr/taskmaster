package net.gg.myapplication.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.LocationTask;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;


import net.gg.myapplication.BuildConfig;
import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.MyModule.Task;

import net.gg.myapplication.R;
import net.gg.myapplication.db.AppDb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddTask extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_PERMISSION = 0;
    LoadingProgress progress = new LoadingProgress(AddTask.this);
    com.amplifyframework.datastore.generated.model.Task taskFromAws;
    private final int IMAGE_TASK_CODE = 20;

    boolean locationPermissionGranted =false;

    Bitmap imageForUpload = null;
    ImageView imageView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Location lastKnownLocation;

    LocationTask locationTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        imageView = findViewById(R.id.view_add_image);
        FunctionalityForBtn();
        setSupportActionBar("Add Task");
        getTeamId();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        checkPermissionRequest();
        try {
            getIntentInflate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String TeamId = "";

    private void getTeamId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TeamId = sharedPreferences.getString("teamId", "Noteam");

    }


    @Override
    protected void onResume() {
        /// set total task number
        TextView TotalTask = findViewById(R.id.text_view_total_task);
        Intent intent = getIntent();
        TotalTask.setText("Total Task : " + getIntent().getStringExtra("totalTask"));
        // check the permission and get the device location
        if (locationPermissionGranted){
            getDeviceLocation();

        }else {
            checkPermissionRequest();
        }

        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();
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
        getTaskForEdit(spinner, addTask, taskTitleField, taskDescriptionInput, saveButton);
        /**
         * this method for previous lab
         */
//        // save using room data base
//        addTaskRoom(spinner, taskTitleField, taskDescriptionInput, addTask);
//        /////  set edit page
//        editTaskRoom(spinner, addTask, taskTitleField, taskDescriptionInput, saveButton);
        /**
         * add image button
         */
        Button addImage = findViewById(R.id.btn_add_image);
        addImage.setOnClickListener(v -> {
            imagePicker();

        });

        Button setLocation = findViewById(R.id.btn_set_location);
        setLocation.setOnClickListener(v->{
            if (locationPermissionGranted){
                Intent intent =  new Intent(getApplicationContext(),Location_activity.class);

                startActivity(intent);
            }else {
                checkPermissionRequest();
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
                        .teamTasksId(taskFromAws.getTeamTasksId())
                        .id(taskFromAws.getId())
                        .build();
                progress.stopLoading();
                Amplify.API.mutate(
                        ModelMutation.update(task),
                        success -> {

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

                 locationTask = LocationTask
                        .builder().latitude(String.valueOf(lastKnownLocation.getLatitude()))
                        .longitude(String.valueOf(lastKnownLocation.getLongitude()))
                        .build();
                Amplify.API.query(ModelMutation.create(locationTask),s->{
                    System.out.println("Location done saved ");
                },err->{

                });
                com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task.builder().
                        title(taskTitleField.getText().toString())
                        .state(spinner.getSelectedItem().toString()).
                        body(taskDescriptionInput.getText().toString())
                        .teamTasksId(TeamId)
                        .taskLocationId(locationTask.getId())
                        .build();
                // add task to data storage aws
                AddDataStorageAws(task);
                try {
                    uploadImageAws(task);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });


    }


    private void AddDataStorageAws(com.amplifyframework.datastore.generated.model.Task task) {
        Amplify.API.query(ModelMutation.create(task), success -> {
            progress.stopLoading();
            finish();
        }, error -> {

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

    void getTaskForEdit(Spinner spinner, Button addTask, EditText taskTitleField, EditText taskDescriptionInput, Button saveButton) {
        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        /// get the task
        Amplify.API.query(ModelQuery.get(com.amplifyframework.datastore.generated.model.Task.class, taskId
                ), res -> {
                    if (res.hasData()) {
                        taskFromAws = res.getData();
                        // set on click for save


                        runOnUiThread(() -> {
                            /// set text dor input
                            taskTitleField.setText(res.getData().getTitle());
                            taskDescriptionInput.setText(res.getData().getBody());
                            /// set visibility for button
                            saveButton.setVisibility(View.VISIBLE);
                            addTask.setVisibility(View.INVISIBLE);
                            TextView title = findViewById(R.id.text_view_add_task);
                            title.setText("Edit Task");
                            setSupportActionBar("Edit Task");

                        });

                    }
                }
                , err -> {
                });

    }

    private void imagePicker() {
        ImagePicker.with(this)
                .compress(1024)            //Final image size will be less than 1 MB
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    /**
     * this method for picker image
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();


        imageView.setImageURI(uri);
        imageForUpload = convertUriToBimap(uri);

    }

    private Bitmap convertUriToBimap(Uri uri) {
        Bitmap bitmap = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private File convertToFile() throws IOException {
        File file = new File(getApplicationContext().getFilesDir(), "image.jpg");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        imageForUpload.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        return file;
    }

    private void uploadImageAws(com.amplifyframework.datastore.generated.model.Task task) throws IOException {
        if (imageForUpload != null) {
            Amplify.Storage.uploadFile(
                    "image" + task.getId(),
                    convertToFile(),
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        }
    }

    private void getIntentInflate() throws IOException {
        Intent intent = getIntent();
        Uri data = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (intent.getType()!=null&&  intent.getType().contains("image/") && data != null) {
            configureAwsAmplify();
            Uri iamgeUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            imageView.setImageURI(iamgeUri);
            imageForUpload = convertUriToBimap(iamgeUri);
            convertToFile();


        }

    }


    /// sum stuff to configure amplify
    void configureAwsAmplify() {
        try {
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

        } catch (AmplifyException e) {
//            Log.e("TAG", "Could not initialize Amplify", e);
        }



    }

    /// this for google map

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;

    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.

                } else {
                    checkPermissionRequest();
                }

            });

    public void makePermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);    }
    public void checkPermissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted=true;

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            makePermissionRequest();
        } else {
            showAlertDialog();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This Permission is require");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                startActivity(i);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                com.google.android.gms.tasks.Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                TextView location =findViewById(R.id.text_location_name);
                                location.setText("Your current  Location : Completely Added");



                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());

                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }



}