import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Bill implements Serializable {
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate start;
    private LocalDate end;
    private double total;

    public Bill(Customer customer, Vehicle vehicle, LocalDate start, LocalDate end) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.start = start;
        this.end = end;
        this.total = vehicle.getPrice() * ChronoUnit.DAYS.between(start,end);
    }

    @Override
    public String toString() {
        return "\n=========================================================="+
                "\n"+"Bill: " +"\n"+
                "Customer: " + customer +"\n"+
                "Vehicle: " + vehicle +"\n"+
                "Start Date:" + start +"\n"+
                "End Date: " + end +"\n"+
                "Total: "+ total+"\n"+
                "==========================================================\n";
    }
}
