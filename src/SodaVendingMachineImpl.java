import java.util.*;

public class SodaVendingMachineImpl implements VendingMachine {
    private Inventory<Product> productInventory = new Inventory<>();
    private Inventory<Money> moneyInventory = new Inventory<>();
    private long currentBalance = 0;
    private Product currentProduct;
    Queue<Product> productQueue = new LinkedList<>();

    public SodaVendingMachineImpl() {
        initialize();
    }

    private void initialize() {
        for (Product product : Product.values()) {
            productInventory.put(product, 10);
        }

        for (Money money : Money.values()) {
            moneyInventory.put(money, 5);
        }
    }

    @Override
    public int selectProduct(Product product) {
        if(productInventory.hasItem(product)) {
            currentProduct = product;
        }

        return productInventory.getQuantity(product);
    }

    @Override
    public long insertMoney(Money money) {
        currentBalance += money.getValue();
        moneyInventory.add(money);

        return currentBalance;
    }

    @Override
    public List<Money> refund() {
        if (currentBalance == 0) {
            return null;
        }
        List<Money> refund = getChange(currentBalance);
        updateMoneyInventory(refund);
        currentBalance = 0;
        currentProduct = null;
        return refund;
    }

    private void updateMoneyInventory(List<Money> refund) {
        for(Money money : refund) {
            moneyInventory.deduct(money);
        }
    }

    private List<Money> getChange(long amount) {
        List<Money> change = new ArrayList<>();

        while(amount > 0) {
            if(amount >= Money.VND_200000.getValue() && moneyInventory.hasItem(Money.VND_200000)) {
                change.add(Money.VND_200000);
                amount -= Money.VND_200000.getValue();
            } else if(amount >= Money.VND_100000.getValue() && moneyInventory.hasItem(Money.VND_100000)) {
                change.add(Money.VND_100000);
                amount -= Money.VND_100000.getValue();
            } else if(amount >= Money.VND_50000.getValue() && moneyInventory.hasItem(Money.VND_50000)) {
                change.add(Money.VND_50000);
                amount -= Money.VND_50000.getValue();
            } else if(amount >= Money.VND_20000.getValue() && moneyInventory.hasItem(Money.VND_20000)) {
                change.add(Money.VND_20000);
                amount -= Money.VND_20000.getValue();
            } else if(amount >= Money.VND_10000.getValue() && moneyInventory.hasItem(Money.VND_10000)) {
                change.add(Money.VND_10000);
                amount -= Money.VND_10000.getValue();
            } else {
                throw new RuntimeException("Not enough change. Please choose another product.");
            }
        }

        return change;
    }

    @Override
    public Pair<Product, List<Money>> purchaseProductAndRemainChange() {
        if(currentProduct == null) {
            throw new RuntimeException("Please select a product first.");
        }

        if(currentBalance < currentProduct.getPrice()) {
            throw new RuntimeException("Not enough money. Please insert more money.");
        }

        if(productInventory.getQuantity(currentProduct) == 0) {
            throw new RuntimeException("Out of stock. Please choose another product.");
        }

        currentBalance -= currentProduct.getPrice();
        productInventory.deduct(currentProduct);

        if (productQueue.size() == 3) {
            productQueue.poll();
            boolean check = true;
            for (Product product : productQueue) {
                if (!product.equals(currentProduct)) {
                    check = false;
                }
            }

            if (check && productInventory.hasItem(currentProduct)) {
                if(Promotion.checkWinRate(currentProduct)) {
                    System.out.println("Congratulations! You won a free " + currentProduct.getName());
                    productInventory.deduct(currentProduct);
                }
            }
        }

        productQueue.add(currentProduct);

        List<Money> change = getChange(currentBalance);
        updateMoneyInventory(change);
        currentBalance = 0;

        Pair<Product, List<Money>> result = new Pair<>(currentProduct, change);

        currentProduct = null;

        return result;
    }

    @Override
    public void reset() {
        productInventory.clear();
        moneyInventory.clear();
        currentBalance = 0;
        currentProduct = null;
        initialize();
    }

    @Override
    public Product getCurrentProduct() {
        return currentProduct;
    }

    @Override
    public long getCurrentBalance() {
        return currentBalance;
    }
}
