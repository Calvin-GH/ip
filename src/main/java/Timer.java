import java.util.Scanner;

public class Timer {
    public static void main(String[] args) {
        printLine();
        System.out.println("Hello! I'm Timer");
        System.out.println("What can I do for you?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                printLine();
                System.out.println("Bye. Hope to see you again soon!");
                printLine();
                break;
            }

            printLine();
            System.out.println(input);
            printLine();
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
