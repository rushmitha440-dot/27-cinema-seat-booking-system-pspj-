
import java.util.*;
import java.util.stream.Collectors;

public class CinemaSeatBookingSystem {

    public static void main(String[] args) {

        // Store all cinema seats
        List<Integer> seats = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            seats.add(i);
        }

        // Store booking details
        Map<Integer, String> bookings = new HashMap<>();

        // Store booked seat numbers without duplicates
        Set<Integer> bookedSeats = new HashSet<>();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CINEMA SEAT BOOKING SYSTEM ===");
            System.out.println("1. View Available Seats");
            System.out.println("2. Book a Seat");
            System.out.println("3. View Bookings");
            System.out.println("4. Booking Summary");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    List<Integer> availableSeats = seats.stream()
                        .filter(seat -> !bookedSeats.contains(seat))
                        .collect(Collectors.toList());

                    System.out.println("Available Seats: "
                        + availableSeats);
                    break;

                case 2:
                    System.out.print("Enter seat number (1-10): ");
                    int seat = sc.nextInt();
                    sc.nextLine();

                    if (!seats.contains(seat)) {
                        System.out.println("Invalid seat number!");
                    } else if (bookedSeats.contains(seat)) {
                        System.out.println("Seat already booked!");
                    } else {
                        System.out.print("Enter customer name: ");
                        String name = sc.nextLine();

                        bookings.put(seat, name);
                        bookedSeats.add(seat);

                        System.out.println(
                            "Seat booked successfully!");
                    }
                    break;

                case 3:
                    if (bookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        bookings.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(entry ->
                                System.out.println(
                                    "Seat " + entry.getKey()
                                    + " - " + entry.getValue()));
                    }
                    break;

                case 4:
                    long totalBooked = bookedSeats.stream().count();

                    List<Integer> available = seats.stream()
                        .filter(s -> !bookedSeats.contains(s))
                        .collect(Collectors.toList());

                    System.out.println("Total Seats: " + seats.size());
                    System.out.println("Booked Seats: " + totalBooked);
                    System.out.println("Available Seats: "
                        + available.size());

                    String report = bookings.values().stream()
                        .distinct()
                        .collect(Collectors.joining(", "));

                    System.out.println("Customers: "
                        + (report.isEmpty() ? "None" : report));
                    break;

                case 5:
                    System.out.print(
                        "Enter seat number to cancel: ");
                    int cancelSeat = sc.nextInt();
                    sc.nextLine();

                    Optional<String> customer =
                        Optional.ofNullable(bookings.get(cancelSeat));

                    if (customer.isPresent()) {
                        bookings.remove(cancelSeat);
                        bookedSeats.remove(cancelSeat);
                        System.out.println("Booking cancelled for "
                            + customer.get());
                    } else {
                        System.out.println("No booking found.");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using the system!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
