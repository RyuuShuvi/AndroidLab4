package ua.cn.stu.api.model.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v2/country?region=LCN")
    Call<CountriesResponse> getCountries();
}

