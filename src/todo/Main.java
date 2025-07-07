package todo;
import todo.ui.*;

/**
 * Todo 애플리케이션의 진입점 클래스
 * 
 * 사용법: java todo.Main [fancy]
 * - 인자 없이 실행: 기본 UI 모드
 * - "fancy" 인자로 실행: 화려한 UI 모드
 */
public class Main {
    public static void main(String[] args) {
        ITodoUI ui;
        
        // 명령행 인자를 확인하여 UI 모드를 결정
        if (args.length > 0 && "fancy".equalsIgnoreCase(args[0])) {
            // 화려한 UI 모드 (컬러, 박스 그리기 등)
            ui = new FancyTodoUI();
        } else {
            // 기본 UI 모드 (단순한 텍스트 기반)
            ui = new BasicTodoUI();
        }
        
        // 선택된 UI를 시작
        ui.start();
    }
}
