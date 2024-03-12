
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingGUI extends JFrame {
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewShoppingCartButton;
    private List<Product> productList;
    private ShoppingCart shoppingCart;

    private JTextArea discountsTextArea;
    private JScrollPane discountsScrollPane;
    private JPanel totalPricePanel;

    public ShoppingGUI() {
        shoppingCart = new ShoppingCart();
        productList = new ArrayList<>();

        setTitle("Westminster Shopping System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"});
        productTable = new JTable();
        productDetailsTextArea = new JTextArea(10, 40);
        addToCartButton = new JButton("Add to Cart");
        viewShoppingCartButton = new JButton("View Shopping Cart");

        applyStyling();

        readProductsFromFile("products.dat");
        updateTable();


        configureMainPanel();
        configureTopCenterPanel();
        configureCenterPanel();
        configureDetailsPanel();

        addEventListeners();
        updateTable();
        updateDetailsPanel();
    }

    private void applyStyling() {
        Font boldFont = new Font("Arial", Font.BOLD, 14);

        productTypeComboBox.setFont(boldFont);
        viewShoppingCartButton.setFont(boldFont);
        addToCartButton.setFont(boldFont);

        productDetailsTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
        productDetailsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        productTable.getTableHeader().setFont(boldFont);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));

        addToCartButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewShoppingCartButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void configureMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setLayout(new BorderLayout());
        add(mainPanel);
    }

    private void configureTopCenterPanel() {
        JPanel topCenterPanel = new JPanel(new FlowLayout());
        topCenterPanel.add(new JLabel("Product Type:"));
        topCenterPanel.add(productTypeComboBox);
        add(topCenterPanel, BorderLayout.NORTH);
    }

    private void configureCenterPanel() {
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        productTable.setRowHeight(30);

        // Center-align the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(viewShoppingCartButton, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void configureDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JScrollPane(productDetailsTextArea), BorderLayout.CENTER);
        detailsPanel.add(addToCartButton, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        productTypeComboBox.addActionListener(e -> updateTable());
        productTable.getSelectionModel().addListSelectionListener(e -> updateDetailsPanel());
        addToCartButton.addActionListener(e -> addToCart());
        viewShoppingCartButton.addActionListener(e -> showShoppingCart());
    }

    private void updateTable() {
        String selectedType = (String) productTypeComboBox.getSelectedItem();
        List<Product> filteredList = (selectedType.equals("All")) ? productList : filterProductsByType(selectedType);

        ProductTableModel tableModel = new ProductTableModel(filteredList);
        productTable.setModel(tableModel);

        customizeColumnRendering("Product");
        customizeColumnRendering("Available Items");

        TableRowSorter<ProductTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);

        int rowCount = productTable.getRowCount();
        if (rowCount > 0) {
            productTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            productTable.scrollRectToVisible(productTable.getCellRect(rowCount - 1, 0, true));
        }

    }

    private void customizeColumnRendering(String columnName) {
        int columnIndex = getColumnIndexByName(productTable, columnName);
        if (columnIndex != -1) {
            TableColumn column = productTable.getColumnModel().getColumn(columnIndex);
            column.setCellRenderer(new ProductCellRenderer());
        }
    }

    private int getColumnIndexByName(JTable table, String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private List<Product> filterProductsByType(String type) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if ((type.equals("Electronics") && product instanceof Electronics) ||
                    (type.equals("Clothes") && product instanceof Clothing)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

    private void updateDetailsPanel() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product selectedProduct = productList.get(selectedRow);
            productDetailsTextArea.setText(selectedProduct.toString());
        }
        DefaultCaret caret = (DefaultCaret) productDetailsTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        productDetailsTextArea.setCaret(caret);
        productDetailsTextArea.append("\n");
        productDetailsTextArea.setCaretPosition(productDetailsTextArea.getDocument().getLength());
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product selectedProduct = productList.get(selectedRow);
            shoppingCart.addToCart(selectedProduct);
            JOptionPane.showMessageDialog(this, "Product added to the shopping cart.");
            updateDiscounts();
            updateTable();
        }
    }

    private void readProductsFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                productList = (List<Product>) obj;
                System.out.println("Products loaded from file successfully.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
        }
    }

    private void showShoppingCart() {
        JFrame cartFrame = new JFrame("Shopping Cart");
        configureCartFrame(cartFrame);
        cartFrame.setVisible(true);
    }

    private void configureCartFrame(JFrame cartFrame) {
        cartFrame.setSize(600, 400);
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable cartTable = new JTable();
        cartTable.setRowHeight(30);

        ShoppingCartTableModel cartTableModel = new ShoppingCartTableModel(shoppingCart.getCartItems());
        cartTable.setModel(cartTableModel);

        // Initialize totalPricePanel
        JPanel totalPricePanel = new JPanel();
        totalPricePanel.add(new JLabel(String.format("Final Total Price: $%.2f", ShoppingCart.calculateTotalPrice(shoppingCart.getCartItems()))));

        JTextArea discountsTextArea = new JTextArea(5, 40);
        JScrollPane discountsScrollPane = new JScrollPane(discountsTextArea);

        configureDiscountsTextArea(discountsTextArea);

        configureCartFrameStyling(cartTable, discountsTextArea, totalPricePanel, cartFrame);
    }


    private void configureDiscountsTextArea(JTextArea discountsTextArea) {
        discountsTextArea.setEditable(false);
        discountsScrollPane = new JScrollPane(discountsTextArea);

        // Calculate discounts
        double firstPurchaseDiscount = ShoppingCart.calculateFirstPurchaseDiscount(shoppingCart.getCartItems());
        double categoryDiscount = ShoppingCart.calculateCategoryDiscount(shoppingCart.getCartItems());

        // Display discounts in the text area
        String discountsText = String.format("First Purchase Discount (10%%): -$%.2f\n" +
                "Three Items in Same Category Discount (20%%): -$%.2f\n", firstPurchaseDiscount, categoryDiscount);

        discountsTextArea.setText(discountsText);
    }

    private void configureCartFrameStyling(JTable cartTable, JTextArea discountsTextArea, JPanel totalPricePanel, JFrame cartFrame) {
        Font boldFont = new Font("Arial", Font.BOLD, 14);

        cartTable.getTableHeader().setFont(boldFont);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 14));

        discountsTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        discountsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalPricePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalPricePanel.setFont(new Font("Arial", Font.PLAIN, 16));

        cartFrame.setLayout(new BorderLayout());
        cartFrame.add(new JScrollPane(cartTable), BorderLayout.CENTER);


        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalPricePanel, BorderLayout.NORTH);
        southPanel.add(discountsScrollPane, BorderLayout.SOUTH); // Use discountsScrollPane
        cartFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private void updateDiscounts() {
        double firstPurchaseDiscount = ShoppingCart.calculateFirstPurchaseDiscount(shoppingCart.getCartItems());
        double categoryDiscount = ShoppingCart.calculateCategoryDiscount(shoppingCart.getCartItems());

        JTextArea discountsTextArea = new JTextArea(5, 40);
        JScrollPane discountsScrollPane = new JScrollPane(discountsTextArea);

        configureDiscountsTextArea(discountsTextArea);

    }

    private void updateDiscountsPanel(JTextArea discountsTextArea) {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            if (frame instanceof JFrame && frame.getTitle().equals("Shopping Cart")) {
                JPanel southPanel = (JPanel) ((JFrame) frame).getContentPane().getComponent(1);
                southPanel.remove(1);
                southPanel.add(discountsScrollPane, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                break;
            }
        }
    }

}
