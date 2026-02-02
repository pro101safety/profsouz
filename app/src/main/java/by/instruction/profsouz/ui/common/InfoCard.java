package by.instruction.profsouz.ui.common;

public class InfoCard {
    private final String id;
    private final String title;
    private final String subtitle;
    private final String siteUrl;
    private final String email;
    private final String phone;
    private final String telegramUrl;
    private final String imageUrl;

    public InfoCard(String id, String title, String subtitle, String siteUrl, String email, String phone, String telegramUrl, String imageUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.siteUrl = siteUrl;
        this.email = email;
        this.phone = phone;
        this.telegramUrl = telegramUrl;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
