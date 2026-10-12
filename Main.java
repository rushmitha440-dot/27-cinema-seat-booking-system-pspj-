
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

// Module 4: Interface
interface Payable {
    double calculateTotal();
}

// Module 4: Custom exception
class SeatBookingException extends Exception {
    public SeatBookingException(String message) {
        super(message);
    }
}

// Module 4: Abstract class and encapsulation
abstract class Person {
    private String name;
    private String phone;

    public Person(String name, String phone) {
        setName(name);
        setPhone(phone);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()
                || name.contains("|")) {
            throw new IllegalArgumentException(
                "Invalid customer name.");
        }
        this.name = name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("[0-9]{10}")) {
            throw new IllegalArgumentException(
                "Enter a valid 10-digit phone number.");
        }
        this.phone = phone;
    }
}

// Module 4: Inheritance
class Customer extends Person {
    public Customer(String name, String phone) {
        super(name, phone);
    }
}

// Module 4: Class, object, interface and Comparable
class Booking implements Payable, Comparable<Booking> {

    private static final double TICKET_PRICE = 200.0;

    private final String bookingId;
    private final Customer customer;
    private final List<String> seats;

    // Parameterized constructor
    public Booking(String bookingId, Customer customer,
                   List<String> seats) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.seats = Collections.unmodifiableList(
            new ArrayList<>(seats));
    }

    // Constructor overloading
    public Booking(String bookingId, Customer customer,
                   String seat) {
        this(bookingId, customer, Arrays.asList(seat));
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<String> getSeats() {
        return seats;
    }

    public static double getTicketPrice() {
        return TICKET_PRICE;
    }

    @Override
    public double calculateTotal() {
        return seats.size() * TICKET_PRICE;
    }

    @Override
    public int compareTo(Booking other) {
        return bookingId.compareTo(other.bookingId);
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId
            + "\nCustomer: " + customer.getName()
            + "\nPhone: " + customer.getPhone()
            + "\nSeats: " + String.join(", ", seats)
            + "\nTotal Amount: Rs. " + calculateTotal();
    }

    // Module 5: Convert booking into a text record
    public String toRecord() {
        return customer.getName() + "|"
            + customer.getPhone() + "|"
            + String.join(";", seats) + "|"
            + calculateTotal() + "|"
            + bookingId;
    }

    // Module 5: Read and parse a text record
    public static Booking fromRecord(String line)
            throws SeatBookingException {

        String[] parts = line.split("\\|", -1);

        if (parts.length != 5) {
            throw new SeatBookingException(
                "Invalid saved booking record.");
        }

        try {
            Customer customer =
                new Customer(parts[0], parts[1]);

            List<String> seats = new ArrayList<>();

            for (String item : parts[2].split(";")) {
                String seat = item.trim().toUpperCase();

                // Correct class name: Main, not CinemaSystem
                if (!Main.isValidSeat(seat)
                        || seats.contains(seat)) {
                    throw new SeatBookingException(
                        "Invalid seat in saved record.");
                }

                seats.add(seat);
            }

            if (seats.isEmpty()) {
                throw new SeatBookingException(
                    "No seats in saved record.");
            }

            Booking booking =
                new Booking(parts[4], customer, seats);

            double savedTotal = Double.parseDouble(parts[3]);

            if (savedTotal != booking.calculateTotal()) {
                throw new SeatBookingException(
                    "Invalid booking amount.");
            }

            return booking;

        } catch (NumberFormatException e) {
            throw new SeatBookingException(
                "Invalid amount in saved record.");
        }
    }
}

// Main class
public class Main {

    private static final String MOVIE_NAME = "Cinema Movie";
    private static final String FILE_NAME = "bookings.txt";

    private static final int ROWS = 5;
    private static final int SEATS_PER_ROW = 5;

    private final Scanner sc = new Scanner(System.in);
    private final List<Booking> bookings = new ArrayList<>();
    private final Set<String> bookedSeats = new HashSet<>();

    private static int bookingCounter = 1;

    public static void main(String[] args) {
        Main cinema = new Main();
        cinema.loadBookings();
        cinema.run();
    }

    // Module 5: String validation
    public static boolean isValidSeat(String seat) {
        return seat != null
            && seat.matches("[A-E][1-5]");
    }

    // Main menu
    public void run() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== CINEMA SEAT BOOKING =====");
            System.out.println("Movie: " + MOVIE_NAME);
            System.out.println("Ticket Price: Rs. "
                + Booking.getTicketPrice());

            System.out.println("\n1. View Seats");
            System.out.println("2. Book Seats");
            System.out.println("3. View All Bookings");
            System.out.println("4. Search Booking");
            System.out.println("5. Sort Bookings");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice =
                    Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1:
                        displaySeats();
                        break;
                    case 2:
                        bookSeats();
                        break;
                    case 3:
                        displayBookings();
                        break;
                    case 4:
                        searchBooking();
                        break;
                    case 5:
                        sortBookings();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid menu number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                // Finally block
            }
        }

        sc.close();
    }

    // Display available and booked seats
    private void displaySeats() {
        System.out.println("\nO = Available, X = Booked");

        for (char row = 'A'; row <= 'E'; row++) {
            for (int number = 1; number <= 5; number++) {
                String seat = row + String.valueOf(number);

                if (bookedSeats.contains(seat)) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[O] ");
                }
            }
            System.out.println();
        }

        System.out.println("Seat names: A1 to E5");
    }

    // Book seats
    private void bookSeats() {
        try {
            System.out.print("Enter customer name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter 10-digit phone number: ");
            String phone = sc.nextLine().trim();

            Customer customer = new Customer(name, phone);

            displaySeats();

            System.out.print(
                "Enter seats separated by commas (example A1,A2): ");

            String input = sc.nextLine().trim().toUpperCase();
            String[] selectedSeats = input.split(",");

            List<String> seats = new ArrayList<>();

            for (String item : selectedSeats) {
                String seat = item.trim();

                if (!isValidSeat(seat)) {
                    throw new SeatBookingException(
                        "Invalid seat: " + seat);
                }

                if (seats.contains(seat)) {
                    throw new SeatBookingException(
                        "You entered the same seat twice.");
                }

                if (bookedSeats.contains(seat)) {
                    throw new SeatBookingException(
                        "Seat " + seat + " is already booked.");
                }

                seats.add(seat);
            }

            if (seats.isEmpty()) {
                throw new SeatBookingException(
                    "Select at least one seat.");
            }

            // Module 5: StringBuilder
            StringBuilder seatText = new StringBuilder();

            for (String seat : seats) {
                if (seatText.length() > 0) {
                    seatText.append(", ");
                }
                seatText.append(seat);
            }

            String bookingId =
                "BK" + System.currentTimeMillis()
                + bookingCounter++;

            Booking booking =
                new Booking(bookingId, customer, seats);

            // Save before confirming the booking
            saveBooking(booking);

            bookings.add(booking);
            bookedSeats.addAll(seats);

            System.out.println("\n===== BOOKING SUCCESSFUL =====");
            System.out.println(booking);
            System.out.println("Selected seats: " + seatText);
            System.out.println("==============================");

        } catch (SeatBookingException e) {
            System.out.println("Booking Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());

        } catch (IOException | SecurityException e) {
            System.out.println(
                "Unable to save booking. Please try again.");
        }
    }

    // Module 5: Append booking to file
    private void saveBooking(Booking booking) throws IOException {
        Path path = Paths.get(FILE_NAME);

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(booking.toRecord());
            writer.newLine();
        }
    }

    // Module 5: Load saved bookings
    private void loadBookings() {
        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Booking booking = Booking.fromRecord(line);

                    boolean duplicate = false;

                    for (String seat : booking.getSeats()) {
                        if (bookedSeats.contains(seat)) {
                            duplicate = true;
                            break;
                        }
                    }

                    if (duplicate) {
                        System.out.println(
                            "Skipped conflicting saved booking.");
                        continue;
                    }

                    bookings.add(booking);
                    bookedSeats.addAll(booking.getSeats());

                } catch (SeatBookingException
                        | IllegalArgumentException e) {
                    System.out.println(
                        "Skipped invalid saved booking.");
                }
            }

            System.out.println("Loaded bookings: "
                + bookings.size());

        } catch (IOException | SecurityException e) {
            System.out.println("Unable to read booking file.");
        }
    }

    // Display all bookings
    private void displayBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("\n--------------------");
            System.out.println(booking);
        }
    }

    // Search booking using its ID
    private void searchBooking() {
        System.out.print("Enter booking ID: ");
        String id = sc.nextLine().trim();

        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(id)) {
                System.out.println("\nBooking Found:");
                System.out.println(booking);
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    // Module 5: Sorting and searching algorithms
    private void sortBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings to sort.");
            return;
        }

        // Comparable: sort by booking ID
        Collections.sort(bookings);

        System.out.println("\nSorted by Booking ID:");

        for (Booking booking : bookings) {
            System.out.println(
                booking.getBookingId() + " - "
                + booking.getCustomer().getName());
        }

        // Comparator: sort by customer name
        bookings.sort(
            Comparator.comparing(
                b -> b.getCustomer().getName().toLowerCase()
            )
        );

        System.out.println("\nSorted by Customer Name:");

        for (Booking booking : bookings) {
            System.out.println(
                booking.getCustomer().getName() + " - "
                + booking.getBookingId());
        }

        // Arrays.sort and Arrays.binarySearch
        String[] seatNames = new String[25];
        int index = 0;

        for (char row = 'A'; row <= 'E'; row++) {
            for (int number = 1; number <= 5; number++) {
                seatNames[index++] =
                    row + String.valueOf(number);
            }
        }

        Arrays.sort(seatNames);

        int result = Arrays.binarySearch(seatNames, "A3");

        if (result >= 0) {
            System.out.println("\nSeat A3 exists in the seat list.");
        }
    }
}