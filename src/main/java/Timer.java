import java.util.Scanner;

public class Timer {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    // ---------- Main ----------
    public static void main(String[] args) {
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
                printLine();
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                printLine();
                continue;
            }

            if (input.startsWith("mark ")) {
                int index = parseIndex(input, "mark ");
                if (isValidIndex(index, taskCount)) {
                    tasks[index].markDone();
                    printLine();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[index]);
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
                    tasks[index].unmarkDone();
                    printLine();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[index]);
                    printLine();
                } else {
                    printLine();
                    System.out.println("Please give a valid task number to unmark.");
                    printLine();
                }
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
                    printLine();
                    System.out.println("Sorry, I can't add more tasks (limit reached).");
                    printLine();
                    continue;
                }

                Task t = new Todo(desc);
                tasks[taskCount] = t;
                taskCount++;

                printLine();
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                printLine();
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring("deadline ".length()).trim();
                int byPos = rest.indexOf(" /by ");
                if (byPos == -1) {
                    printLine();
                    System.out.println("Please use: deadline <description> /by <by>");
                    printLine();
                    continue;
                }

                String desc = rest.substring(0, byPos).trim();
                String by = rest.substring(byPos + " /by ".length()).trim();

                if (desc.isEmpty() || by.isEmpty()) {
                    printLine();
                    System.out.println("Please provide both description and /by for a deadline.");
                    printLine();
                    continue;
                }

                if (taskCount >= MAX_TASKS) {
                    printLine();
                    System.out.println("Sorry, I can't add more tasks (limit reached).");
                    printLine();
                    continue;
                }

                Task t = new Deadline(desc, by);
                tasks[taskCount] = t;
                taskCount++;

                printLine();
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                printLine();
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
                    System.out.println("Please provide description, /from and /to for an event.");
                    printLine();
                    continue;
                }

                if (taskCount >= MAX_TASKS) {
                    printLine();
                    System.out.println("Sorry, I can't add more tasks (limit reached).");
                    printLine();
                    continue;
                }

                Task t = new Event(desc, from, to);
                tasks[taskCount] = t;
                taskCount++;

                printLine();
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                printLine();
                continue;
            }

            printLine();
            System.out.println("Sorry, I don't understand that command yet.");
            printLine();
        }
    }

    // ---------- Helpers ----------
    private static void printLine() {
        System.out.println(LINE);
    }

    private static int parseIndex(String input, String prefix) {
        try {
            return Integer.parseInt(input.substring(prefix.length()).trim()) - 1; // 1-based to 0-based
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean isValidIndex(int index, int taskCount) {
        return index >= 0 && index < taskCount;
    }

    // ---------- Inheritance Model ----------
    private static class Task {
        protected String description;
        protected boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public void markDone() {
            isDone = true;
        }

        public void unmarkDone() {
            isDone = false;
        }

        protected String getStatusIcon() {
            return isDone ? "X" : " ";
        }

        @Override
        public String toString() {
            return "[" + getStatusIcon() + "] " + description;
        }
    }

    private static class Todo extends Task {
        public Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    private static class Deadline extends Task {
        protected String by;

        public Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
    }

    private static class Event extends Task {
        protected String from;
        protected String to;

        public Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
        }
    }
}
