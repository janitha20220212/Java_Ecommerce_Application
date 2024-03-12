import java.io.Serializable;

abstract class Product implements Serializable {
    private String productId;
    private String productName;
    private int availableItems;
    private double price;

    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Add getCategory() method to determine the category of the product
    public abstract String getCategory();

    @Override
    public String toString() {
        return "ID: " + productId + "\nName: " + productName + "\nAvailable Items: " + availableItems + "\nPrice: " + price;
    }
}
