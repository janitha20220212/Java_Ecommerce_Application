import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private final List<Product> productList;
    private final String[] columnNames = {"Product ID", "Product Name", "Available Items", "Price"};

    public ProductTableModel(List<Product> productList) {
        this.productList = productList != null ? productList : List.of(); // Ensure productList is not null
    }

    @Override
    public int getRowCount() {
        return productList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Return the appropriate class for each column
        if (columnIndex == 0) {
            return Product.class;  // Assuming Product is the class of the "Product" column
        } else if (columnIndex == 1) {
            return Integer.class;  // Assuming "Available Items" is of type Integer
        } else if (columnIndex == 2) {
            return Double.class;   // Assuming "Price" is of type Double
        } else if (columnIndex == 3) {
            return String.class;   // Assuming "Brand" is of type String
        } else if (columnIndex == 4) {
            return Integer.class;  // Assuming "Warranty Period" is of type Integer
        }
        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = productList.get(rowIndex);

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
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Make all cells non-editable
    }

    public DefaultTableCellRenderer getCellRenderer(int row, int column) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        return renderer;
    }

}
