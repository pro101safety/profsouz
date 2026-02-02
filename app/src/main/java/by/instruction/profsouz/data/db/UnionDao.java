package by.instruction.profsouz.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UnionDao {
    @Query("SELECT * FROM union_items ORDER BY position ASC")
    LiveData<List<UnionEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UnionEntity> items);

    @Query("DELETE FROM union_items")
    void clear();

    @Query("SELECT * FROM union_items WHERE id = :unionId LIMIT 1")
    UnionEntity getById(String unionId);
}
