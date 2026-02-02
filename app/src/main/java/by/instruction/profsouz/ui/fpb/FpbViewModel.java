package by.instruction.profsouz.ui.fpb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import by.instruction.profsouz.data.db.FpbSectionEntity;
import by.instruction.profsouz.data.repository.AppRepository;

public class FpbViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final LiveData<List<FpbSectionEntity>> sections;

    public FpbViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        sections = repository.observeFpbSections();
    }

    public LiveData<List<FpbSectionEntity>> getSections() {
        return sections;
    }

    public void refresh() {
        repository.refreshFpb();
    }
}
