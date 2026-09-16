import java.util.*;

class Room {
    private int roomNumber;
    private String category; // Standard, Deluxe, Suite
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

class Reservation {
    private String bookingId;
    private String guestName;
    private Room room;
    private int nights;
    private double totalAmount;

    public Reservation(String guestName, Room room, int nights) {
        this.bookingId = "RES" + (1000 + new Random().nextInt(9000));
        this.guestName = guestName;
        this.room = room;
        this.nights = nights;
        this.totalAmount = room.getPricePerNight() * nights;
    }

    public String getBookingId() { return bookingId; }
    public Room getRoom() { return room; }

    public void displayDetails() {
        System.out.println("\n================ BOOKING CONFIRMATION ================");
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Guest Name  : " + guestName);
        System.out.println("Room Number : " + room.getRoomNumber() + " (" + room.getCategory() + ")");
        System.out.println("Nights      : " + nights);
        System.out.printf("Total Paid  : $%.2f\n", totalAmount);
        System.out.println("Status      : CONFIRMED");
        System.out.println("======================================================");
    }
}

public class HotelReservationSystem {
    private static List<Room> rooms = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        initializeRooms();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==================================================");
        System.out.println("   WELCOME TO CODEALPHA HOTEL RESERVATION SYSTEM  ");
        System.out.println("==================================================");

        while (running) {
            System.out.println("\n1. Search Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> searchRooms();
                case 2 -> makeReservation(scanner);
                case 3 -> cancelReservation(scanner);
                case 4 -> viewBookings();
                case 5 -> {
                    running = false;
                    System.out.println("Thank you for using the Hotel Reservation System!");
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 80.00));
        rooms.add(new Room(102, "Standard", 80.00));
        rooms.add(new Room(201, "Deluxe", 150.00));
        rooms.add(new Room(202, "Deluxe", 150.00));
        rooms.add(new Room(301, "Suite", 300.00));
    }

    private static void searchRooms() {
        System.out.println("\n--- AVAILABLE ROOMS ---");
        System.out.printf("%-10s %-15s %-10s\n", "Room No", "Category", "Price/Night");
        System.out.println("----------------------------------------");
        boolean availableFound = false;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.printf("%-10d %-15s $%-9.2f\n", room.getRoomNumber(), room.getCategory(), room.getPricePerNight());
                availableFound = true;
            }
        }
        if (!availableFound) {
            System.out.println("No rooms currently available.");
        }
    }

    private static void makeReservation(Scanner scanner) {
        searchRooms();
        System.out.print("\nEnter Room Number to book: ");
        int roomNum = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNum && room.isAvailable()) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("❌ Invalid or unavailable room number.");
            return;
        }

        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Number of Nights: ");
        int nights = scanner.nextInt();

        double totalCost = selectedRoom.getPricePerNight() * nights;
        System.out.printf("Total cost is $%.2f. Processing payment...\n", totalCost);
        System.out.println("💳 Payment Successful!");

        selectedRoom.setAvailable(false);
        Reservation reservation = new Reservation(name, selectedRoom, nights);
        reservations.add(reservation);
        reservation.displayDetails();
    }

    private static void cancelReservation(Scanner scanner) {
        System.out.print("\nEnter Booking ID to cancel: ");
        String bookingId = scanner.next().trim();

        Reservation targetReservation = null;
        for (Reservation res : reservations) {
            if (res.getBookingId().equalsIgnoreCase(bookingId)) {
                targetReservation = res;
                break;
            }
        }

        if (targetReservation == null) {
            System.out.println("❌ Booking ID not found.");
            return;
        }

        targetReservation.getRoom().setAvailable(true);
        reservations.remove(targetReservation);
        System.out.println("✅ Reservation " + bookingId + " successfully cancelled and refund processed.");
    }

    private static void viewBookings() {
        if (reservations.isEmpty()) {
            System.out.println("\nNo active reservations found.");
            return;
        }
        for (Reservation res : reservations) {
            res.displayDetails();
        }
    }
}