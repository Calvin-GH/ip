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
        System.out.println("Wassup! I'm Timer");
        System.out.println("How can I help you kind sir?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                printLine();
                System.out.println("Bye. Hope to see you again soon!");
                printLine();
                break;
            }

            if (input.equals("list")) {
                printList(tasks, taskCount);
                continue;
            }

            if (input.startsWith("mark ")) {
                handleMark(tasks, taskCount, input, true);
                continue;
            }

            if (input.startsWith("unmark ")) {
                handleMark(tasks, taskCount, input, false);
                continue;
            }

            if (input.startsWith("todo ")) {
                taskCount = handleTodo(tasks, taskCount, input);
                continue;
            }

            if (input.startsWith("deadline ")) {
                taskCount = handleDeadline(tasks, taskCount, input);
                continue;
            }

            if (input.startsWith("event ")) {
                taskCount = handleEvent(tasks, taskCount, input);
                continue;
            }

            printWrapped("Sorry, I don't understand that command yet.");
        }
    }

    private static void printWrapped(String message) {
        printLine();
        System.out.println(message);
        printLine();
    }

    private static void handleMark(Task[] tasks, int taskCount, String input, boolean markDone) {
        int index = parseIndex(input, markDone ? "mark " : "unmark ");
        if (!isValidIndex(index, taskCount)) {
            printLine();
            System.out.println("Please give a valid task number.");
            printLine();
            return;
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

    private static int handleTodo(Task[] tasks, int taskCount, String input) {
        String description = input.substring("todo ".length()).trim();
        if (description.isEmpty()) {
            printLine();
            System.out.println("Please provide a description for a todo.");
            printLine();
            return taskCount;
        }
        if (taskCount >= MAX_TASKS) {
            printFullMessage();
            return taskCount;
        }
        Task task = new Todo(description);
        tasks[taskCount++] = task;
        printTaskAdded(task, taskCount);
        return taskCount;
    }

    private static int handleDeadline(Task[] tasks, int taskCount, String input) {
        String arguments = input.substring("deadline ".length()).trim();
        String[] parts = arguments.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            printLine();
            System.out.println("Please use: deadline <description> /by <by>");
            printLine();
            return taskCount;
        }
        if (taskCount >= MAX_TASKS) {
            printFullMessage();
            return taskCount;
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks[taskCount++] = task;
        printTaskAdded(task, taskCount);
        return taskCount;
    }

    private static int handleEvent(Task[] tasks, int taskCount, String input) {
        String arguments = input.substring("event ".length()).trim();

        int fromPos = arguments.indexOf(" /from ");
        int toPos = arguments.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            printLine();
            System.out.println("Please use: event <description> /from <from> /to <to>");
            printLine();
            return taskCount;
        }

        String description = arguments.substring(0, fromPos).trim();
        String from = arguments.substring(fromPos + " /from ".length(), toPos).trim();
        String to = arguments.substring(toPos + " /to ".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            printLine();
            System.out.println("Please use: event <description> /from <from> /to <to>");
            printLine();
            return taskCount;
        }
        if (taskCount >= MAX_TASKS) {
            printFullMessage();
            return taskCount;
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

    private static void printFullMessage() {
        printLine();
        System.out.println("Sorry, I can't add more tasks (limit reached).");
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static int parseIndex(String input, String prefix) {
        try {
            return Integer.parseInt(input.substring(prefix.length()).trim()) - 1; // 1-based -> 0-based
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean isValidIndex(int index, int taskCount) {
        return index >= 0 && index < taskCount;
    }
}
