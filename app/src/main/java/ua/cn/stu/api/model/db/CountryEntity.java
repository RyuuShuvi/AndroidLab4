package ua.cn.stu.api.model.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries")
public class CountryEntity {

    private String iso2Code;


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String capitalCity;

    public CountryEntity(String iso2Code, String name, String capitalCity) {
        this.iso2Code = iso2Code;
        this.name = name;
        this.capitalCity = capitalCity;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    public String getIso2Code() {
        return iso2Code;
    }

    public String getName() {
        return name;
    }

    public String getCapitalCity() {
        return capitalCity;
    }
}

