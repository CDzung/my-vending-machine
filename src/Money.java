import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public enum Money {
    VND_10000(10000),
    VND_20000(20000),
    VND_50000(50000),
    VND_100000(100000),
    VND_200000(200000);

    private final int value;

    private Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Map<Integer, Money> moneyMap = new HashMap<>();

    static {
        for (Money money : Money.values()) {
            moneyMap.put(money.value, money);
        }
    }

    public static Money getMoneyByValue(int value) {
        return moneyMap.get(value);
    }

    public String toString() {
        return formatMoney(value);
    }

    public static String formatMoney(long money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money) + " VND";
    }
}
