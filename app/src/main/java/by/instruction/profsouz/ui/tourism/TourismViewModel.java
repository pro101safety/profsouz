package by.instruction.profsouz.ui.tourism;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import by.instruction.profsouz.data.db.TourismEntity;
import by.instruction.profsouz.data.repository.AppRepository;

public class TourismViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final LiveData<List<TourismEntity>> tourism;

    public TourismViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        tourism = repository.observeTourism();
    }

    public LiveData<List<TourismEntity>> getTourism() {
        return tourism;
    }

    public void refresh() {
        repository.refreshTourism();
    }
}
