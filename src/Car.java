import java.io.Serializable;

/**
 * this class is a type of vehicle that is why it extends Vehicle
 */
public class Car extends Vehicle implements Serializable {
    private final int PRICE = 50;
    public Car(String brand, String model) {
        super(brand, model);
    }
    @Override
    public int getPrice() {
        return  PRICE;
    }

    @Override

    public Type getType(){
        return Type.CAR;
    }
}
