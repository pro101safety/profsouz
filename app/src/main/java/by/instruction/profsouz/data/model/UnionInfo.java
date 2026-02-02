package by.instruction.profsouz.data.model;

import com.google.gson.annotations.SerializedName;

public class UnionInfo {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("siteUrl")
    private String siteUrl;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("telegramUrl")
    private String telegramUrl;
    @SerializedName("description")
    private String description;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public String getDescription() {
        return description;
    }
}
