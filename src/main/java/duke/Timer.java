package duke;

import java.util.ArrayList;
import java.util.Scanner;

public class Timer {
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        String logo = " _____                      \n"
                + "|_   _|(_) ______   ___   ___  \n"
                + "  | | | | '_ ` _ \\ / _ \\ '__| \n"
                + "  | | | | | | | | |  __/ |    \n"
                + "  |_| |_|_| |_| |_|\\___|_|    \n";

        System.out.println("Hello you can call me\n" + logo);
        printLine();
        System.out.println("Wassup! I'm Timer");
        System.out.println("How can I help you kind sir?");
        printLine();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    printWrapped("Bye. Hope to see you again soon!");
                    break;
                }

                if (input.equals("list")) {
                    printList(tasks);
                    continue;
                }

                if (input.equals("mark") || input.startsWith("mark ")) {
                    handleMark(tasks, input, true);
                    continue;
                }

                if (input.equals("unmark") || input.startsWith("unmark ")) {
                    handleMark(tasks, input, false);
                    continue;
                }

                if (input.equals("todo") || input.startsWith("todo ")) {
                    handleTodo(tasks, input);
                    continue;
                }

                if (input.equals("deadline") || input.startsWith("deadline ")) {
                    handleDeadline(tasks, input);
                    continue;
                }

                if (input.equals("event") || input.startsWith("event ")) {
                    handleEvent(tasks, input);
                    continue;
                }

                if (input.equals("delete") || input.startsWith("delete ")) {
                    handleDelete(tasks, input);
                    continue;
                }

                throw new DukeException("Sorry, I don't understand that command. "
                        + "Try: list, todo, deadline, event, delete, mark, unmark, bye.");

            } catch (DukeException e) {
                printWrapped(e.getMessage());
            }
        }
    }

    private static void printWrapped(String message) {
        printLine();
        System.out.println(message);
        printLine();
    }

    private static void handleMark(ArrayList<Task> tasks, String input, boolean markDone) throws DukeException {
        int index = parseIndex(input, markDone ? "mark" : "unmark");
        if (!isValidIndex(index, tasks.size())) {
            throw new DukeException("Please provide a valid task number (e.g., mark 1).");
        }

        if (markDone) {
            tasks.get(index).markDone();
            printLine();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(index));
            printLine();
        } else {
            tasks.get(index).unmarkDone();
            printLine();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.get(index));
            printLine();
        }
    }

    private static void handleTodo(ArrayList<Task> tasks, String input) throws DukeException {
        String description = input.length() > 4 ? input.substring(4).trim() : "";

        if (description.isEmpty()) {
            throw new DukeException("A todo needs a description. Example: todo read book");
        }

        Task task = new Todo(description);
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void handleDeadline(ArrayList<Task> tasks, String input) throws DukeException {
        String arguments = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = arguments.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DukeException("Deadline format: deadline <description> /by <by>");
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void handleEvent(ArrayList<Task> tasks, String input) throws DukeException {
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

        Task task = new Event(description, from, to);
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void handleDelete(ArrayList<Task> tasks, String input) throws DukeException {
        int index = parseIndex(input, "delete");
        if (!isValidIndex(index, tasks.size())) {
            throw new DukeException("Please provide a valid task number (e.g., delete 2).");
        }

        Task removedTask = tasks.remove(index);
        printLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
        printLine();
    }

    private static void printList(ArrayList<Task> tasks) {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        printLine();
    }

    private static void printTaskAdded(Task task, int taskCount) {
        printLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in the list.");
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static int parseIndex(String input, String commandWord) {
        String rest = input.length() > commandWord.length()
                ? input.substring(commandWord.length()).trim()
                : "";
        try {
            return Integer.parseInt(rest) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean isValidIndex(int index, int taskCount) {
        return index >= 0 && index < taskCount;
    }
}