import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProductCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check the available items and set the background color to red if less than 3
        int availableItemsColumnIndex = table.getColumnModel().getColumnIndex("Available Items");
        int availableItems = (int) table.getValueAt(row, availableItemsColumnIndex);

        if (availableItems < 3) {
            cellComponent.setBackground(Color.RED);
            cellComponent.setForeground(Color.WHITE);
            cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
        } else {
            // Reset background and text color to default
            cellComponent.setBackground(table.getBackground());
            cellComponent.setForeground(table.getForeground());
            cellComponent.setFont(table.getFont());
        }

        return cellComponent;
    }
}