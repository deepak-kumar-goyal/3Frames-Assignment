import java.util.*;

/*
Problem-3:
Come up with an approach for product configuration, where multiple products can be
stored. Build an in-memory database or in-memory storage. We should be able to have
product categories along with product descriptions and details. We should be able to store a
wide range of types of products similar to Amazon, we should be able to implement efficient
search of the products and flexible configuration of the products. In addition to in-memory
storage, build an efficient textual search on any of the parameters (similar to search in
Amazon).
*/

// Class to represent a product
class Product {
    private String productId;
    private String productName;
    private String category;
    private String description;
    private Map<String, String> details;

    public Product(String productId, String productName, String category, String description, Map<String, String> details) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.details = details;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}

// Class to represent an in-memory product database
class ProductDatabase {
    private Map<String, Product> productMap;
    private Map<String, List<Product>> categoryMap;

    public ProductDatabase() {
        productMap = new HashMap<>();
        categoryMap = new HashMap<>();
    }

    // Method to add a product to the database
    public void addProduct(Product product) {
        productMap.put(product.getProductId(), product);
        categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
    }

    // Method to search products by category
    public List<Product> searchByCategory(String category) {
        return categoryMap.getOrDefault(category, new ArrayList<>());
    }

    // Method to search products by keyword in product name, description, or details
    public List<Product> searchByKeyword(String keyword) {
        List<Product> results = new ArrayList<>();
        for (Product product : productMap.values()) {
            if (product.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getDetails().values().stream().anyMatch(value -> value.toLowerCase().contains(keyword.toLowerCase()))) {
                results.add(product);
            }
        }
        return results;
    }
}

public class Solution3 {
    public static void main(String[] args) {
        // Initialize product database
        ProductDatabase productDatabase = new ProductDatabase();

        // Add sample products
        productDatabase.addProduct(new Product("P1", "Laptop", "Electronics", "High-performance laptop",
                new HashMap<String, String>(){{
                    put("Brand", "Dell");
                    put("Processor", "Intel Core i7");
        }}));
        productDatabase.addProduct(new Product("P2", "Smartphone", "Electronics", "Flagship smartphone",
                new HashMap<String, String>(){{
                    put("Brand", "Samsung");
                    put("RAM", "8GB");
                }}));
        productDatabase.addProduct(new Product("P3", "Running Shoes", "Sports", "Lightweight running shoes",
                new HashMap<String, String>(){{
                    put("Brand", "Nike");
                    put("Size", "10");
                }}));
        productDatabase.addProduct(new Product("P4", "Yoga Mat", "Sports", "Non-slip yoga mat",
                new HashMap<String, String>(){{
                    put("Brand", "Lululemon");
                    put("Color", "Blue");
                }}));

        // Search products by category
        System.out.println("Search by category 'Electronics':");
        List<Product> electronicsProducts = productDatabase.searchByCategory("Electronics");
        for (Product product : electronicsProducts) {
            System.out.println(product.getProductName());
        }

        // Search products by keyword
        System.out.println("\nSearch by keyword 'running':");
        List<Product> keywordProducts = productDatabase.searchByKeyword("running");
        for (Product product : keywordProducts) {
            System.out.println(product.getProductName());
        }
    }
}
