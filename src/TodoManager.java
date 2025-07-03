import java.util.*;

public class TodoManager {
    private static TodoManager instance;
    private ArrayList<Todo> todos;

    private TodoManager() {
        this.todos = new ArrayList<>();
    }

    public static TodoManager getInstance() {
        if (instance == null) {
            instance = new TodoManager();
        }
        return instance;
    }

    public void addTodo(String description) {
        Todo todo = new Todo(description);
        todos.add(todo);
    }

    public void displayTodos() {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
    }

    public void completeTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(true);
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
    
    public void displayIncompleteTodos() {
        int count = 0;
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (!todo.isCompleted()) {
                System.out.printf("[%2d] %s%n", i, todo);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("🎉 All todos are completed! Great job! 🎉");
        }
    }
    
    public boolean hasIncompleteTodos() {
        for (Todo todo : todos) {
            if (!todo.isCompleted()) {
                return true;
            }
        }
        return false;
    }
    
    public void displayCompletedTodos() {
        int count = 0;
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.isCompleted()) {
                System.out.printf("[%2d] %s%n", i, todo);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("🔔 No completed todos yet! Start completing some! 🔔");
        }
    }
    
    public boolean hasCompletedTodos() {
        for (Todo todo : todos) {
            if (todo.isCompleted()) {
                return true;
            }
        }
        return false;
    }
    
    public void toggleTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(!todo.isCompleted());
        }
    }
}