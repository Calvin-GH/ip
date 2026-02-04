import java.util.Scanner;

public class Timer {
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        printLine();
        System.out.println("Hello! I'm Timer");
        System.out.println("What can I do for you?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[MAX_TASKS];
        boolean[] isDone = new boolean[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                printLine();
                System.out.println("Bye. Hope to see you again soon!");
                printLine();
                break;
            }

            if (input.equals("list")) {
                printLine();
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + formatTask(tasks[i], isDone[i]));
                }
                printLine();
                continue;
            }

            if (input.startsWith("mark ")) {
                int index = parseIndex(input, "mark ");
                if (isValidIndex(index, taskCount)) {
                    isDone[index] = true;
                    printLine();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + formatTask(tasks[index], true));
                    printLine();
                } else {
                    printLine();
                    System.out.println("Please give a valid task number to mark.");
                    printLine();
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                int index = parseIndex(input, "unmark ");
                if (isValidIndex(index, taskCount)) {
                    isDone[index] = false;
                    printLine();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + formatTask(tasks[index], false));
                    printLine();
                } else {
                    printLine();
                    System.out.println("Please give a valid task number to unmark.");
                    printLine();
                }
                continue;
            }

            if (taskCount < MAX_TASKS) {
                tasks[taskCount] = input;
                isDone[taskCount] = false;
                taskCount++;

                printLine();
                System.out.println("added: " + input);
                printLine();
            } else {
                printLine();
                System.out.println("Sorry, I can't add more tasks (limit reached).");
                printLine();
            }
        }
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static String formatTask(String description, boolean done) {
        return "[" + (done ? "X" : " ") + "] " + description;
    }

    private static int parseIndex(String input, String prefix) {
        try {
            String numberPart = input.substring(prefix.length()).trim();
            return Integer.parseInt(numberPart) - 1; // convert to 0-based index
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean isValidIndex(int index, int taskCount) {
        return index >= 0 && index < taskCount;
    }
}
