import java.util.HashMap;
import java.util.Map;

public class Todo {
    private String title;       // Short title
    private String description; // Detailed description
    private boolean completed;  // Completion status
    // Additional metadata that can store arbitrary key/value pairs
    private Map<String, String> metadata;

    // Constructor: used when creating new todos with title and description
    public Todo(String title, String description) {
        this(title, description, new HashMap<>());
    }

    // Constructor that accepts extra metadata
    public Todo(String title, String description, Map<String, String> metadata) {
        this.title = title;
        this.description = description;
        this.completed = false; // Newly created todos are incomplete
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
        return (completed ? "✅ " : "❌") + title + " - " + description;
    }
}
