package by.instruction.profsouz.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_items")
public class NewsEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String url;
    private String source;
    private long publishedAt;
    private String imageUrl;
    private String summary;

    public NewsEntity(@NonNull String id, String title, String url, String source, long publishedAt, String imageUrl, String summary) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.source = source;
        this.publishedAt = publishedAt;
        this.imageUrl = imageUrl;
        this.summary = summary;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSummary() {
        return summary;
    }
}
