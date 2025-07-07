package test;

/**
 * 모든 테스트를 실행하는 메인 클래스
 * 
 * 프로젝트의 모든 테스트를 한 번에 실행할 수 있습니다.
 */
public class AllTests {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("🚀 Todo CLI 테스트 스위트 시작");
        System.out.println("========================================");
        
        // Todo 도메인 테스트
        System.out.println("\n📌 Todo 도메인 테스트");
        System.out.println("----------------------------------------");
        TodoTest.main(args);
        
        // TodoService 테스트
        System.out.println("\n📌 TodoService 테스트");
        System.out.println("----------------------------------------");
        TodoServiceTest.main(args);
        
        // TodoCsvRepository 테스트
        System.out.println("\n📌 TodoCsvRepository 테스트");
        System.out.println("----------------------------------------");
        TodoCsvRepositoryTest.main(args);
        
        System.out.println("\n========================================");
        System.out.println("✨ 모든 테스트 실행 완료");
        System.out.println("========================================");
    }
}