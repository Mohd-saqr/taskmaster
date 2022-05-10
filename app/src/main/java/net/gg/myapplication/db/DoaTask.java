package net.gg.myapplication.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import net.gg.myapplication.MyModule.Task;
import java.util.List;

@Dao
public interface DoaTask {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id = :id")
    Task getTask(Long id);

    @Insert
    void AddTask(Task task);

    @Delete
    void DeleteTask(Task task);

    @Update
    void UpdateTask(Task task);

}
