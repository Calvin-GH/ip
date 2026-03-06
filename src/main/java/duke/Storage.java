package duke;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "tasks.txt");

    public static ArrayList<Task> load() throws DukeException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            createFileIfMissing();

            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(parseTask(line));
            }
        } catch (IOException e) {
            throw new DukeException("Unable to load tasks from file.");
        }

        return tasks;
    }

    public static void save(ArrayList<Task> tasks) throws DukeException {
        try {
            createFileIfMissing();

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }

            Files.write(FILE_PATH, lines);
        } catch (IOException e) {
            throw new DukeException("Unable to save tasks to file.");
        }
    }

    private static void createFileIfMissing() throws IOException {
        if (FILE_PATH.getParent() != null) {
            Files.createDirectories(FILE_PATH.getParent());
        }
        if (!Files.exists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
        }
    }

    private static Task parseTask(String line) throws DukeException {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new DukeException("Corrupted task data: " + line);
        }

        String type = parts[0];
        String status = parts[1];
        String description = parts[2];

        Task task;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new DukeException("Corrupted deadline data: " + line);
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new DukeException("Corrupted event data: " + line);
            }
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            throw new DukeException("Unknown task type in file: " + line);
        }

        if (status.equals("1")) {
            task.markDone();
        } else if (!status.equals("0")) {
            throw new DukeException("Invalid task status in file: " + line);
        }

        return task;
    }

    private static String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + status + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + status + " | " + deadline.getDescription() + " | " + deadline.getBy();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + status + " | " + event.getDescription() + " | " + event.getFrom() + " | " + event.getTo();
        }

        return "";
    }
}