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
  deadline return book /by Sunday
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
  2.[D][X] return book (by: Sunday)
  ```

### Descriptions containing separators

* Aim: Verify that task descriptions containing `|` do not corrupt the save file.
* First-run inputs: `todo read | annotate book`, followed by `bye`.
* Second-run inputs: `list`, followed by `bye`.
* Expected output after `list`: `1.[T][ ] read | annotate book`.
