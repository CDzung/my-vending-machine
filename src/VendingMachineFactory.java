public class VendingMachineFactory {
    public static VendingMachine createVendingMachine() {
        return new SodaVendingMachineImpl();
    }
}
