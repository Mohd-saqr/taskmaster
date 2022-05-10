package net.gg.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.gg.myapplication.MyModule.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDb extends RoomDatabase {
    public abstract DoaTask doaTask();

    private static AppDb appDb;

    public static synchronized AppDb getInstance(Context context) {
        if (appDb == null) {
            appDb = Room.databaseBuilder(context,
                    AppDb.class, "database-name").allowMainThreadQueries().build();
        }
        return appDb;
    }

}
