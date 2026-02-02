package by.instruction.profsouz.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "union_items")
public class UnionEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String siteUrl;
    private String email;
    private String phone;
    private String telegramUrl;
    private String description;
    private String imageUrl;
    private int position;

    public UnionEntity(@NonNull String id, String name, String siteUrl, String email, String phone, String telegramUrl, String description, String imageUrl, int position) {
        this.id = id;
        this.name = name;
        this.siteUrl = siteUrl;
        this.email = email;
        this.phone = phone;
        this.telegramUrl = telegramUrl;
        this.description = description;
        this.imageUrl = imageUrl;
        this.position = position;
    }

    @NonNull
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPosition() {
        return position;
    }
}
