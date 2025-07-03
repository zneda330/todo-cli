public class Todo {
    private String description; // Todo content
    private boolean completed;  // Completion status

    // Constructor: used when creating new todos
    public Todo(String description) {
        this.description = description;
        this.completed = false; // Newly created todos are incomplete
    }

    // Method to get todo content
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

    // Method to represent todo as string (using emoji)
    @Override
    public String toString() {
        return (completed ? "✅ " : "❌") + description;
    }
}
