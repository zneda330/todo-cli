package todo.domain;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class Todo {
    private String title;       // Short title
    private String description; // Detailed description
    private boolean completed;  // Completion status
    private LocalDate dueDate;  // Optional due date
    // Additional metadata that can store arbitrary key/value pairs
    private Map<String, String> metadata;

    // Constructor: used when creating new todos with title and description
    public Todo(String title, String description) {
        this(title, description, null, new HashMap<>());
    }

    // Constructor with due date
    public Todo(String title, String description, LocalDate dueDate) {
        this(title, description, dueDate, new HashMap<>());
    }

    // Constructor that accepts extra metadata
    public Todo(String title, String description, LocalDate dueDate, Map<String, String> metadata) {
        this.title = title;
        this.description = description;
        this.completed = false; // Newly created todos are incomplete
        this.dueDate = dueDate;
        this.metadata = new HashMap<>();
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    // Method to get todo content
    public String getTitle() {
        return title;
    }

    // Method to get todo description
    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Method to check completion status
    public boolean isCompleted() {
        return completed;
    }

    // Method to change completion status
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadataField(String key, String value) {
        metadata.put(key, value);
    }

    public String getMetadataField(String key) {
        return metadata.get(key);
    }

    // Method to represent todo as string (using emoji)
    @Override
    public String toString() {
        String base = (completed ? "✅ " : "❌") + title + " - " + description;
        if (dueDate != null) {
            base += " (Due: " + dueDate + ")";
        }
        return base;
    }
}
