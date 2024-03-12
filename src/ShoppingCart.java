import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ShoppingCart  implements java.io.Serializable {


    private java.util.List<Product> items;
    private static List<Product> cartItems;

    public ShoppingCart() {
        cartItems = new ArrayList<>();
        this.items = new java.util.ArrayList<>();
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public void addItem(Product product) {
        items.add(product);
    }

    public void addToCart(Product product) {
        cartItems.add(product);
    }

    public double calculateFinalPrice() {
        // Calculate final price with discounts
        // (Additional logic for discounts as specified)
        return 0.0; // Placeholder, implement the actual calculation
    }

    public static double calculateTotalPrice(List<Product> cartItems) {
        double totalPrice = 0;

        // Ensure cartItems is not null before iterating
        if (cartItems != null) {
            for (Product product : cartItems) {
                totalPrice += product.getPrice();
            }
        }

        // Apply discounts
        totalPrice = applyCategoryDiscount(totalPrice);
        totalPrice = applyFirstPurchaseDiscount(totalPrice);

        return totalPrice;
    }

    private static double applyCategoryDiscount(double totalPrice) {
        // Apply 20% discount when the user buys at least three products of the same category
        long electronicsCount = cartItems.stream().filter(product -> product instanceof Electronics).count();
        long clothingCount = cartItems.size() - electronicsCount;

        if (electronicsCount >= 3 || clothingCount >= 3) {
            totalPrice *= 0.8; // 20% discount
        }

        return totalPrice;
    }
    static double calculateCategoryDiscount(List<Product> cartItems) {
        double totalPrice = calculateTotalPrice(cartItems);

        long electronicsCount = cartItems.stream().filter(product -> product instanceof Electronics).count();
        long clothingCount = cartItems.size() - electronicsCount;

        if (electronicsCount >= 3 || clothingCount >= 3) {
            return 0.2; // 20% discount
        }

        return 0;
    }

    static double calculateFirstPurchaseDiscount(List<Product> cartItems) {
        if (!cartItems.isEmpty()) {
            Product firstProduct = cartItems.get(0);
            if (firstProduct instanceof Electronics || firstProduct instanceof Clothing) {
                return 0.1; // 10% discount
            }
        }

        return 0;
    }

    private static double applyFirstPurchaseDiscount(double totalPrice) {
        // Apply 10% discount for the very first purchase
        if (cartItems.size() > 0) {
            Product firstProduct = cartItems.get(0);
            if (firstProduct instanceof Electronics || firstProduct instanceof Clothing) {
                totalPrice *= 0.9; // 10% discount
            }
        }

        return totalPrice;
    }

    public static double calculateDiscount(List<Product> cartItems) {
        double discount = 0;

        // Calculate discount based on the product category
        long electronicsCount = cartItems.stream().filter(product -> product instanceof Electronics).count();
        long clothingCount = cartItems.stream().filter(product -> product instanceof Clothing).count();

        if (electronicsCount >= 3) {
            // Apply 20% discount for at least three electronics products
            discount += 0.2;
        }

        if (clothingCount >= 3) {
            // Apply 20% discount for at least three clothing products
            discount += 0.2;
        }

        // Apply 10% discount for the very first purchase
        if (cartItems.size() > 0) {
            Product firstProduct = cartItems.get(0);
            if (firstProduct instanceof Electronics || firstProduct instanceof Clothing) {
                discount += 0.1;
            }
        }

        return discount;
    }

    public List<Product> getProductsByCategory(String category) {
        // Get products from the cart based on the specified category
        return cartItems.stream()
                .filter(product -> category.equalsIgnoreCase("All") || product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        // Format the shopping cart details for display
        StringBuilder cartDetails = new StringBuilder("Shopping Cart:\n");
        for (Product item : items) {
            cartDetails.append(item.toString()).append("\n");
        }
        cartDetails.append("Final Price: $").append(calculateFinalPrice());
        return cartDetails.toString();
    }
}