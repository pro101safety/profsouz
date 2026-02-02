package by.instruction.profsouz.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FpbDao {
    @Query("SELECT * FROM fpb_sections ORDER BY title ASC")
    LiveData<List<FpbSectionEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FpbSectionEntity> items);

    @Query("DELETE FROM fpb_sections")
    void clear();
}
