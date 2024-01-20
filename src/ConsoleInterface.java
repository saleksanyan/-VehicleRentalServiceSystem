import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class gets together all the functionalities and creates the system
 */
public class ConsoleInterface {

    public ConsoleInterface() {
        Scanner scanner = new Scanner(System.in);
        String actionNum = getAction(scanner);
        Management management = new Management();
        try {
            while (!actionNum.equals("11")) {
                if (actionNum.equals("1")) {
                    action1(scanner, management);

                } else if (actionNum.equals("4")) {
                    addCustomer(scanner, management);

                } else if (!management.getVehicles().isEmpty()) {
                    if (!management.getCustomers().isEmpty()) {
                        if (actionNum.equals("5")) {
                            rentVehicle(management, scanner);
                        }
                    }
                    if (actionNum.equals("2")) {
                        Vehicle v = getVehicle(management, scanner);
                        System.out.println("You can only update details about brand and model\n" +
                                "What do you want to update?(1 - brand, 2 - model)");
                        String updateType = scanner.nextLine().trim();
                        while (!(actionNum.matches("\\d+") &&
                                (Integer.parseInt(updateType) == 1 || Integer.parseInt(updateType) == 2))) {
                            System.out.println("Please choose between 1,2");
                            updateType = scanner.nextLine().trim();
                        }

                        if (updateType.equals("1")) {
                            System.out.println("what brand do you want to set the vehicle?");
                            v.setBrand(scanner.nextLine().trim());
                            System.out.println("The brand is updated!");
                        } else {
                            System.out.println("what model do you want to set the vehicle?");
                            v.setModel(scanner.nextLine().trim());
                            System.out.println("The model is updated!");
                        }
                    } else if (actionNum.equals("3")) {
                        System.out.println("Which vehicle do you want to delete?");
                        Vehicle v = getVehicle(management, scanner);
                        management.getVehicles().remove(v);
                        System.out.println("The vehicle is removed!");
                    } else if (actionNum.equals("8")) {
                        Vehicle v = getVehicle(management, scanner);
                        management.vehicleReport(v);
                    } else if (actionNum.equals("9")) {
                        File file = getFile("Please enter the name/path " +
                                "of the file in which you want to look into", scanner, true);
                        management.lookingIntoSpecificVehicleHistory(file, getVehicle(management, scanner).getID());
                    } else if (actionNum.equals("10")) {
                        Vehicle r = getVehicle(management, scanner);
                        File file = getFile("Please enter the name/path of " +
                                "the file in which you want to save the vehicle info", scanner, false);
                        management.savingSpecificVehicleHistory(r, file);
                    }

                }else {
                        System.out.println("The dataset of vehicles is empty!");
                }

                if (!management.getCustomers().isEmpty()) {
                    if (actionNum.equals("6")) {
                        System.out.println("Which vehicle do you want to return?");
                        Vehicle v = getVehicle(management, scanner);
                        management.returnRentedVehicles(v);
                    } else if (actionNum.equals("7")) {
                        Customer c = getCustomer(management, scanner);
                        management.customerReport(c);
                    }
                } else {
                        System.out.println("The dataset of customers is empty!");
                    }
                actionNum = getAction(scanner);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Bye!!");
        }
        }



    /**
     * asks user what he wants to do in the system
     * @param scanner reads the user answers
     * @return user answer
     */
    private static String getAction(Scanner scanner) {
        String actionNum;
        System.out.println("Please type ONLY the numbers that you can see in " +
                "front of the given action"+"\n"+
                "1 - Add vehicle"+"\n"+"2 - Update vehicle\n3 - Delete vehicle\n"+"4 - Add customer"+
                "\n"+"5 - Rent a vehicle\n"+"6 - Return a vehicle" +"\n"+
                "7 - Make a report for a specific customer"+"\n"+ "8 - Make a report " +
                "for a specific vehicle"+"\n"+
                "9 - Look into specific vehicle's history via file path"+"\n"+
                "10 - Saving specific vehicle's history via file path"+"\n"+"11 - Exit");
        actionNum = scanner.nextLine();

        while(!(actionNum.equals("1") || actionNum.equals("2") || actionNum.equals("3") ||
                actionNum.equals("4") || actionNum.equals("5") || actionNum.equals("6")
                || actionNum.equals("7") || actionNum.equals("8")
                || actionNum.equals("9") || actionNum.equals("10") || actionNum.equals("11"))){
            System.out.println("Please choose the answer from 1,2,3,4,5,6,7,8,9,10,11");
            actionNum = scanner.nextLine().trim();
        }
        return actionNum;
    }

    /**
     *
     * @param question what question we want to ask the user
     * @param scanner reads the user answers
     * @param read - true if we want to read the file, false otherwise
     * @return file with given path
     */
    private static File getFile(String question, Scanner scanner, boolean read) throws IOException {
        System.out.println(question);
        String path = scanner.nextLine();
        File file = new File(path);
        if(!read)
            file.createNewFile();
        return file;
    }

    /**
     * does 3rd action from the list of actions
     * @param management OperatorFunctionality class object
     */
    private static void rentVehicle(Management management, Scanner scanner) throws IOException {
        Customer c = getCustomer(management, scanner);
        Vehicle r = getVehicle(management, scanner);
        LocalDate s = dateGetter(scanner, "starting");
        LocalDate e = dateGetter(scanner, "ending");
        while(s.isAfter(e)) {
            System.out.println("Invalid date! Please enter them again!");
            s = dateGetter(scanner, "starting");
            e = dateGetter(scanner, "ending");
        }
        Bill b = management.addVehicleRenting(s, e, c, r);
        if(b!=null){
            r.addVehicleRenting(new Renting(s,e,c));
            c.addVehicleRentingDate(new Renting(s,e,r));
            System.out.println("Do you want to save the bill in a file?(y/n)");
            String answer = scanner.nextLine().trim();
            while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
                System.out.println("Please answer y or n");
                answer = scanner.nextLine().trim();
            }
            if (answer.equalsIgnoreCase("y")) {
                System.out.println(
                        "\nPlease enter the name/path of the file in which you want to save the bill");
                String path = scanner.nextLine().trim();
                management.savingBill(b, path);
            }
        }
        else System.out.println("That vehicle is not available for that period of time");
    }

    /**
     * creates a vehicle object and returns it
     * @return vehicle object
     */

    private static Vehicle getVehicle(Management management, Scanner scanner) {
        printVehicles(management);
        System.out.print("Please enter vehicle ID: ");
        String vehicleID = scanner.nextLine().trim();
        while(!(vehicleID.matches("\\d+") && Integer.parseInt(vehicleID)<=Vehicle.getIdRep()
                && Integer.parseInt(vehicleID) > 0)){
            System.out.print("Please enter valid vehicle ID: ");
            vehicleID = scanner.nextLine().trim();
        }
        return management.findVehicleByID(Integer.parseInt(vehicleID));
    }

    /**
     * creates a customer object and returns it
     * @return customer object
     */

    private static Customer getCustomer(Management management, Scanner scanner) {
        printCustomers(management);
        System.out.print("Please enter customer ID: ");
        String customerID = scanner.nextLine().trim();
        while(!(customerID.matches("\\d+") && Integer.parseInt(customerID)<=Vehicle.getIdRep()
                && Integer.parseInt(customerID) > 0)){
            System.out.print("Please enter valid customer ID: ");
            customerID = scanner.nextLine().trim();
        }
        return management.findCustomerByID(Integer.parseInt(customerID));
    }

    /**
     * does 2nd action from the list of actions
     */

    private static void addCustomer(Scanner scanner, Management management) {
        Customer customer;
        String regexMail = "^(.+)@(.+)$";
        String regexPhone = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
        Pattern mailPattern = Pattern.compile(regexMail);
        Pattern phonePattern = Pattern.compile(regexPhone);
        System.out.print("Please enter customer's name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Please enter customer's email(if you do not want to share this info just enter '0')");
        String mail = scanner.nextLine().trim();
        Matcher matcherMail = mailPattern.matcher(mail);
        if (mail.equals("0"))
            System.out.println("Please enter customer's phone number(this is mandatory since you did not enter the mail)");
        else {
            while (!matcherMail.matches()) {
                System.out.println("Please enter valid email address");
                mail = scanner.nextLine().trim();
                matcherMail = mailPattern.matcher(mail);
            }
            System.out.println("Please enter customer's phone number(if you do not want to enter the phone number enter '0')");
        }
        String phone = scanner.nextLine().trim();
        Matcher matcherPhone = phonePattern.matcher(phone);
        System.out.println("Please also enter your driving license number");
        String license = "^[A-Z](?:\\d[- ]*){14}$";
        Pattern regexLicense = Pattern.compile(license);
        String lNumber = scanner.nextLine().trim();
        Matcher matcherLicense = regexLicense.matcher(lNumber);
        while(!matcherLicense.matches()){
            System.out.println("Please enter valid license address");
            lNumber = scanner.nextLine().trim();
            matcherLicense = regexLicense.matcher(lNumber);
        }
        if (!mail.equals("0") && phone.equals("0"))
            customer = new Customer(name, mail, null, lNumber);
        else {
            while (!matcherPhone.matches()) {
                System.out.println("Please enter valid phone number");
                phone = scanner.nextLine().trim();
                matcherPhone = phonePattern.matcher(phone);
            }
            if (!mail.equals("0"))
                customer = new Customer(name, mail, phone, lNumber);
            else
                customer = new Customer(name, null, phone, lNumber);

        }
        try {
            management.addCustomer(customer);
        } catch (IOException e) {
            System.err.println("addCustomer method, IOException");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("addCustomer method, ClassNotFoundException");
            throw new RuntimeException(e);
        }
    }

    /**
     * print vehicle objects
     */
    private static void printVehicles(Management management) {
        System.out.println("\n" + "==========================================================");
        for (Vehicle v:
                management.getVehicles()) {
            System.out.println(v);
        }
        System.out.println("==========================================================" + "\n");
    }

    /**
     * prints customer objects
     */
    private static void printCustomers(Management management) {
        System.out.println("\n" + "==========================================================");
        for (Customer customer:
                management.getCustomers()) {
            System.out.println(customer);
        }
        System.out.println("==========================================================" + "\n");
    }

    /**
     * does 1st action from the list of actions
     */
    private static void action1(Scanner scanner, Management operator) throws IOException {
        System.out.println("What type of vehicle do you want to add?" + "\n" +
                "(Please type ONLY the numbers that you can see in front of the given action)" + "\n" +
                "1 - Car" + "\n" + "2 - Bike" + "\n" + "3 - Van");
        String vehicleType = scanner.nextLine().trim();
        while (!(vehicleType.equals("1") || vehicleType.equals("2") || vehicleType.equals("3"))) {
            System.out.println("Please choose the answer from 1,2,3");
            vehicleType = scanner.nextLine().trim();
        }
        System.out.println("Please specify the brand of the vehicle");
        String brand = scanner.nextLine().trim();
        System.out.println("Also specify the model of the vehicle");
        String model = scanner.nextLine().trim();
        switch (vehicleType) {
            case "1":
                operator.addVehicle(new Car(brand,model));
                break;
            case "2":
                operator.addVehicle(new Bike(brand,model));
                break;
            case "3":
                operator.addVehicle(new Van(brand,model));
                break;
        }
    }

    /**
     * checks and returns date for booking
     * @param period - specifies if the date is the end or the start
     * @return created date
     */
    private static LocalDate dateGetter(Scanner scanner, String period) {
        System.out.println("This data is for "+period +" date of renting period");
        System.out.print("Please enter the year of your renting: ");
        String year = scanner.nextLine().trim();
        while(year.isEmpty()|| !year.matches("\\d+") || Calendar.getInstance().get(Calendar.YEAR) > Integer.parseInt(year)){
            System.out.print("Please enter valid year of your renting: ");
            year = scanner.nextLine().trim();
        }
        System.out.print("Please enter the month of your renting: ");
        String month = scanner.nextLine().trim();
        while(month.isEmpty()|| !month.matches("\\d+") || (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(year)
                && Integer.parseInt(month) < Calendar.getInstance().get(Calendar.MONTH))
                || !(month.matches("\\d+") && Integer.parseInt(month)<13 &&Integer.parseInt(month)>0)) {
            System.out.print("Please enter a valid month of your renting(1-12): ");
            month = scanner.nextLine().trim();
        }
        System.out.print("Please enter the day of your renting: ");
        String day = scanner.nextLine().trim();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        while(day.isEmpty() || !day.matches("\\d+") || (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(year)
                && Integer.parseInt(month) == (Calendar.getInstance().get(Calendar.MONTH)+1) &&
                Integer.parseInt(day) < Calendar.getInstance().get(Calendar.DATE))
                || !(month.matches("\\d+") && Integer.parseInt(day)<=numDays && Integer.parseInt(day)>0)) {
            System.out.print("Please enter a valid day of your renting: ");
            day = scanner.nextLine().trim();
        }
        return LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
    }
}
