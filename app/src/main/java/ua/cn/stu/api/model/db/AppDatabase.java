package ua.cn.stu.api.model.db;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CountryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract CountryDao countryDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "countries-db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}



