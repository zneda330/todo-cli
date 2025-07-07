package todo.application;

import todo.domain.Todo;
import todo.domain.TodoFilter;
import todo.domain.TodoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TodoService 인터페이스의 구현체
 * 
 * 실제 비즈니스 로직을 구현하며, Repository를 통해 데이터를 관리합니다.
 * 기존 TodoManager의 로직을 서비스 레이어로 이동시켰습니다.
 */
public class TodoServiceImpl implements TodoService {
    private final TodoRepository repository;
    private final List<Todo> todos;
    
    /**
     * TodoServiceImpl 생성자
     * @param repository Todo 저장소
     */
    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
        this.todos = new ArrayList<>();
        this.todos.addAll(repository.load());
    }
    
    /**
     * 새로운 Todo 추가
     */
    @Override
    public void addTodo(String title, String description, LocalDate dueDate) {
        Todo todo = new Todo(title, description, dueDate, new HashMap<>());
        todos.add(todo);
        repository.save(todo);
    }
    
    /**
     * 모든 Todo 조회
     */
    @Override
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos);
    }
    
    /**
     * 완료된 Todo 조회
     */
    @Override
    public List<Todo> getCompletedTodos() {
        return repository.load(TodoFilter.COMPLETED);
    }
    
    /**
     * 미완료 Todo 조회
     */
    @Override
    public List<Todo> getIncompleteTodos() {
        return repository.load(TodoFilter.INCOMPLETE);
    }
    
    /**
     * Todo 상태 토글
     */
    @Override
    public boolean toggleTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            Todo todo = todos.get(index);
            todo.setCompleted(!todo.isCompleted());
            repository.save(todos);
            return true;
        }
        return false;
    }
    
    /**
     * Todo 삭제
     */
    @Override
    public boolean deleteTodo(int index) {
        if (index >= 0 && index < todos.size()) {
            todos.remove(index);
            repository.save(todos);
            return true;
        }
        return false;
    }
    
    /**
     * 전체 Todo 개수
     */
    @Override
    public int getTodoCount() {
        return todos.size();
    }
    
    /**
     * 특정 인덱스의 Todo 조회
     */
    @Override
    public Todo getTodoAt(int index) {
        if (index >= 0 && index < todos.size()) {
            return todos.get(index);
        }
        return null;
    }
}