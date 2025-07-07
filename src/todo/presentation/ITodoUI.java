package todo.presentation;

/**
 * Todo 사용자 인터페이스의 공통 계약을 정의하는 인터페이스
 * 
 * UI는 사용자와의 상호작용을 담당하고, Service를 통해 비즈니스 로직을 처리합니다.
 * Controller 없이 UI → Service → Repository 구조를 따릅니다.
 */
public interface ITodoUI {
    /**
     * UI를 시작하고 메인 루프를 실행
     */
    void start();
    
    /**
     * 환영 메시지를 표시
     */
    void displayWelcome();
    
    /**
     * 메인 메뉴를 표시
     */
    void displayMenu();
    
    /**
     * 종료 메시지를 표시
     */
    void displayGoodbye();
    
    /**
     * Todo 추가 기능을 처리
     */
    void handleAddTodo();
    
    /**
     * Todo 목록 표시 기능을 처리
     */
    void handleDisplayTodos();
    
    /**
     * Todo 상태 토글 기능을 처리
     */
    void handleToggleTodo();
}