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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                printLine();
                continue;
            }

            if (taskCount < MAX_TASKS) {
                tasks[taskCount] = input;
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
}
