public import java.util.*;

class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventLocation;

    public Event(String eventName, String eventDate, String eventTime, String eventLocation) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    @Override
    public String toString() {
        return "Event Name: " + eventName + "\nDate: " + eventDate + "\nTime: " + eventTime + "\nLocation: " + eventLocation;
    }
}

class EventScheduler {
    private List<Event> eventList;

    public EventScheduler() {
        this.eventList = new ArrayList<>();
    }

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public void removeEvent(String eventName) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getEventName().equalsIgnoreCase(eventName)) {
                eventList.remove(i);
                System.out.println("Event removed successfully!");
                return;
            }
        }
        System.out.println("Event not found!");
    }

    public void updateEvent(String eventName, String newDate, String newTime, String newLocation) {
        for (Event event : eventList) {
            if (event.getEventName().equalsIgnoreCase(eventName)) {
                event.setEventDate(newDate);
                event.setEventTime(newTime);
                event.setEventLocation(newLocation);
                System.out.println("Event updated successfully!");
                return;
            }
        }
        System.out.println("Event not found!");
    }

    public void displayEvents() {
        if (eventList.isEmpty()) {
            System.out.println("No events scheduled.");
        } else {
            for (Event event : eventList) {
                System.out.println(event);
                System.out.println("event");
            }
        }
    }

    public void searchEvent(String eventName) {
        for (Event event : eventList) {
            if (event.getEventName().equalsIgnoreCase(eventName)) {
                System.out.println(event);
                return;
            }
        }
        System.out.println("Event not found!");
    }
}

public class EventSchedulerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EventScheduler scheduler = new EventScheduler();

        int choice;
        do {
            System.out.println("\nEvent Scheduler Menu:");
            System.out.println("1. Add Event");
            System.out.println("2. Remove Event");
            System.out.println("3. Update Event");
            System.out.println("4. Display All Events");
            System.out.println("5. Search Event");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter event name: ");
                    String eventName = scanner.nextLine();
                    System.out.print("Enter event date (YYYY-MM-DD): ");
                    String eventDate = scanner.nextLine();
                    System.out.print("Enter event time (HH:MM): ");
                    String eventTime = scanner.nextLine();
                    System.out.print("Enter event location: ");
                    String eventLocation = scanner.nextLine();
                    scheduler.addEvent(new Event(eventName, eventDate, eventTime, eventLocation));
                    break;

                case 2:
                    System.out.print("Enter the event name to remove: ");
                    String removeEventName = scanner.nextLine();
                    scheduler.removeEvent(removeEventName);
                    break;

                case 3:
                    System.out.print("Enter the event name to update: ");
                    String updateEventName = scanner.nextLine();
                    System.out.print("Enter new event date (YYYY-MM-DD): ");
                    String newDate = scanner.nextLine();
                    System.out.print("Enter new event time (HH:MM): ");
                    String newTime = scanner.nextLine();
                    System.out.print("Enter new event location: ");
                    String newLocation = scanner.nextLine();
                    scheduler.updateEvent(updateEventName, newDate, newTime, newLocation);
                    break;

                case 4:
                    scheduler.displayEvents();
                    break;

                case 5:
                    System.out.print("Enter the event name to search: ");
                    String searchEventName = scanner.nextLine();
                    scheduler.searchEvent(searchEventName);
                    break;

                case 6:
                    System.out.println("Exiting the application...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}
 
    

