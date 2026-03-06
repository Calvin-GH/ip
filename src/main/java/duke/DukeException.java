package duke;

/**
 * Represents an exception specific to the Timer chatbot.
 */
public class DukeException extends Exception {

    /**
     * Creates a DukeException with a message.
     *
     * @param message Error message.
     */
    public DukeException(String message) {
        super(message);
    }
}