package by.instruction.profsouz.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import by.instruction.profsouz.data.db.NewsEntity;
import by.instruction.profsouz.data.prefs.UserPreferences;
import by.instruction.profsouz.data.repository.AppRepository;

public class NewsViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final UserPreferences preferences;
    private final LiveData<List<NewsEntity>> news;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        preferences = new UserPreferences(application);
        news = repository.observeNews();
    }

    public LiveData<List<NewsEntity>> getNews() {
        return news;
    }

    public void refresh() {
        repository.refreshNews(preferences.getSelectedUnionId());
    }
}
