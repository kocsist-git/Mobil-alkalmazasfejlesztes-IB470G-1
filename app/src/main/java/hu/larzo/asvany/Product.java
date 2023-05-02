package hu.larzo.asvany;

public class Product {
    private String name;
    private String imageUrl;
    private String title;
    private String price;

    public Product() {
    }

    public Product(String name, String imageUrl, String price, String title) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

}

