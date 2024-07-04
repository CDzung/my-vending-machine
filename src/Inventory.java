import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Inventory<T> {
    private final Map<T, Integer> inventory = new HashMap<>();

    public int getQuantity(T item) {
        return inventory.getOrDefault(item, 0);
    }

    public void add(T item) {
        inventory.put(item, inventory.getOrDefault(item, 0) + 1);
    }

    public void deduct(T item) {
        if (hasItem(item)) {
            inventory.put(item, inventory.get(item) - 1);
        }
    }

    public boolean hasItem(T item) {
        return getQuantity(item) > 0;
    }

    public void clear() {
        inventory.clear();
    }

    public void put(T item, int quantity) {
        inventory.put(item, quantity);
    }

    public Set<T> getItems() {
        return Collections.unmodifiableSet(inventory.keySet());
    }

    public Map<T, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }
}
