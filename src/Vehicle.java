import java.io.Serializable;
import java.util.ArrayList;

/**
 * this abstract class represents vehicle information
 */
public abstract class Vehicle implements Serializable {
    enum Type {CAR, BIKE, VAN}

    private static int idRep;
    private int id;
    private final ArrayList<Renting> vehicleRenting;

    private String brand;
    private String model;


    public Vehicle(String brand, String model) {
        vehicleRenting = new ArrayList<>();
        id = ++idRep;
        this.model = model;
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static int getIdRep() {
        return idRep;
    }

    public ArrayList<Renting> getVehicleRenting() {
        return vehicleRenting;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicle.getID() == this.getID();
    }

    public void addVehicleRenting(Renting info) {
        vehicleRenting.add(info);
    }

    public int getID() {
        return id;
    }

    public static void setIdRep(int idRep) {
        Vehicle.idRep = idRep;
    }

    public abstract int getPrice();

    public abstract Type getType();

    @Override
    public String toString() {
        return "ID: " + id + " Type: " + getType() + " Brand: " + brand + " Model: " + model;
    }
}
