import java.util.*;

public class BasicTodoUI implements ITodoUI {
    private Scanner scanner;

    public BasicTodoUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        displayWelcome();
        
        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            
            if (handleChoice(choice)) {
                break;
            }
        }
        
        displayGoodbye();
    }

    public void displayWelcome() {
        System.out.println("================================");
        System.out.println("📝 Welcome to Todo Manager!");
        System.out.println("================================");
    }

    public void displayMenu() {
        System.out.println("\n📋 Please select a menu:");
        System.out.println("1. ➕ Add Todo");
        System.out.println("2. 📃 View Todo List");
        System.out.println("3. 🔄 Toggle Todo Status");
        System.out.println("4. 🚪 Exit Program");
        System.out.print("Choice: ");
    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                handleAddTodo();
                break;
            case "2":
                handleDisplayTodos();
                break;
            case "3":
                handleToggleTodo();
                break;
            case "4":
                return true;
            default:
                System.out.println("❌ Invalid choice. Please enter a number between 1-4.\n");
        }
        return false;
    }

    public void handleAddTodo() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        ➕ ADD NEW TODO");
        System.out.println("================================");
        System.out.print("📝 Enter a new todo: ");
        String todoDescription = scanner.nextLine();
        TodoManager.getInstance().addTodo(todoDescription);
        System.out.println("🎉 Todo added successfully! 🎉");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void handleDisplayTodos() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        📃 TODO LIST");
        System.out.println("================================");
        
        if (TodoManager.getInstance().getTodoCount() == 0) {
            System.out.println("🌟 No todos yet! Add some new ones! 🌟");
        } else {
            TodoManager.getInstance().displayTodos();
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void handleToggleTodo() {
        clearScreen();
        System.out.println("================================");
        System.out.println("      🔄 TOGGLE TODO STATUS");
        System.out.println("================================");
        
        if (TodoManager.getInstance().getTodoCount() == 0) {
            System.out.println("❌ No todos available!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        System.out.println("📋 Select todo type to view:");
        System.out.println("1. ❌ Incomplete Todos");
        System.out.println("2. ✅ Completed Todos");
        System.out.print("Choice (1-2): ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                handleIncompleteToggle();
                break;
            case "2":
                handleCompletedToggle();
                break;
            default:
                System.out.println("❌ Invalid choice. Please enter 1 or 2.");
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
        }
    }
    
    private void handleIncompleteToggle() {
        clearScreen();
        System.out.println("================================");
        System.out.println("       ❌ INCOMPLETE TODOS");
        System.out.println("================================");
        
        if (!TodoManager.getInstance().hasIncompleteTodos()) {
            System.out.println("🎉 All todos are completed! Great job! 🎉");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        TodoManager.getInstance().displayIncompleteTodos();
        System.out.println("================================");
        
        System.out.println("Which todo would you like to mark as complete?");
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (!TodoManager.getInstance().isValidIndex(todoIndex)) {
            System.out.println("Please enter a valid number.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        Todo todo = TodoManager.getInstance().getTodoAt(todoIndex);
        if (todo != null && todo.isCompleted()) {
            System.out.println("❌ This todo is already completed!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        TodoManager.getInstance().toggleTodo(todoIndex);
        System.out.println("🎉 Todo marked as completed! 🎉");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void handleCompletedToggle() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        ✅ COMPLETED TODOS");
        System.out.println("================================");
        
        if (!TodoManager.getInstance().hasCompletedTodos()) {
            System.out.println("🔔 No completed todos yet! Start completing some! 🔔");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        TodoManager.getInstance().displayCompletedTodos();
        System.out.println("================================");
        
        System.out.println("Which todo would you like to mark as incomplete?");
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (!TodoManager.getInstance().isValidIndex(todoIndex)) {
            System.out.println("Please enter a valid number.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        Todo todo = TodoManager.getInstance().getTodoAt(todoIndex);
        if (todo != null && !todo.isCompleted()) {
            System.out.println("❌ This todo is already incomplete!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        TodoManager.getInstance().toggleTodo(todoIndex);
        System.out.println("🔄 Todo marked as incomplete! 🔄");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayGoodbye() {
        clearScreen();
        System.out.println("================================");
        System.out.println("Goodbye!");
        System.out.println("================================");
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}