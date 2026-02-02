package by.instruction.profsouz.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TourismDao {
    @Query("SELECT * FROM tourism_items ORDER BY name ASC")
    LiveData<List<TourismEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TourismEntity> items);

    @Query("DELETE FROM tourism_items")
    void clear();
}
