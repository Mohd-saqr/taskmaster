package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import net.gg.myapplication.Helper.LoadingProgress;
import net.gg.myapplication.R;
import net.gg.myapplication.RecyclerView.MyAdapterForRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MapsActivity";
    LoadingProgress progress = new LoadingProgress(MainActivity.this);
    MyAdapterForRecyclerView myAdapterForRecyclerView;
    static List<com.amplifyframework.datastore.generated.model.Task> tasksArray = new ArrayList<>();
    InterstitialAd mInterstitialAd;
     RewardedAd mRewardedAd;
    int countRewardCoin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionalityForBtn();
        setSupportActionBar();
        getShearedPreference();
        runRecyclerView();
        startActivityIntent();
        addGoogleInit();
        bannerAdInit();



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

        // initialits ad
        Button ad = findViewById(R.id.btn_Interstitial_ads);
        ad.setOnClickListener(v->{
            InterstitialAds();
        });

        //reward ads

        Button rewardAds =findViewById(R.id.btn_reward_ads);
        rewardAds.setOnClickListener(v->{
            RewardedAds();
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


    /**
     * google ADMob
     */

    @SuppressLint("MissingPermission")
    private void bannerAdInit(){


        AdView  mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }


    private void addGoogleInit(){
        MobileAds.initialize(this, initializationStatus -> {
        });
    }


    private void InterstitialAds(){




        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MainActivity.this);
                        } else {

                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        }
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });


    }

    private void RewardedAds (){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });

                        /**
                         * show the ads
                         */

                        if (mRewardedAd != null) {
                            Activity activityContext = MainActivity.this;
                            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    Log.d(TAG, "The user earned the reward.");
                                    int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();
                                    countRewardCoin++;
                                    TextView rewardCount =findViewById(R.id.text_view_reward);
                                    rewardCount.setText("You Have Earned : " +rewardType + " " + countRewardCoin);
                                    System.out.println("reward"+rewardType + "" +rewardAmount);
                                }
                            });
                        } else {
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                    }
                });
    }






}







