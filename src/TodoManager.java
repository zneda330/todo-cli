import java.util.*;

// Repository for persistence
// (TodoCsvRepository lives in the default package)


public class TodoManager {
    private static TodoManager instance;
    private ArrayList<Todo> todos;
    private TodoRepository repository;

    private TodoManager() {
        this.repository = new TodoCsvRepository("todos.csv");
        this.todos = new ArrayList<>();
        this.todos.addAll(repository.load());
    }

    public static TodoManager getInstance() {
        if (instance == null) {
            instance = new TodoManager();
        }
        return instance;
    }

    // Backwards compatibility method
    public void addTodo(String description) {
        addTodo(description, "", new HashMap<>());
    }

    public void addTodo(String title, String description) {
        addTodo(title, description, new HashMap<>());
    }

    public void addTodo(String title, String description, Map<String, String> metadata) {
        Todo todo = new Todo(title, description, metadata);
        todos.add(todo);
        repository.save(todos);
    }

    public void displayTodos() {
        List<Todo> list = fetchTodos(TodoFilter.ALL);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
    }

    public void completeTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(true);
            repository.save(todos);
        }
    }

    public int getTodoCount() {
        return todos.size();
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < todos.size();
    }
    
    public Todo getTodoAt(int index) {
        if (index >= 0 && index < todos.size()) {
            return todos.get(index);
        }
        return null;
    }

    /**
     * Load todos from repository using a filter.
     */
    public List<Todo> fetchTodos(TodoFilter filter) {
        return repository.load(filter);
    }

    /**
     * Retrieve a todo directly from the repository by index.
     */
    public Todo fetchTodo(int index) {
        return repository.get(index);
    }
    
    public void displayIncompleteTodos() {
        List<Todo> list = fetchTodos(TodoFilter.INCOMPLETE);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
        if (list.isEmpty()) {
            System.out.println("🎉 All todos are completed! Great job! 🎉");
        }
    }
    
    public boolean hasIncompleteTodos() {
        return !fetchTodos(TodoFilter.INCOMPLETE).isEmpty();
    }
    
    public void displayCompletedTodos() {
        List<Todo> list = fetchTodos(TodoFilter.COMPLETED);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
        if (list.isEmpty()) {
            System.out.println("🔔 No completed todos yet! Start completing some! 🔔");
        }
    }
    
    public boolean hasCompletedTodos() {
        return !fetchTodos(TodoFilter.COMPLETED).isEmpty();
    }
    
    public void toggleTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(!todo.isCompleted());
            repository.save(todos);
        }
    }
}
