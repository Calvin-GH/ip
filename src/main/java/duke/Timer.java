package duke;

import java.util.Scanner;

public class Timer {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String logo = " _____                      \n"
                + "|_   _|(_) ______   ___   ___  \n"
                + "  | | | | '_ ` _ \\ / _ \\ '__| \n"
                + "  | | | | | | | | |  __/ |    \n"
                + "  |_| |_|_| |_| |_|\\___|_|    \n";

        System.out.println("Hello you can call me\n" + logo);
        printLine();
        System.out.println("Wassup! I'm duke.Timer");
        System.out.println("How can I help you kind sir?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    printWrapped("Bye. Hope to see you again soon!");
                    break;
                }

                if (input.equals("list")) {
                    printList(tasks, taskCount);
                    continue;
                }

                if (input.equals("mark") || input.startsWith("mark ")) {
                    handleMark(tasks, taskCount, input, true);
                    continue;
                }

                if (input.equals("unmark") || input.startsWith("unmark ")) {
                    handleMark(tasks, taskCount, input, false);
                    continue;
                }

                if (input.equals("todo") || input.startsWith("todo ")) {
                    taskCount = handleTodo(tasks, taskCount, input);
                    continue;
                }

                if (input.equals("deadline") || input.startsWith("deadline ")) {
                    taskCount = handleDeadline(tasks, taskCount, input);
                    continue;
                }

                if (input.equals("event") || input.startsWith("event ")) {
                    taskCount = handleEvent(tasks, taskCount, input);
                    continue;
                }

                throw new DukeException("Sorry, I don't understand that command. "
                        + "Try: list, todo, deadline, event, mark, unmark, bye.");

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

    private static void handleMark(Task[] tasks, int taskCount, String input, boolean markDone) throws DukeException {
        int index = parseIndex(input, markDone ? "mark" : "unmark");
        if (!isValidIndex(index, taskCount)) {
            throw new DukeException("Please provide a valid task number (e.g., mark 1).");
        }

        if (markDone) {
            tasks[index].markDone();
            printLine();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[index]);
            printLine();
        } else {
            tasks[index].unmarkDone();
            printLine();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[index]);
            printLine();
        }
    }

    private static int handleTodo(Task[] tasks, int taskCount, String input) throws DukeException {
        String description = input.length() > 4 ? input.substring(4).trim() : "";

        if (description.isEmpty()) {
            throw new DukeException("A todo needs a description. Example: todo read book");
        }
        if (taskCount >= MAX_TASKS) {
            throw new DukeException("duke.Task limit reached. Please delete a task before adding more.");
        }

        Task task = new Todo(description);
        tasks[taskCount++] = task;
        printTaskAdded(task, taskCount);
        return taskCount;
    }

    private static int handleDeadline(Task[] tasks, int taskCount, String input) throws DukeException {
        String arguments = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = arguments.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DukeException("duke.Deadline format: deadline <description> /by <by>");
        }
        if (taskCount >= MAX_TASKS) {
            throw new DukeException("duke.Task limit reached. Please delete a task before adding more.");
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks[taskCount++] = task;
        printTaskAdded(task, taskCount);
        return taskCount;
    }

    private static int handleEvent(Task[] tasks, int taskCount, String input) throws DukeException {
        String arguments = input.length() > 5 ? input.substring(5).trim() : "";

        int fromPos = arguments.indexOf(" /from ");
        int toPos = arguments.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new DukeException("duke.Event format: event <description> /from <from> /to <to>");
        }

        String description = arguments.substring(0, fromPos).trim();
        String from = arguments.substring(fromPos + " /from ".length(), toPos).trim();
        String to = arguments.substring(toPos + " /to ".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DukeException("duke.Event format: event <description> /from <from> /to <to>");
        }
        if (taskCount >= MAX_TASKS) {
            throw new DukeException("duke.Task limit reached. Please delete a task before adding more.");
        }

        Task task = new Event(description, from, to);
        tasks[taskCount++] = task;
        printTaskAdded(task, taskCount);
        return taskCount;
    }

    private static void printList(Task[] tasks, int taskCount) {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
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
        String rest = input.length() > commandWord.length() ? input.substring(commandWord.length()).trim() : "";
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
