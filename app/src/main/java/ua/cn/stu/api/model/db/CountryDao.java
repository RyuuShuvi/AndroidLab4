package ua.cn.stu.api.model.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import androidx.lifecycle.LiveData;


@Dao
public interface CountryDao {

    @Insert
    void insertAll(List<CountryEntity> countries);

    @Query("SELECT * FROM countries")
    LiveData<List<CountryEntity>> getAllCountriesLive();

    @Query("SELECT * FROM countries WHERE iso2Code = :iso2Code")
    LiveData<CountryEntity> getCountryByIso2Code(String iso2Code);
}
