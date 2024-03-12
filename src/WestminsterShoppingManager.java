import javax.swing.*;
import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {
    private static final int MAX_PRODUCTS = 50;
    private List<Product> productList;
    private UserManager userManager;

    private static final double FIRST_PURCHASE_DISCOUNT = 0.10;
    private static final double CATEGORY_DISCOUNT = 0.20;

    public WestminsterShoppingManager() {

        this.userManager = new UserManager();
        this.productList = new ArrayList<>();
        // Load products from a file if available
       // loadProducts();
    }

    public boolean authenticateUser(String username, String password) {
        return userManager.authenticateUser(username, password);
    }

    public void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            ShoppingGUI shoppingGUI = new ShoppingGUI();
            shoppingGUI.setVisible(true);
        });
    }

    @Override
    public void addProduct(Product product) {
        if (productList.size() < MAX_PRODUCTS) {
            if (isProductIdUnique(product.getProductId())) {
                productList.add(product);
                System.out.println("Product added successfully!");

                // Print the details of the added product
                System.out.println("Product Details:");
                System.out.println(product.toString());

                System.out.println("----------------------");
            } else {
                System.out.println("Product with ID " + product.getProductId() + " already exists. Cannot add duplicate products.");
            }
        } else {
            System.out.println("Cannot add more products. Maximum limit reached.");
        }
    }

    // Helper method to check if a product ID is unique
    private boolean isProductIdUnique(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return false; // ID already exists
            }
        }
        return true; // ID is unique
    }

    @Override
    public void deleteProduct(String productId) {
        Product deletedProduct = findProductById(productId);

        if (deletedProduct != null) {
            productList.remove(deletedProduct);
            System.out.println("Product deleted successfully:");
            System.out.println(deletedProduct.toString());
            System.out.println("Total products left in the system: " + productList.size());
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    @Override
    public void printProducts() {
        // Check if the productList is empty
        if (productList.isEmpty()) {
            System.out.println("No products available.");
        } else {
            // Sort the products alphabetically by product name
            Collections.sort(productList, Comparator.comparing(Product::getProductName));

            System.out.println("List of Products (Sorted Alphabetically by Name):");
            for (Product product : productList) {
                String productType = (product instanceof Electronics) ? "Type: Electronics" : "Type: Clothing";

                System.out.format("%s%s%n", productType, product.toString());
                System.out.println("----------------------");
            }
        }
    }


    @Override
    public void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(productList);
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
            e.printStackTrace();  // Consider logging the exception
        }
    }

    // Load products from file on application startup
    void loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("products.dat"))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                productList = (List<Product>) obj;
                System.out.println("Products loaded from file successfully.");
            }
        } catch (FileNotFoundException e) {
            // Ignore if file not found, it will be created when saving products
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
            e.printStackTrace();  // Consider logging the exception
        }
    }

    // Method to add a product from the console
    public void addProductFromConsole(Scanner scanner) {
        System.out.println("Enter product type (Electronics or Clothing): ");
        String productType = scanner.next();

        if (productType.equalsIgnoreCase("Electronics")) {
            addElectronicsProductFromConsole(scanner);
        } else if (productType.equalsIgnoreCase("Clothing")) {
            addClothingProductFromConsole(scanner);
        } else {
            System.out.println("Invalid product type.");
        }
    }

    // Method to add an Electronics product from the console
    private void addElectronicsProductFromConsole(Scanner scanner) {
        System.out.println("Enter product ID: ");
        String productId = scanner.next();
        System.out.println("Enter product name: ");
        String productName = scanner.next();
        System.out.println("Enter available items: ");
        int availableItems = validatePositiveIntInput(scanner);
        System.out.println("Enter price: ");
        double price = validatePositiveDoubleInput(scanner);
        System.out.println("Enter brand: ");
        String brand = scanner.next();
        System.out.println("Enter warranty period (in months): ");
        int warrantyPeriod = validatePositiveIntInput(scanner);

        Electronics electronicsProduct = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
        addProduct(electronicsProduct);
    }

    // Method to add a Clothing product from the console
    private void addClothingProductFromConsole(Scanner scanner) {
        System.out.println("Enter product ID: ");
        String productId = scanner.next();
        System.out.println("Enter product name: ");
        String productName = scanner.next();
        System.out.println("Enter available items: ");
        int availableItems = validatePositiveIntInput(scanner);
        System.out.println("Enter price: ");
        double price = validatePositiveDoubleInput(scanner);
        System.out.println("Enter size: ");
        String size = scanner.next();
        System.out.println("Enter color: ");
        String color = scanner.next();

        Clothing clothingProduct = new Clothing(productId, productName, availableItems, price, size, color);
        addProduct(clothingProduct);
    }
    

    // Main method for testing the functionality
    public static void main(String[] args) {
        ConsoleMenu.main(args); // Run the console menu
    }


    // Helper method to find a product by ID
    private Product findProductById(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Helper method to validate positive integer input
    private int validatePositiveIntInput(Scanner scanner) {
        int input = -1;
        do {
            try {
                input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next(); // Consume the invalid input
            }
        } while (input <= 0);

        return input;
    }

    // New method to get the product list
    public List<Product> getProductList() {
        return productList;
    }

    // Helper method to validate positive double input
    private double validatePositiveDoubleInput(Scanner scanner) {
        double input = -1.0;
        do {
            try {
                input = scanner.nextDouble();
                if (input <= 0.0) {
                    System.out.println("Please enter a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive number.");
                scanner.next(); // Consume the invalid input
            }
        } while (input <= 0.0);

        return input;
    }


}