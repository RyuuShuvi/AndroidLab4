package ua.cn.stu.api.model.API;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "countries", strict = false)
public class CountriesResponse {
    @ElementList(name = "country", inline = true)
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }
}

