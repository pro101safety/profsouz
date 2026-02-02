package by.instruction.profsouz.data.model;

public class UnionDetails {
    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final String siteUrl;
    private final String chairName;
    private final String chairTitle;
    private final String description;
    private final String imageUrl;

    public UnionDetails(String name,
                        String address,
                        String phone,
                        String email,
                        String siteUrl,
                        String chairName,
                        String chairTitle,
                        String description,
                        String imageUrl) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.siteUrl = siteUrl;
        this.chairName = chairName;
        this.chairTitle = chairTitle;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
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

    public String getChairName() {
        return chairName;
    }

    public String getChairTitle() {
        return chairTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
