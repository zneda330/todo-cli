package todo.presentation;
import todo.application.TodoService;
import todo.domain.Todo;
import java.util.*;
import java.time.LocalDate;

/**
 * 기본 Todo UI 구현 클래스
 * 간단한 텍스트 기반의 사용자 인터페이스를 제공합니다.
 * 이모지를 사용하여 시각적으로 친근한 UI를 구성합니다.
 */
public class BasicTodoUI implements ITodoUI {
    private final TodoService todoService;
    private final Scanner scanner;

    /**
     * BasicTodoUI 생성자
     * @param todoService 비즈니스 로직을 처리할 서비스
     */
    public BasicTodoUI(TodoService todoService) {
        this.todoService = todoService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Todo 애플리케이션을 시작합니다.
     * 환영 메시지를 표시하고, 메뉴를 통해 사용자와 상호작용한 후 종료 메시지를 표시합니다.
     */
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

    /**
     * 환영 메시지를 표시합니다.
     */
    public void displayWelcome() {
        System.out.println("================================");
        System.out.println("📝 Welcome to Todo Manager!");
        System.out.println("================================");
    }

    /**
     * 메인 메뉴를 표시합니다.
     * 사용자가 선택할 수 있는 5가지 옵션을 보여줍니다.
     */
    public void displayMenu() {
        System.out.println("\n📋 Please select a menu:");
        System.out.println("1. ➕ Add Todo");
        System.out.println("2. 📃 View Todo List");
        System.out.println("3. 🔄 Toggle Todo Status");
        System.out.println("4. ❌ Delete Todo");
        System.out.println("5. 🚪 Exit Program");
        System.out.print("Choice: ");
    }

    /**
     * 사용자의 메뉴 선택을 처리합니다.
     * @param choice 사용자가 선택한 메뉴 번호
     * @return 프로그램을 종료해야 하면 true, 계속 실행하면 false
     */
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
                handleDeleteTodo();
                break;
            case "5":
                return true;
            default:
                System.out.println("❌ Invalid choice. Please enter a number between 1-5.\n");
        }
        return false;
    }

    /**
     * 새로운 Todo를 추가하는 기능을 처리합니다.
     * 사용자로부터 제목, 설명, 마감일을 입력받아 Todo를 생성합니다.
     */
    public void handleAddTodo() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        ➕ ADD NEW TODO");
        System.out.println("================================");
        System.out.print("📝 Enter todo title: ");
        String title = scanner.nextLine();
        System.out.print("📝 Enter todo description: ");
        String todoDescription = scanner.nextLine();
        System.out.print("📅 Enter due date (YYYY-MM-DD) or leave blank: ");
        String dueInput = scanner.nextLine();
        LocalDate dueDate = null;
        if (!dueInput.trim().isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueInput.trim());
            } catch (Exception e) {
                System.out.println("Invalid date format. Ignoring due date.");
            }
        }
        todoService.addTodo(title, todoDescription, dueDate);
        System.out.println("🎉 Todo added successfully! 🎉");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Todo 목록을 화면에 표시하는 헬퍼 메서드
     */
    private void displayTodoList(List<Todo> todos) {
        for (int i = 0; i < todos.size(); i++) {
            System.out.printf("[%2d] %s%n", i, todos.get(i));
        }
    }

    /**
     * 모든 Todo 목록을 표시합니다.
     * Todo가 없으면 안내 메시지를 표시합니다.
     */
    public void handleDisplayTodos() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        📃 TODO LIST");
        System.out.println("================================");
        
        if (todoService.getTodoCount() == 0) {
            System.out.println("🌟 No todos yet! Add some new ones! 🌟");
        } else {
            displayTodoList(todoService.getAllTodos());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Todo의 완료 상태를 변경하는 기능을 처리합니다.
     * 미완료 Todo를 완료로, 완료된 Todo를 미완료로 변경할 수 있습니다.
     */
    public void handleToggleTodo() {
        clearScreen();
        System.out.println("================================");
        System.out.println("      🔄 TOGGLE TODO STATUS");
        System.out.println("================================");
        
        if (todoService.getTodoCount() == 0) {
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
    
    /**
     * 미완료 Todo를 완료 상태로 변경하는 기능을 처리합니다.
     * 미완료 Todo 목록을 표시하고 사용자가 선택한 Todo를 완료 상태로 변경합니다.
     */
    private void handleIncompleteToggle() {
        clearScreen();
        System.out.println("================================");
        System.out.println("       ❌ INCOMPLETE TODOS");
        System.out.println("================================");
        
        if (todoService.getIncompleteTodos().isEmpty()) {
            System.out.println("🎉 All todos are completed! Great job! 🎉");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        displayTodoList(todoService.getIncompleteTodos());
        System.out.println("================================");
        
        System.out.println("Which todo would you like to mark as complete?");
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (todoIndex < 0 || todoIndex >= todoService.getTodoCount()) {
            System.out.println("Please enter a valid number.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        Todo todo = todoService.getTodoAt(todoIndex);
        if (todo != null && todo.isCompleted()) {
            System.out.println("❌ This todo is already completed!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        todoService.toggleTodo(todoIndex);
        System.out.println("🎉 Todo marked as completed! 🎉");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * 완료된 Todo를 미완료 상태로 변경하는 기능을 처리합니다.
     * 완료된 Todo 목록을 표시하고 사용자가 선택한 Todo를 미완료 상태로 변경합니다.
     */
    private void handleCompletedToggle() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        ✅ COMPLETED TODOS");
        System.out.println("================================");
        
        if (todoService.getCompletedTodos().isEmpty()) {
            System.out.println("🔔 No completed todos yet! Start completing some! 🔔");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        displayTodoList(todoService.getCompletedTodos());
        System.out.println("================================");
        
        System.out.println("Which todo would you like to mark as incomplete?");
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (todoIndex < 0 || todoIndex >= todoService.getTodoCount()) {
            System.out.println("Please enter a valid number.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        Todo todo = todoService.getTodoAt(todoIndex);
        if (todo != null && !todo.isCompleted()) {
            System.out.println("❌ This todo is already incomplete!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        todoService.toggleTodo(todoIndex);
        System.out.println("🔄 Todo marked as incomplete! 🔄");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Todo를 삭제하는 기능을 처리합니다.
     * 전체 Todo 목록을 표시하고 사용자가 선택한 Todo를 삭제합니다.
     */
    private void handleDeleteTodo() {
        clearScreen();
        System.out.println("================================");
        System.out.println("        ❌ DELETE TODO");
        System.out.println("================================");

        if (todoService.getTodoCount() == 0) {
            System.out.println("❌ No todos to delete!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        displayTodoList(todoService.getAllTodos());
        System.out.println("================================");
        System.out.print("Enter todo number to delete: ");
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (todoIndex < 0 || todoIndex >= todoService.getTodoCount()) {
            System.out.println("Please enter a valid number.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        todoService.deleteTodo(todoIndex);
        System.out.println("🎉 Todo deleted! 🎉");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * 종료 메시지를 표시합니다.
     */
    public void displayGoodbye() {
        clearScreen();
        System.out.println("================================");
        System.out.println("Goodbye!");
        System.out.println("================================");
    }
    
    /**
     * 콘솔 화면을 지웁니다.
     * ANSI 이스케이프 시퀀스를 사용하여 화면을 초기화합니다.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}