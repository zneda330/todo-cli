# Todo CLI

This is a small Java console application for managing todos. It supports two interfaces:

- **BasicTodoUI** – simple text based interface.
- **FancyTodoUI** – colorful interface with ASCII decorations.

## Features

- Add todos with title, description and optional due date.
- View all todos.
- Toggle completion status.
- Delete todos.
- Persist tasks to a CSV file (`todos.csv`).

Run with `java todo.Main [fancy]` to choose UI.

## Architecture
This project follows a basic DDD layout with separate packages for domain, application, infrastructure and UI.
