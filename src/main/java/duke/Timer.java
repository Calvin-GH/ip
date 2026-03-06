package duke;

/**
 * Main entry point of the Timer chatbot application.
 * Coordinates the UI, task list, parser, and storage components.
 */
public class Timer {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Timer chatbot that loads tasks from the given file path.
     *
     * @param filePath Path to the file used for storing tasks.
     */
    public Timer(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Runs the chatbot until the user exits.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();

                if (input.equals("bye")) {
                    ui.showGoodbye();
                    isExit = true;
                } else if (input.equals("list")) {
                    ui.showTaskList(tasks.getTasks());
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    handleMark(input, true);
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    handleMark(input, false);
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    Task task = Parser.parseTodo(input);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTaskAdded(task, tasks.size());
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    Task task = Parser.parseDeadline(input);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTaskAdded(task, tasks.size());
                } else if (input.equals("event") || input.startsWith("event ")) {
                    Task task = Parser.parseEvent(input);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTaskAdded(task, tasks.size());
                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    handleDelete(input);
                } else if (input.equals("find") || input.startsWith("find ")) {
                    handleFind(input);
                } else {
                    throw new DukeException("Sorry, I don't understand that command.");
                }

            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Handles mark and unmark commands.
     *
     * @param input Full user command.
     * @param markDone Whether the task should be marked done.
     * @throws DukeException If the index is invalid.
     */
    private void handleMark(String input, boolean markDone) throws DukeException {
        int index = Parser.parseIndex(input, markDone ? "mark" : "unmark");

        if (!tasks.isValidIndex(index)) {
            throw new DukeException("Please provide a valid task number.");
        }

        Task task = tasks.get(index);

        if (markDone) {
            task.markDone();
            ui.showMarked(task);
        } else {
            task.unmarkDone();
            ui.showUnmarked(task);
        }

        storage.save(tasks.getTasks());
    }

    /**
     * Handles deleting tasks.
     *
     * @param input User command.
     * @throws DukeException If index is invalid.
     */
    private void handleDelete(String input) throws DukeException {
        int index = Parser.parseIndex(input, "delete");

        if (!tasks.isValidIndex(index)) {
            throw new DukeException("Please provide a valid task number.");
        }

        Task removedTask = tasks.delete(index);
        storage.save(tasks.getTasks());
        ui.showDeleted(removedTask, tasks.size());
    }

    /**
     * Handles find command.
     *
     * @param input User command containing keyword.
     * @throws DukeException If keyword is missing.
     */
    private void handleFind(String input) throws DukeException {
        String keyword = input.substring(4).trim();

        if (keyword.isEmpty()) {
            throw new DukeException("Please provide a keyword to find.");
        }

        ui.showFindResults(tasks.find(keyword));
    }

    /**
     * Starts the Timer chatbot.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Timer("data/tasks.txt").run();
    }
}