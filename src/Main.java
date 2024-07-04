import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VendingMachine vendingMachine = VendingMachineFactory.createVendingMachine();

        while (true) {
            System.out.println("1. Select product");
            System.out.println("2. Insert money");
            System.out.println("3. Cancel request");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            switch (option) {
                case 1:
                    selectProduct(vendingMachine, scanner);
                    break;
                case 2:
                    insertMoney(vendingMachine, scanner);
                    break;
                case 3:
                    refund(vendingMachine);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void refund(VendingMachine vendingMachine) {
        try {
            List<Money> refund = vendingMachine.refund();
            if (refund == null) {
                System.out.println("No money to refund.");
                return;
            }
            if (refund.isEmpty()) {
                System.out.println("No money to refund.");
                return;
            }
            System.out.println("Refund:");
            for (Money money : refund) {
                System.out.println(money);
            }
        } catch (RuntimeException e) {
            System.out.println("Can not refund. Please select a product.");
        }

    }

    private static void insertMoney(VendingMachine vendingMachine, Scanner scanner) {
        if (vendingMachine.getCurrentProduct() == null) {
            System.out.println("Please select a product first.");
            return;
        }

        // Insert money
        while(vendingMachine.getCurrentBalance() < vendingMachine.getCurrentProduct().getPrice()) {
            System.out.print("Insert money:");
            int amount;
            try {
                amount = Integer.parseInt(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Invalid money. Please try again.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid money. Please try again.");
                return;
            }

            Money money = Money.getMoneyByValue(amount);
            if (money == null) {
                System.out.println("Invalid money. Please try again.");
                return;
            }

            vendingMachine.insertMoney(money);
            System.out.println("Current balance: " + Money.formatMoney(vendingMachine.getCurrentBalance()));
            if (vendingMachine.getCurrentBalance() >= vendingMachine.getCurrentProduct().getPrice()) {
                break;
            }

            System.out.println("Remaining amount: " + Money.formatMoney(vendingMachine.getCurrentProduct().getPrice() - vendingMachine.getCurrentBalance()));
            System.out.println("Do you want to insert more money? (Y/N)");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("Y")) {
                break;
            }
        }

        if (vendingMachine.getCurrentBalance() >= vendingMachine.getCurrentProduct().getPrice()) {
            System.out.println("Start purchasing product...");

            try {
                Pair<Product, List<Money>> purchase = vendingMachine.purchaseProductAndRemainChange();
                System.out.println("Purchased Product: " + purchase.getKey());
                if (purchase.getValue().isEmpty()) {
                    System.out.println("No change.");
                    return;
                }
                System.out.println("Change:");
                for (Money money : purchase.getValue()) {
                    System.out.println(money);
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void selectProduct(VendingMachine vendingMachine, Scanner scanner) {
        System.out.println("Select product:");
        for (Product product : Product.values()) {
            System.out.println(product);
        }
        System.out.print("Choose a product: ");
        int productId;
        try {
            productId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid product. Please try again.");
            return;
        }
        Product product = Product.getProductById(productId);
        int quantity = vendingMachine.selectProduct(product);
        if (quantity == 0) {
            System.out.println("Product is out of stock.");
            return;
        }
        System.out.println("Selected Product: " + product);
    }
}