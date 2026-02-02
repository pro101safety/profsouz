package by.instruction.profsouz.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news_items ORDER BY publishedAt DESC")
    LiveData<List<NewsEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewsEntity> items);

    @Query("DELETE FROM news_items")
    void clear();
}
