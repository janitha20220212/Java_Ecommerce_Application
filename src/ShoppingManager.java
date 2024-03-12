import java.io.Serializable;

public interface ShoppingManager extends Serializable {
    void addProduct(Product product);
    void deleteProduct(String productId);
    void printProducts();
    void saveProducts();

}
