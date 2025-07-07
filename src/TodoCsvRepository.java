import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Use TodoFilter (in default package) to specify which todos to load

/**
 * Simple repository that saves and loads todos using a CSV file.
 */
public class TodoCsvRepository implements TodoRepository {
    private final String filePath;

    public TodoCsvRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Load all todos from the CSV file.
     * Each line uses the format: "title","description",completed,metadata
     */
    public List<Todo> load() {
        return load(TodoFilter.ALL);
    }

    /**
     * Load todos filtered by completion state.
     *
     * @param filter which todos to load
     */
    public List<Todo> load(TodoFilter filter) {
        List<Todo> todos = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return todos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // split into up to 4 parts: title, description, completed, metadata
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 4);
                if (parts.length >= 3) {
                    String title = unquote(parts[0]);
                    String description = unquote(parts[1]);
                    boolean completed = Boolean.parseBoolean(parts[2]);
                    Map<String, String> metadata = new HashMap<>();
                    if (parts.length == 4) {
                        for (String entry : parts[3].split(";")) {
                            if (entry.isEmpty()) continue;
                            String[] kv = entry.split("=", 2);
                            if (kv.length == 2) {
                                metadata.put(kv[0], kv[1]);
                            }
                        }
                    }
                    Todo todo = new Todo(title, description, metadata);
                    todo.setCompleted(completed);
                    if (filter == TodoFilter.ALL
                            || (filter == TodoFilter.COMPLETED && todo.isCompleted())
                            || (filter == TodoFilter.INCOMPLETE && !todo.isCompleted())) {
                        todos.add(todo);
                    }
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return todos;
    }

    /**
     * Retrieve a single todo by its index.
     *
     * @param index zero-based index to load
     * @return todo at the index or null if out of range
     */
    public Todo get(int index) {
        List<Todo> all = load();
        if (index >= 0 && index < all.size()) {
            return all.get(index);
        }
        return null;
    }

    /**
     * Save todos to CSV file.
     */
    public void save(List<Todo> todos) {
        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Todo todo : todos) {
                StringBuilder metadata = new StringBuilder();
                for (Map.Entry<String, String> e : todo.getMetadata().entrySet()) {
                    if (metadata.length() > 0) metadata.append(';');
                    metadata.append(e.getKey()).append('=').append(e.getValue());
                }
                String metaString = metadata.toString();
                String line = String.format("%s,%s,%b",
                        quote(todo.getTitle()),
                        quote(todo.getDescription()),
                        todo.isCompleted());
                if (!metaString.isEmpty()) {
                    line = line + "," + metaString;
                }
                writer.println(line);
            }
        } catch (IOException e) {
            // ignore
        }
    }

    private String quote(String text) {
        String escaped = text.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private String unquote(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            String inner = text.substring(1, text.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return text;
    }
}
