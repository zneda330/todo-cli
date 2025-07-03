import java.util.*;

public class Todo {
    private String description; // 할일 내용
    private boolean completed;  // 완료 여부
    private static ArrayList<Todo> todos = new ArrayList<>();

    // 생성자: 새로운 할일을 만들 때 사용
    public Todo(String description) {
        this.description = description;
        this.completed = false; // 새로 만든 할일은 미완료 상태
    }

    // 할일 내용을 가져오는 메서드
    public String getDescription() {
        return description;
    }

    // 완료 여부를 확인하는 메서드
    public boolean isCompleted() {
        return completed;
    }

    // 완료 상태를 변경하는 메서드
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // 할일을 문자열로 표현하는 메서드 (이모지 사용)
    @Override
    public String toString() {
        return (completed ? "✅ " : "❌") + description;
    }

    // 할일 추가 메서드
    public static void addTodo(String description) {
        Todo todo = new Todo(description);
        todos.add(todo);
    }

    // 할일 목록 출력 메서드
    public static void displayTodos() {
        for (Todo todo : todos) {
            System.out.println(todo);
        }
    }

    // 할일 상태 변경 메서드
    public static void completeTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(true);
        }
    }

    // 할일 개수 반환 메서드
    public static int getTodoCount() {
        return todos.size();
    }

    // 할일 관리 시스템 시작 메서드
    public static void startTodoApp() {
        Scanner todoSc = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("📝 할일 관리 프로그램에 오신 것을 환영합니다!");
        System.out.println("================================");

        todos: while (true) {
            System.out.println("\n📋 멋진 메뉴를 선택하세요:");
            System.out.println("1. ➕ 할일 추가");
            System.out.println("2. 📃 할일 목록 보기");
            System.out.println("3. 🔄 할일 상태 변경");
            System.out.println("4. 🚪 프로그램 종료");
            System.out.print("선택: ");
            String choice = todoSc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("📝 새로운 할일을 입력하세요: ");
                    String todoDescription = todoSc.nextLine();
                    Todo.addTodo(todoDescription);
                    break;
                case "2":
                    Todo.displayTodos();
                    break;
                case "3":
                    System.out.println("몇 번 투두를 완료로 바꾸시겠습니까?");
                    int completeTodo = todoSc.nextInt();

                    if (completeTodo >= Todo.getTodoCount() || completeTodo < 0) {
                        System.out.println("올바른 값을 입력하세요.");
                        continue;
                    }

                    Todo.completeTodo(completeTodo);
                    break;
                case "4":
                    break todos;
                default:
                    System.out.println("❌ 잘못된 선택입니다. 1-4 사이의 숫자를 입력해주세요.\n");
            }
        }

        System.out.println("================================");
        System.out.println("안녕히가세요.");
        System.out.println("================================");
    }
}
