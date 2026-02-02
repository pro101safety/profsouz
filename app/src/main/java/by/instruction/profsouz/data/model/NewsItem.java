package by.instruction.profsouz.data.model;

import com.google.gson.annotations.SerializedName;

public class NewsItem {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("source")
    private String source;
    @SerializedName("publishedAt")
    private long publishedAt;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("summary")
    private String summary;

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
