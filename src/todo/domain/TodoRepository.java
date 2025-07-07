package todo.domain;

import java.util.List;

/**
 * Todo 저장소 인터페이스
 * 
 * 이 인터페이스는 Todo 데이터의 영속성을 관리하는 저장소의 계약을 정의합니다.
 * 구체적인 저장 방식(파일, 데이터베이스 등)에 독립적인 추상화를 제공합니다.
 */
public interface TodoRepository {
    /**
     * 모든 Todo 목록을 로드
     * @return 전체 Todo 목록
     */
    List<Todo> load();
    
    /**
     * 필터를 적용하여 Todo 목록을 로드
     * @param filter 적용할 필터 (ALL, COMPLETED, INCOMPLETE)
     * @return 필터링된 Todo 목록
     */
    List<Todo> load(TodoFilter filter);
    
    /**
     * 특정 인덱스의 Todo를 가져오기
     * @param index Todo의 인덱스
     * @return Todo 객체
     */
    Todo get(int index);
    
    /**
     * Todo 목록을 저장소에 저장
     * @param todos 저장할 Todo 목록
     */
    void save(List<Todo> todos);
    
    /**
     * 단일 Todo를 저장소에 추가
     * 기존 Todo 목록에 새로운 Todo를 추가하여 저장합니다.
     * @param todo 추가할 Todo 항목
     */
    void save(Todo todo);
}
