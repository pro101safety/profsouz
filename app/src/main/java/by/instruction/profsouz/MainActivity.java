package by.instruction.profsouz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import by.instruction.profsouz.databinding.ActivityMainBinding;
import by.instruction.profsouz.data.sync.SyncWorker;
import by.instruction.profsouz.ui.home.NewsDetailActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String RADIO_NARODNOE_URL = "https://narodnoeradio.by/";
    private static final String RADIO_NOVOE_URL = "https://www.novoeradio.by/";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_leadership,
                R.id.nav_official_info,
                R.id.nav_belchas,
                R.id.nav_home,
                R.id.nav_fpb,
                R.id.nav_unions,
                R.id.nav_tourism,
                R.id.nav_reception_schedule,
                R.id.nav_documents_catalog,
                R.id.nav_legal_aid,
                R.id.nav_property
        ).setOpenableLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        scheduleImmediateSync();
        scheduleBackgroundSync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        bindRadioAction(menu, R.id.action_radio_narodnoe);
        bindRadioAction(menu, R.id.action_radio_novoe);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contacts) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_contacts);
            return true;
        } else if (id == R.id.action_privacy) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_privacy);
            return true;
        } else if (id == R.id.action_radio_narodnoe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(RADIO_NARODNOE_URL));
            startActivity(intent);
            return true;
        } else if (id == R.id.action_radio_novoe) {
            startActivity(NewsDetailActivity.createIntent(this, RADIO_NOVOE_URL));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void scheduleBackgroundSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                SyncWorker.class,
                6,
                TimeUnit.HOURS
        ).setConstraints(constraints).build();
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("fpb_sync", ExistingPeriodicWorkPolicy.REPLACE, request);
    }

    private void scheduleImmediateSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this)
                .enqueueUniqueWork("fpb_sync_now", ExistingWorkPolicy.REPLACE, request);
    }

    private void bindRadioAction(Menu menu, int itemId) {
        MenuItem item = menu.findItem(itemId);
        if (item == null) {
            return;
        }
        View actionView = item.getActionView();
        if (actionView != null) {
            actionView.setOnClickListener(view -> onOptionsItemSelected(item));
        }
    }
}