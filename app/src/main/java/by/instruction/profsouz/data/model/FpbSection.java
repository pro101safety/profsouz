package by.instruction.profsouz.data.model;

import com.google.gson.annotations.SerializedName;

public class FpbSection {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("contact")
    private String contact;
    @SerializedName("linkUrl")
    private String linkUrl;

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
