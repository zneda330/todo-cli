package todo.application;

import todo.domain.Todo;
import java.time.LocalDate;
import java.util.List;

/**
 * Todo 비즈니스 로직을 정의하는 서비스 인터페이스
 * 
 * DDD의 Application Service 역할을 담당합니다.
 * 도메인 로직을 조율하고 유즈케이스를 구현합니다.
 */
public interface TodoService {
    /**
     * 새로운 Todo 추가
     * @param title 제목
     * @param description 설명
     * @param dueDate 마감일 (선택사항)
     */
    void addTodo(String title, String description, LocalDate dueDate);
    
    /**
     * 모든 Todo 조회
     * @return 전체 Todo 목록
     */
    List<Todo> getAllTodos();
    
    /**
     * 완료된 Todo 조회
     * @return 완료된 Todo 목록
     */
    List<Todo> getCompletedTodos();
    
    /**
     * 미완료 Todo 조회
     * @return 미완료 Todo 목록
     */
    List<Todo> getIncompleteTodos();
    
    /**
     * Todo 상태 토글
     * @param index Todo 인덱스
     * @return 성공 여부
     */
    boolean toggleTodo(int index);
    
    /**
     * Todo 삭제
     * @param index Todo 인덱스
     * @return 성공 여부
     */
    boolean deleteTodo(int index);
    
    /**
     * 전체 Todo 개수
     * @return Todo 개수
     */
    int getTodoCount();
    
    /**
     * 특정 인덱스의 Todo 조회
     * @param index Todo 인덱스
     * @return Todo 객체 또는 null
     */
    Todo getTodoAt(int index);
}