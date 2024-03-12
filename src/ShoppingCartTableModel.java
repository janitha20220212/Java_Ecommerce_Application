import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ShoppingCartTableModel extends AbstractTableModel {
    private List<Product> cartItems;
    private final String[] columnNames = {"Product Name", "Category", "Price"};

    public ShoppingCartTableModel(List<Product> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public int getRowCount() {
        return cartItems.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < cartItems.size()) {
            // For cart items
            Product product = cartItems.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return product.getProductId();
                case 1:
                    return product.getProductName();
                case 2:
                    return product.getAvailableItems();
                case 3:
                    return product.getPrice();
                default:
                    return null;
            }
        } else {
            // For summary rows
            double totalPrice = ShoppingCart.calculateTotalPrice(cartItems);
            double discount = ShoppingCart.calculateDiscount(cartItems);
            double finalTotal = totalPrice - discount;

            switch (columnIndex) {
                case 0:
                    return "Discount";
                case 1:
                    return discount;
                case 2:
                    return "Subtotal";
                case 3:
                    return totalPrice;
                default:
                    return null;
            }
        }
    }


    @Override
    public String getColumnName(int column) {
        // Return column names for the table headers
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Name";
            case 2:
                return "Available Items";
            case 3:
                return "Price";
            default:
                return "";
        }
    }
}
