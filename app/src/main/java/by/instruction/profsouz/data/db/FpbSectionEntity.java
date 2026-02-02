package by.instruction.profsouz.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fpb_sections")
public class FpbSectionEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String contact;
    private String linkUrl;

    public FpbSectionEntity(@NonNull String id, String title, String content, String imageUrl, String contact, String linkUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.contact = contact;
        this.linkUrl = linkUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContact() {
        return contact;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}
