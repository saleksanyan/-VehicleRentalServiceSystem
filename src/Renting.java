import java.io.Serializable;
import java.time.LocalDate;
/**
 * This class is for renting vehicles for customers
 */
public class Renting implements Serializable{
        private LocalDate start;
        private LocalDate end;
        private Vehicle vehicle;

        private Customer customer;

        public Renting(LocalDate start, LocalDate end, Vehicle vehicle) {
            this.start = start;
            this.end = end;
            this.vehicle = vehicle;

        }

        public Renting(LocalDate start, LocalDate end, Customer customer) {
            this.start = start;
            this.end = end;
            this.customer = customer;
        }

        public LocalDate getStart() {
            return start;
        }

        public Customer getCustomer() {
            if(customer != null)
                return customer;
            return null;
        }

        public LocalDate getEnd() {
            return end;
        }

        public Vehicle getVehicle() {
            if(vehicle != null)
                return vehicle;
            return null;
        }

        public void setStart(LocalDate start) {
            this.start = start;
        }

        public void setEnd(LocalDate end) {
            this.end = end;
        }

        @Override
        public String toString() {
            if(vehicle != null)
                return "\n=========================================================="+ "\n"+
                        "Booking details" +"\n"+
                        "Start Date: " + start +"\n"+
                        "End Date: " + end +"\n"+
                        "Vehicle: " + vehicle + "\n"+
                        "=========================================================="+ "\n";
            else
                return "\n=========================================================="+ "\n"+
                        "Booking:" +"\n"+
                        "Start Date: " + start +"\n"+
                        "End Date: " + end +"\n"+
                        "Customer: " + customer+ "\n"+
                        "=========================================================="+ "\n";

        }

}
