//Janitha dissanayaka
//IIT ID : 20220212
//UOW ID : 19537894


import java.util.Scanner;

public class ConsoleMenu {
    public static void main(String[] args) {

        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Scanner scanner = new Scanner(System.in);

        int choice = 0;

        do {
            System.out.println("\n===== Console Menu =====");
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print products");
            System.out.println("4. Save products to file");
            System.out.println("5. Load products from file");
            System.out.println("6. Launch GUI");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        shoppingManager.addProductFromConsole(scanner);
                        break;
                    case 2:
                        System.out.print("Enter the product ID to delete: ");
                        String productIdToDelete = scanner.next();
                        shoppingManager.deleteProduct(productIdToDelete);
                        break;
                    case 3:
                        shoppingManager.printProducts();
                        break;
                    case 4:
                        shoppingManager.saveProducts();
                        break;
                    case 5:
                        shoppingManager.loadProducts();
                        break;
                    case 6:
                        shoppingManager.launchGUI();
                        break;
                    case 7:
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Consume the newline character
            }

        } while (choice != 7);

        scanner.close();
    }
}
