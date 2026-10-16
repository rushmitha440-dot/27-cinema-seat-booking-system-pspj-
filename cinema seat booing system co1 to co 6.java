import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.IOException;

class CO6CinemaExample {

    public static void main(String[] args) {

        // Generics and ArrayList
        ArrayList<Booking> bookings = new ArrayList<>();

        bookings.add(new Booking(1, "Ravi", "A1", "Regular", 200));
        bookings.add(new Booking(2, "Asha", "A2", "Premium", 300));
        bookings.add(new Booking(3, "Ravi", "A3", "Regular", 200));

        // HashMap: search booking by ID
        HashMap<Integer, Booking> bookingMap = new HashMap<>();

        for (Booking b : bookings) {
            bookingMap.put(b.getId(), b);
        }

        System.out.println("Booking ID 2: " + bookingMap.get(2));

        // HashSet: unique occupied seats
        HashSet<String> occupiedSeats = new HashSet<>();

        for (Booking b : bookings) {
            occupiedSeats.add(b.getSeat());
        }

        System.out.println("Occupied seats: " + occupiedSeats);

        // TreeMap: bookings ordered by ID
        TreeMap<Integer, Booking> orderedBookings = new TreeMap<>();
        orderedBookings.putAll(bookingMap);

        System.out.println("Ordered bookings: " + orderedBookings);

        // For-each
        for (Booking b : bookings) {
            System.out.println(b);
        }

        // Iterator
        Iterator<Booking> iterator = bookings.iterator();

        while (iterator.hasNext()) {
            System.out.println("Iterator: " + iterator.next());
        }

        // Predicate: Premium ticket check
        Predicate<Booking> premium =
            b -> b.getType().equals("Premium");

        // Function: Booking to amount
        Function<Booking, Double> getAmount =
            Booking::getAmount;

        // Consumer: Print booking
        Consumer<Booking> printBooking =
            System.out::println;

        // Supplier: Default message
        Supplier<String> message =
            () -> "No booking information available";

        // Comparator: Sort by price
        bookings.sort(
            Comparator.comparingDouble(Booking::getAmount)
        );

        // Stream filter, map, sorted and collect
        List<String> premiumCustomers = bookings.stream()
            .filter(premium)
            .map(Booking::getName)
            .sorted()
            .distinct()
            .limit(10)
            .collect(Collectors.toList());

        System.out.println("Premium customers: " + premiumCustomers);

        // forEach and method reference
        bookings.stream().forEach(printBooking);

        // count and reduce
        long count = bookings.stream().count();

        double revenue = bookings.stream()
            .map(getAmount)
            .reduce(0.0, Double::sum);

        System.out.println("Booking count: " + count);
        System.out.println("Revenue: Rs. " + revenue);

        // groupingBy and counting
        Map<String, Long> ticketCounts = bookings.stream()
            .collect(Collectors.groupingBy(
                Booking::getType,
                Collectors.counting()
            ));

        System.out.println("Ticket counts: " + ticketCounts);

        // joining
        String names = bookings.stream()
            .map(Booking::getName)
            .distinct()
            .collect(Collectors.joining(", "));

        System.out.println("Customers: " + names);

        // Optional: safe search
        Optional<Booking> found = bookings.stream()
            .filter(b -> b.getId() == 99)
            .findFirst();

        System.out.println(
            found.map(Booking::getName).orElse(message.get())
        );

        // Imperative aggregation
        double loopRevenue = 0;

        for (Booking b : bookings) {
            loopRevenue += b.getAmount();
        }

        // Functional aggregation
        double streamRevenue = bookings.stream()
            .mapToDouble(Booking::getAmount)
            .sum();

        System.out.println("Loop revenue: " + loopRevenue);
        System.out.println("Stream revenue: " + streamRevenue);
        
        
// Read file lines as a stream
        try (Stream<String> lines =
                 Files.lines(Paths.get("bookings.txt"))) {

            lines.filter(line -> !line.isBlank())
                 .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("Booking file not found.");
        }
    }
}

class Booking {

    private int id;
    private String name;
    private String seat;
    private String type;
    private double amount;

    public Booking(int id, String name, String seat,
                   String type, double amount) {
        this.id = id;
        this.name = name;
        this.seat = seat;
        this.type = type;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSeat() {
        return seat;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + seat +
               " | " + type + " | Rs. " + amount;
    }
}