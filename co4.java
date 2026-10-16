 import java.util.Scanner;

// CO4: Class and Object
class Cinema {
    private String customerName;
    private String seatNumber;
    private double ticketPrice;

    // CO4: Constructor
    Cinema(String customerName, String seatNumber, double ticketPrice) {
        this.customerName = customerName;
        this.seatNumber = seatNumber;
        this.ticketPrice = ticketPrice;
    }

    // CO4: Getter methods
    public String getCustomerName() {
        return customerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    // CO4: Method
    public void displayBooking() {
        System.out.println("\n--- Booking Details ---");
        System.out.println("Customer Name: " + customerName);
        System.out.println("Seat Number: " + seatNumber);
        System.out.println("Ticket Price: Rs. " + ticketPrice);
    }
}

// CO4: Inheritance
class PremiumCinema extends Cinema {
    private double extraCharge;

    PremiumCinema(String name, String seat, double price,
                  double extraCharge) {
        super(name, seat, price);
        this.extraCharge = extraCharge;
    }

    // CO4: Method overriding
    @Override
    public void displayBooking() {
        super.displayBooking();
        System.out.println("Premium Charge: Rs. " + extraCharge);
        System.out.println("Total Amount: Rs. "
                + (getTicketPrice() + extraCharge));
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("=== Cinema Seat Booking System ===");

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter seat number (A1-A10): ");
            String seat = sc.nextLine().toUpperCase();

            // CO4: Validation and exception handling
            if (!seat.matches("A([1-9]|10)")) {
                throw new IllegalArgumentException(
                        "Invalid seat number!");
            }

            System.out.println("1. Regular Seat - Rs. 200");
            System.out.println("2. Premium Seat - Rs. 300");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            Cinema booking;

            if (choice == 1) {
                booking = new Cinema(name, seat, 200);
                booking.displayBooking();
            } else if (choice == 2) {
                booking = new PremiumCinema(name, seat, 300, 50);
                booking.displayBooking();
            } else {
                throw new IllegalArgumentException(
                        "Invalid ticket choice!");
            }

            System.out.println("\nBooking successful!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        } finally {
            System.out.println("Thank you for using our system!");
            sc.close();
        }
    }
}