import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Management {

    private final String VEHICLEFILE = "vehicles.txt";
    private final String CUSTOMERFILE = "customers.txt";
    private File vehicleFile;
    private File customerFile;
    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;


    public Management(){
        try {
            vehicleFile = new File(VEHICLEFILE);
            customerFile = new File(CUSTOMERFILE);
            vehicleFile.createNewFile();
            customerFile.createNewFile();
            customers = getObjects(CUSTOMERFILE);
            vehicles = getObjects(VEHICLEFILE);
            Vehicle.setIdRep(vehicles.size());
            Customer.setIdRep(customers.size());
        }catch (IOException e){
            System.out.println("I/O error occurred");
        }
    }

    public void addVehicle(Vehicle vehicle) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(vehicleFile))) {
            vehicles.add(vehicle);
            outputStream.writeObject(vehicles);
            System.out.println("The vehicle has been added!");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * adds customers to the system/file
     * @param customer -  customer that should be added
     * @throws IOException when file not found
     */
    public void addCustomer(Customer customer) throws IOException, ClassNotFoundException {
        if (!customerIsInTheList(customer)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(customerFile))) {
                customers.add(customer);
                outputStream.writeObject(customers);
                System.out.println(customers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(customer);
            System.out.println("The customer has been added!");
        } else {
            System.out.println("THIS CUSTOMER IS ALREADY IN THE LIST!");
        }
    }

    /**
     * checks if the customer is already in the system
     * @param customer given customer
     * @return true if customer is in the list, no otherwise
     */
    private boolean customerIsInTheList(Customer customer) {
        return customers.contains(customer);
    }


    /**
     * gets vehicles/customers from the file
     * @param fileName the file from which we want to get
     * @return object that was in the file
     * @param <T> vehicle or customer
     * @throws IOException when file not found
     * @throws ClassNotFoundException when we cannot cast the object to T
     */

    public <T> ArrayList<T> getObjects(String fileName) {
        ArrayList<T> items = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            items = (ArrayList<T>) inputStream.readObject();
        } catch (EOFException e) {
            System.out.println( );
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("IOException");
        }
        return items;
    }

    /**
     * books the vehicle for given customer
     * @param start - start date
     * @param end - end date
     * @param c - customer who wants to book a vehicle
     * @param v - vehicles that should be booked
     * @return the bill if the vehicle is available for that period of time
     */
    public Bill addVehicleRenting(LocalDate start, LocalDate end, Customer c, Vehicle v){
        if(!vehicleIsRentedForThatPeriod(v,start,end)) {
            c.addVehicleRentingDate(new Renting(start, end, v));
            v.addVehicleRenting(new Renting(start, end, c));
            replaceVehicle(v);
            replaceCustomer(c);
            Bill b = new Bill(c, v, start, end);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(vehicleFile))) {
                outputStream.writeObject(vehicles);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(customerFile))) {
                outputStream.writeObject(customers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(b);
            System.out.println("The vehicle is rented for that period of time!");
            return b;

        }
        return null;
    }

    /**
     * replaced old vehicle with the same vehicle but with more bookings
     * @param v - same vehicle but with more bookings
     */
    private void replaceVehicle(Vehicle v){
        Vehicle vehicle = findVehicleByID(v.getID());
        vehicles.remove(vehicle);
        vehicles.add(v);
    }
    /**
     * replaced old customer with the same customer but with more bookings
     * @param c - same customer but with more bookings
     */
    private void replaceCustomer(Customer c){
        Customer c1 = findCustomerByID(c.getID());
        customers.remove(c1);
        customers.add(c);
    }

    /**
     * gets customers
     * @return customers
     */

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * gets vehicles
     * @return vehicles
     */

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * saves given vehicles history to the given file
     * @param v - vehicle which history we want to save
     * @param path - path to the file in which we want to save the history
     * @throws FileNotFoundException when the file cannot be found
     */
    public void savingSpecificVehicleHistory(Vehicle v, File path) throws FileNotFoundException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(v.getVehicleRenting());
            System.out.println("Vehicle's report is written in the file: "+ path);
        } catch (IOException e) {
            System.out.println("File not found!");
        }

    }

    /**
     * saves the bill from the booking
     * @param b the bill that should be saved
     * @param path the path to the file in which bill should be saved
     * @throws IOException when file not found
     */
    public void savingBill(Bill b, String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(b);
            System.out.println("Bill has been saved in the file: "+ path);
        }catch (IOException e) {
            System.out.println("File not found!");
        }


    }


    /**
     * gets from the given file vehicles with the same ID as the given vehicle is
     * @param path path to the file where we search vehicle information
     * @param vehicleID given vehicle ID
     * @throws FileNotFoundException if the path of the file is wrong or there is no file with that name
     */
    public void lookingIntoSpecificVehicleHistory(File path, int vehicleID) throws FileNotFoundException {
        ArrayList<?> history;
        Boolean hasSpeceficVehicleObject = false;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            history = (ArrayList<?>) inputStream.readObject();
            for (Object obj:
                    history) {
                if((obj instanceof Vehicle vehicle)) {
                    if((vehicle).getID() == vehicleID) {
                        System.out.println(vehicle);
                        hasSpeceficVehicleObject = true;
                    }
                }
            }
            if(!hasSpeceficVehicleObject)
                System.out.println("There is no vehicle with specified ID in this file\n");
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File not found!");
        }


    }

    /**
     * finds customer with the given id
     * @param id given id
     * @return customer with the given id
     */
    public Customer findCustomerByID(int id){
        for (Customer customer:
                customers) {
            if(customer.getID() == id)
                return customer;
        }
        return null;
    }

    /**
     * this method lets the customer return the vehicle that was rented by them at this period of time
     * @param v the vehicle that customer wants to return
     */
    public void returnRentedVehicles(Vehicle v){
        if(vehicleIsRentedForThatPeriod(v,LocalDate.now(),
                LocalDate.now())){
            ArrayList<Renting> r = v.getVehicleRenting();
            for (int i = 0; i < r.size(); i++) {
                if(((r.get(i).getStart().isBefore(LocalDate.now())
                        && r.get(i).getStart().isAfter(LocalDate.now()))
                        || r.get(i).getStart().equals(LocalDate.now()))
                        || ((r.get(i).getEnd().isBefore(LocalDate.now())
                        && r.get(i).getEnd().isAfter(LocalDate.now()))
                        || r.get(i).getEnd().equals(LocalDate.now())))
                    r.remove(r.get(i));
            }
            System.out.println("Return has been completed successfully");
        }
        else
            System.out.println("The vehicle is not rented");
    }

    /**
     * finds vehicle with the given id
     * @param id given id
     * @return vehicle with the given id
     */
    public Vehicle findVehicleByID(int id){
        for (Vehicle vehicle:
                vehicles) {
            if(vehicle.getID() == id)
                return vehicle;
        }
        return null;
    }

    /**
     * prints vehicle bookings on the screen if there are any
     * @param v given vehicle
     */
    public void vehicleReport(Vehicle v){
        ArrayList<Renting> bookings = v.getVehicleRenting();
        if(bookings.isEmpty()) {
            System.out.println("The vehicle does not have a history of bookings\n");
            return;
        }
        for (Renting b:
                bookings) {
            System.out.println(b);
        }
    }

    /**
     * prints customer bookings on the screen if there are any
     * @param c given customer
     */

    public void customerReport(Customer c){
        ArrayList<Renting> bookings = c.getVehicleRentingDates();
        if(bookings.isEmpty()){
            System.out.println("The customer does not have a history of bookings\n");
            return;
        }
        for (Renting b:
                bookings) {
            System.out.println(b);
        }    }


    /**
     * checks if the vehicle is booked for that period of time
     * @param v vehicle that we should check
     * @param s starting date of reservation
     * @param e ending date of reservation
     * @return true if the vehicle is booked, false otherwise
     */
    private boolean vehicleIsRentedForThatPeriod(Vehicle v, LocalDate s, LocalDate e){
        ArrayList<Renting> r = v.getVehicleRenting();
        if(r==null)
            return false;
        if(vehicles.contains(v))
            for (int i = 0; i < r.size(); i++) {
                if(((r.get(i).getStart().isBefore(e) && r.get(i).getStart().isAfter(s)) || r.get(i).getStart().equals(s))
                        || ((r.get(i).getEnd().isBefore(e) && r.get(i).getEnd().isAfter(s)) || r.get(i).getEnd().equals(e)))
                    return true;
            }
        return false;
    }

}
