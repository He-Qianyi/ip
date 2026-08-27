# UI Test Plan

## Level 7: Persistent Storage

### First launch creates storage

* Aim: Verify that Lynn can start on a new computer without an existing data folder or file.
* Inputs: `bye`
* Expected output: Lynn greets the user and exits normally. A `data/lynn.txt` file is created in the working directory.

### Save and reload tasks

* Aim: Verify that all task types and their completion state survive a restart.
* First-run inputs:
  ```text
  todo read book
  deadline return book /by 2026-09-01
  event team meeting /from Mon 2pm /to 4pm
  mark 2
  delete 3
  bye
  ```
* Second-run inputs:
  ```text
  list
  bye
  ```
* Expected output after `list`:
  ```text
  Here are the tasks in your list:
  1.[T][ ] read book
  2.[D][X] return book (by: Sep 1 2026)
  ```

## Level 8: Deadline Dates

### Parse and format a deadline date

* Aim: Verify that a deadline is stored as a date rather than unprocessed text.
* Inputs: `deadline submit report /by 2026-10-15`, followed by `list`.
* Expected output: The task is displayed as `[D][ ] submit report (by: Oct 15 2026)`.

### Reject an invalid deadline date

* Aim: Verify that malformed date input produces a helpful error without adding a task.
* Inputs: `deadline submit report /by next Thursday`, followed by `list`.
* Expected output: Lynn asks for a `yyyy-MM-dd` date, and `list` does not contain `submit report`.

### Descriptions containing separators

* Aim: Verify that task descriptions containing `|` do not corrupt the save file.
* First-run inputs: `todo read | annotate book`, followed by `bye`.
* Second-run inputs: `list`, followed by `bye`.
* Expected output after `list`: `1.[T][ ] read | annotate book`.
