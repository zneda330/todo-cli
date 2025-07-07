package todo;

import todo.application.TodoService;
import todo.application.TodoServiceImpl;
import todo.domain.TodoRepository;
import todo.infrastructure.TodoCsvRepository;
import todo.presentation.*;

/**
 * Todo 애플리케이션의 진입점 클래스
 * 
 * 간소화된 아키텍처: UI → Service → Repository
 * Controller 없이 UI가 직접 Service를 호출합니다.
 * 
 * 사용법: java todo.Main [fancy]
 * - 인자 없이 실행: 기본 UI 모드
 * - "fancy" 인자로 실행: 화려한 UI 모드
 */
public class Main {
    public static void main(String[] args) {
        // 1. Repository 생성 (Infrastructure Layer)
        TodoRepository repository = new TodoCsvRepository("todos.csv");
        
        // 2. Service 생성 (Application Layer)
        TodoService service = new TodoServiceImpl(repository);
        
        // 3. UI 생성 및 Service 주입 (Presentation Layer)
        ITodoUI ui;
        if (args.length > 0 && "fancy".equalsIgnoreCase(args[0])) {
            ui = new FancyTodoUI(service);
        } else {
            ui = new BasicTodoUI(service);
        }
        
        // 4. 애플리케이션 시작
        ui.start();
    }
}
