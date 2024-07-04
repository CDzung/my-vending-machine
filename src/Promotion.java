import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Promotion {
    private static final DateTimeFormatter DATE_KEY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final double INCREASE_WIN_RATE = 1.5;
    public static final double DEFAULT_WIN_RATE = 0.1;
    private static final long LIMITED_BUDGET_PER_DAY = 50000;
    private static Map<Integer, Long> salePerDayMap = new HashMap<>();
    private static double currentWinRate = 0;

    public static boolean checkWinRate(Product product) {
        Integer today = generateDateKey(LocalDateTime.now());
        Integer yesterday = generateDateKey(LocalDateTime.now().minusDays(1));

        Long saleToday = salePerDayMap.get(today);
        Long saleYesterday = salePerDayMap.get(yesterday);

        if (currentWinRate == 0) {
            currentWinRate = DEFAULT_WIN_RATE;
        } else {
            if (saleYesterday == null || saleYesterday <= LIMITED_BUDGET_PER_DAY) {
                currentWinRate *= INCREASE_WIN_RATE;
            }

            if (saleToday != null && saleToday + product.getPrice() > LIMITED_BUDGET_PER_DAY) {
                return false;
            }
        }

        boolean win = Math.random() < currentWinRate;
        if (win) {
            salePerDayMap.put(today, saleToday == null ? product.getPrice() : saleToday + product.getPrice());
            return true;
        }

        return false;
    }

    public static int generateDateKey(LocalDateTime localDateTime) {
        return Integer.parseInt(DATE_KEY_FORMATTER.format(localDateTime));
    }
}
