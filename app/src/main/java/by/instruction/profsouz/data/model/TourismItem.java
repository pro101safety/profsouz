package by.instruction.profsouz.data.model;

import com.google.gson.annotations.SerializedName;

public class TourismItem {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("region")
    private String region;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("siteUrl")
    private String siteUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSiteUrl() {
        return siteUrl;
    }
}
