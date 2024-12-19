package ua.cn.stu.api.model.API;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "country", strict = false)
public class Country {
    @Element(name = "name")
    private String name;

    @Element(name = "capitalCity")
    private String capitalCity;

    @Element(name = "iso2Code")
    private String iso2Code;  // Add the iso2Code field

    public String getName() {
        return name;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public String getIso2Code() {
        return iso2Code;  // Getter for iso2Code
    }
}
