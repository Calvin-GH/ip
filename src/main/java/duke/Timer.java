package duke;

public class Timer {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

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
                    throw new DukeException("Sorry, I don't understand that command. "
                            + "Try: list, todo, deadline, event, delete, find, mark, unmark, bye.");
                }
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleMark(String input, boolean markDone) throws DukeException {
        int index = Parser.parseIndex(input, markDone ? "mark" : "unmark");
        if (!tasks.isValidIndex(index)) {
            throw new DukeException("Please provide a valid task number (e.g., mark 1).");
        }

        Task task = tasks.get(index);
        if (markDone) {
            task.markDone();
            storage.save(tasks.getTasks());
            ui.showMarked(task);
        } else {
            task.unmarkDone();
            storage.save(tasks.getTasks());
            ui.showUnmarked(task);
        }
    }

    private void handleDelete(String input) throws DukeException {
        int index = Parser.parseIndex(input, "delete");
        if (!tasks.isValidIndex(index)) {
            throw new DukeException("Please provide a valid task number (e.g., delete 2).");
        }

        Task removedTask = tasks.delete(index);
        storage.save(tasks.getTasks());
        ui.showDeleted(removedTask, tasks.size());
    }

    private void handleFind(String input) throws DukeException {
        String keyword = input.length() > 4 ? input.substring(4).trim() : "";
        if (keyword.isEmpty()) {
            throw new DukeException("Please provide a keyword to find.");
        }

        ui.showFindResults(tasks.find(keyword));
    }

    public static void main(String[] args) {
        new Timer("data/tasks.txt").run();
    }
}