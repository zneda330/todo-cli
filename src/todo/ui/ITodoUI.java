package todo.ui;

/**
 * Todo 사용자 인터페이스의 공통 계약을 정의하는 인터페이스
 * 
 * 이 인터페이스를 구현하여 다양한 스타일의 UI를 만들 수 있습니다.
 * (예: 기본 텍스트 UI, 화려한 컬러 UI 등)
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