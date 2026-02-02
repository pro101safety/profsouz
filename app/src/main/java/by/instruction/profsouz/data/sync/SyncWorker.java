package by.instruction.profsouz.data.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import by.instruction.profsouz.data.prefs.UserPreferences;
import by.instruction.profsouz.data.repository.AppRepository;

public class SyncWorker extends Worker {
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppRepository repository = new AppRepository(getApplicationContext());
        UserPreferences preferences = new UserPreferences(getApplicationContext());
        repository.syncAllBlocking(preferences.getSelectedUnionId());
        return Result.success();
    }
}
