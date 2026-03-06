package duke;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {

    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description Task description.
     * @param from Start time.
     * @param to End time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns event start time.
     *
     * @return start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns event end time.
     *
     * @return end time.
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}