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

        System.out.println("Hello from\n" + logo);

        printLine();
        System.out.println("Hello! I'm Timer");
        System.out.println("What can I do for you?");
        printLine();
        System.out.println("Hello! I'm Timer");
        System.out.println("What can I do for you?");
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
                String desc = input.substring("todo ".length()).trim();
                if (desc.isEmpty()) {
                    printLine();
                    System.out.println("Please provide a description for a todo.");
                    printLine();
                    continue;
                }
                if (taskCount >= MAX_TASKS) {
                    printFullMessage();
                    continue;
                }

                Task t = new Todo(desc);
                tasks[taskCount++] = t;
                printTaskAdded(t, taskCount);
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring("deadline ".length()).trim();
                String[] parts = rest.split(" /by ", 2); // split into at most 2 parts

                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    printLine();
                    System.out.println("Please use: deadline <description> /by <by>");
                    printLine();
                    continue;
                }
                if (taskCount >= MAX_TASKS) {
                    printFullMessage();
                    continue;
                }

                Task t = new Deadline(parts[0].trim(), parts[1].trim());
                tasks[taskCount++] = t;
                printTaskAdded(t, taskCount);
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring("event ".length()).trim();

                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");

                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    printLine();
                    System.out.println("Please use: event <description> /from <from> /to <to>");
                    printLine();
                    continue;
                }

                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + " /from ".length(), toPos).trim();
                String to = rest.substring(toPos + " /to ".length()).trim();

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    printLine();
                    System.out.println("Please use: event <description> /from <from> /to <to>");
                    printLine();
                    continue;
                }
                if (taskCount >= MAX_TASKS) {
                    printFullMessage();
                    continue;
                }

                Task t = new Event(desc, from, to);
                tasks[taskCount++] = t;
                printTaskAdded(t, taskCount);
                continue;
            }

            printLine();
            System.out.println("Sorry, I don't understand that command yet.");
            printLine();
        }
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
