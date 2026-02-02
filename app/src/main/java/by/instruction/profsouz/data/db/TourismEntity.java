package by.instruction.profsouz.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tourism_items")
public class TourismEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String region;
    private String address;
    private String phone;
    private String email;
    private String siteUrl;

    public TourismEntity(@NonNull String id, String name, String region, String address, String phone, String email, String siteUrl) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.siteUrl = siteUrl;
    }

    @NonNull
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
