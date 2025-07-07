package todo.domain;

/**
 * Todo 필터링을 위한 열거형 (Enum)
 * 
 * Todo 목록을 조회할 때 사용되는 필터 타입을 정의합니다.
 * Repository와 Manager에서 Todo를 필터링할 때 사용됩니다.
 */
public enum TodoFilter {
    /**
     * 모든 Todo를 포함 (완료/미완료 상관없이)
     */
    ALL,
    
    /**
     * 완료된 Todo만 포함
     */
    COMPLETED,
    
    /**
     * 미완료 Todo만 포함
     */
    INCOMPLETE
}
