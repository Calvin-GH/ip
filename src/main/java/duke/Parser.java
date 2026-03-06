package duke;

import java.time.LocalDate;

public class Parser {
    public static int parseIndex(String input, String commandWord) {
        String rest = input.length() > commandWord.length()
                ? input.substring(commandWord.length()).trim()
                : "";
        try {
            return Integer.parseInt(rest) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static Todo parseTodo(String input) throws DukeException {
        String description = input.length() > 4 ? input.substring(4).trim() : "";
        if (description.isEmpty()) {
            throw new DukeException("A todo needs a description. Example: todo read book");
        }
        return new Todo(description);
    }

    public static Deadline parseDeadline(String input) throws DukeException {
        String arguments = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = arguments.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DukeException("Deadline format: deadline <description> /by <yyyy-mm-dd>");
        }

        try {
            LocalDate by = LocalDate.parse(parts[1].trim());
            return new Deadline(parts[0].trim(), by);
        } catch (Exception e) {
            throw new DukeException("Please enter the deadline in yyyy-mm-dd format.");
        }
    }

    public static Event parseEvent(String input) throws DukeException {
        String arguments = input.length() > 5 ? input.substring(5).trim() : "";

        int fromPos = arguments.indexOf(" /from ");
        int toPos = arguments.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new DukeException("Event format: event <description> /from <from> /to <to>");
        }

        String description = arguments.substring(0, fromPos).trim();
        String from = arguments.substring(fromPos + " /from ".length(), toPos).trim();
        String to = arguments.substring(toPos + " /to ".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DukeException("Event format: event <description> /from <from> /to <to>");
        }

        return new Event(description, from, to);
    }
}