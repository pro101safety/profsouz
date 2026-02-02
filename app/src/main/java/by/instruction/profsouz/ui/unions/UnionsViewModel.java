package by.instruction.profsouz.ui.unions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import by.instruction.profsouz.data.db.UnionEntity;
import by.instruction.profsouz.data.repository.AppRepository;

public class UnionsViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final LiveData<List<UnionEntity>> unions;

    public UnionsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        unions = repository.observeUnions();
    }

    public LiveData<List<UnionEntity>> getUnions() {
        return unions;
    }

    public void refresh() {
        repository.refreshUnions();
    }
}
