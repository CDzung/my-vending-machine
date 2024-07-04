import java.util.List;

public interface VendingMachine {
    public List<Money> refund();
    public Pair<Product, List<Money>> purchaseProductAndRemainChange();
    public int selectProduct(Product product);
    public long insertMoney(Money money);
    public void reset();
    public Product getCurrentProduct();
    public long getCurrentBalance();
}
