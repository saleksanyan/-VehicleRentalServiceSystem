import java.io.Serializable;

/**
 * this class is a type of vehicle that is why it extends Vehicle
 */
public class Bike extends Vehicle implements Serializable {
    private final int PRICE = 10;
    public Bike(String brand, String model) {
        super(brand, model);
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public Type getType() {
        return Type.BIKE;
    }
}
