# Timer User Guide

## Introduction

Timer is a command-line chatbot that helps users manage tasks.  
You can create, organize, search, and track tasks such as **todos, deadlines, and events**.

All tasks are automatically saved and will be loaded again when the program starts.

---

# Features

## Add a Todo

Adds a simple task to your task list.

Format:

todo DESCRIPTION


Example:

todo read book


Example Output:

[T][ ] read book


---

## Add a Deadline

Adds a task with a deadline.

Format:

deadline DESCRIPTION /by YYYY-MM-DD


Dates must follow the **yyyy-mm-dd** format.

Example:

deadline submit report /by 2026-03-20


Example Output:

[D][ ] submit report (by: Mar 20 2026)


---

## Add an Event

Adds a task that occurs during a specific time period.

Format:

event DESCRIPTION /from START /to END


Example:

event project meeting /from Monday 2pm /to 4pm


Example Output:

[E][ ] project meeting (from: Monday 2pm to: 4pm)


---

## List Tasks

Displays all tasks currently stored.

Format:

list


Example Output:

Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Mar 20 2026)
3.[E][ ] project meeting (from: Monday 2pm to: 4pm)


---

## Mark a Task

Marks a task as completed.

Format:

mark INDEX


Example:

mark 1


Example Output:

Nice! I've marked this task as done:
[T][X] read book


---

## Unmark a Task

Marks a task as not completed.

Format:

unmark INDEX


Example:

unmark 1


---

## Delete a Task

Removes a task from the list.

Format:

delete INDEX


Example:

delete 2


Example Output:

Noted. I've removed this task:
[D][ ] submit report (by: Mar 20 2026)


---

## Find Tasks

Searches for tasks containing a keyword.

Format:

find KEYWORD


Example:

find book


Example Output:

Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Jun 06 2026)


---

## Exit the Program

Closes the chatbot.

Format:

bye


Example Output:

Bye. Hope to see you again soon!


---

# Command Summary

| Command | Description |
|--------|-------------|
| `todo DESCRIPTION` | Add a todo task |
| `deadline DESCRIPTION /by YYYY-MM-DD` | Add a deadline task |
| `event DESCRIPTION /from START /to END` | Add an event |
| `list` | Show all tasks |
| `mark INDEX` | Mark task as done |
| `unmark INDEX` | Mark task as not done |
| `delete INDEX` | Delete a task |
| `find KEYWORD` | Search tasks |
| `bye` | Exit the program |

---

# Notes

- Task numbering starts from **1**.
- Tasks are automatically saved to `data/tasks.txt`.
- Deadlines must follow the **yyyy-mm-dd** format.