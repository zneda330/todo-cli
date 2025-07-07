package todo.application;
import todo.domain.Todo;
import todo.domain.TodoFilter;
import todo.domain.TodoRepository;
import todo.infrastructure.TodoCsvRepository;
import java.util.*;
import java.time.LocalDate;

/**
 * Todo 항목들을 관리하는 중앙 매니저 클래스
 * 
 * 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 존재합니다.
 * 모든 Todo 관련 비즈니스 로직을 처리하고 저장소와의 통신을 담당합니다.
 */
public class TodoManager {
    private static TodoManager instance;     // 싱글톤 인스턴스
    private ArrayList<Todo> todos;           // 메모리에 로드된 Todo 목록
    private TodoRepository repository;       // 데이터 저장소

    /**
     * 비공개 생성자 - 싱글톤 패턴 구현
     * CSV 파일에서 Todo 데이터를 로드합니다.
     */
    private TodoManager() {
        this.repository = new TodoCsvRepository("todos.csv");
        this.todos = new ArrayList<>();
        this.todos.addAll(repository.load());
    }

    /**
     * 싱글톤 인스턴스 반환
     * @return TodoManager의 유일한 인스턴스
     */
    public static TodoManager getInstance() {
        if (instance == null) {
            instance = new TodoManager();
        }
        return instance;
    }

    /**
     * 하위 호환성을 위한 메서드 - 설명만으로 Todo 추가
     * @param description 할 일 설명
     */
    public void addTodo(String description) {
        addTodo(description, "", null, new HashMap<>());
    }

    /**
     * 제목과 설명으로 Todo 추가
     * @param title 할 일 제목
     * @param description 할 일 설명
     */
    public void addTodo(String title, String description) {
        addTodo(title, description, null, new HashMap<>());
    }

    /**
     * 제목, 설명, 마감일로 Todo 추가
     * @param title 할 일 제목
     * @param description 할 일 설명
     * @param dueDate 마감일
     */
    public void addTodo(String title, String description, LocalDate dueDate) {
        addTodo(title, description, dueDate, new HashMap<>());
    }

    /**
     * 모든 정보를 포함하여 Todo 추가
     * @param title 할 일 제목
     * @param description 할 일 설명
     * @param dueDate 마감일
     * @param metadata 추가 메타데이터
     */
    public void addTodo(String title, String description, LocalDate dueDate, Map<String, String> metadata) {
        Todo todo = new Todo(title, description, dueDate, metadata);
        todos.add(todo);
        repository.save(todos);  // 저장소에 저장
    }

    /**
     * 모든 Todo를 화면에 출력
     */
    public void displayTodos() {
        List<Todo> list = fetchTodos(TodoFilter.ALL);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
    }

    /**
     * 특정 Todo를 완료 상태로 변경
     * @param index Todo의 인덱스
     */
    public void completeTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(true);
            repository.save(todos);
        }
    }

    /**
     * 전체 Todo 개수 반환
     * @return Todo 개수
     */
    public int getTodoCount() {
        return todos.size();
    }

    /**
     * 유효한 인덱스인지 확인
     * @param index 확인할 인덱스
     * @return 유효 여부
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < todos.size();
    }
    
    /**
     * 특정 인덱스의 Todo 반환
     * @param index Todo의 인덱스
     * @return Todo 객체 또는 null
     */
    public Todo getTodoAt(int index) {
        if (index >= 0 && index < todos.size()) {
            return todos.get(index);
        }
        return null;
    }

    /**
     * 필터를 사용하여 저장소에서 Todo 목록 로드
     * @param filter 적용할 필터 (ALL, COMPLETED, INCOMPLETE)
     * @return 필터링된 Todo 목록
     */
    public List<Todo> fetchTodos(TodoFilter filter) {
        return repository.load(filter);
    }

    /**
     * 저장소에서 특정 인덱스의 Todo를 직접 가져오기
     * @param index Todo의 인덱스
     * @return Todo 객체
     */
    public Todo fetchTodo(int index) {
        return repository.get(index);
    }
    
    /**
     * 미완료 Todo들만 화면에 출력
     */
    public void displayIncompleteTodos() {
        List<Todo> list = fetchTodos(TodoFilter.INCOMPLETE);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
        if (list.isEmpty()) {
            System.out.println("🎉 모든 할 일이 완료되었습니다! 충상합니다! 🎉");
        }
    }
    
    /**
     * 미완료 Todo가 있는지 확인
     * @return 미완료 Todo 존재 여부
     */
    public boolean hasIncompleteTodos() {
        return !fetchTodos(TodoFilter.INCOMPLETE).isEmpty();
    }
    
    /**
     * 완료된 Todo들만 화면에 출력
     */
    public void displayCompletedTodos() {
        List<Todo> list = fetchTodos(TodoFilter.COMPLETED);
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            System.out.printf("[%2d] %s%n", i, todo);
        }
        if (list.isEmpty()) {
            System.out.println("🔔 아직 완료된 할 일이 없습니다! 하나씩 완료해보세요! 🔔");
        }
    }
    
    /**
     * 완료된 Todo가 있는지 확인
     * @return 완료된 Todo 존재 여부
     */
    public boolean hasCompletedTodos() {
        return !fetchTodos(TodoFilter.COMPLETED).isEmpty();
    }
    
    /**
     * Todo의 완료 상태를 토글 (완료 ↔ 미완료)
     * @param index Todo의 인덱스
     */
    public void toggleTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(!todo.isCompleted());
            repository.save(todos);
        }
    }

    /**
     * Todo 삭제
     * @param index 삭제할 Todo의 인덱스
     */
    public void deleteTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            todos.remove(index);
            repository.save(todos);
        }
    }
}
