import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public enum Product {
    COKE(1, "Coke",10000),
    PEPSI(2, "Pepsi",10000),
    SODA(3,"Soda",20000);
    private final int id;

    private final String name;

    private final int price;

    private Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    private static final Map<Integer, Product> productMap = new HashMap<>();

    static {
        for (Product product : Product.values()) {
            productMap.put(product.id, product);
        }
    }

    public static Product getProductById(int id) {
        return productMap.get(id);
    }

    public String toString() {
        return id + " - " + name + " - " + Money.formatMoney(price);
    }
}
