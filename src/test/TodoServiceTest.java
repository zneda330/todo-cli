package test;

import todo.application.TodoService;
import todo.application.TodoServiceImpl;
import todo.domain.Todo;
import todo.domain.TodoRepository;
import todo.infrastructure.TodoCsvRepository;
import java.time.LocalDate;
import java.io.File;
import java.util.List;

/**
 * TodoService 구현체 테스트
 * 
 * TodoServiceImpl의 비즈니스 로직을 검증합니다.
 */
public class TodoServiceTest {
    
    private static final String TEST_FILE = "test_todos.csv";
    
    public static void main(String[] args) {
        TestRunner.reset();
        
        // 테스트 파일 정리
        cleanupTestFile();
        
        // 테스트 실행
        testAddTodo();
        testGetAllTodos();
        testToggleTodo();
        testDeleteTodo();
        testGetCompletedAndIncompleteTodos();
        testGetTodoAt();
        testGetTodoCount();
        
        // 엣지 케이스 테스트
        testEmptyList();
        testInvalidIndex();
        testMultipleTodos();
        testPersistence();
        
        // 결과 출력
        TestRunner.printSummary();
        
        // 테스트 파일 정리
        cleanupTestFile();
    }
    
    /**
     * Todo 추가 테스트
     */
    private static void testAddTodo() {
        TestRunner.startTest("Todo 추가");
        cleanupTestFile(); // 테스트 시작 전 파일 정리
        
        TodoService service = createService();
        
        service.addTodo("제목1", "설명1", null);
        TestRunner.assertEquals(1, service.getTodoCount(), "Todo가 추가됨");
        
        service.addTodo("제목2", "설명2", LocalDate.now());
        TestRunner.assertEquals(2, service.getTodoCount(), "두 번째 Todo가 추가됨");
        
        Todo todo = service.getTodoAt(0);
        TestRunner.assertEquals("제목1", todo.getTitle(), "첫 번째 Todo 제목 확인");
        TestRunner.assertEquals("설명1", todo.getDescription(), "첫 번째 Todo 설명 확인");
    }
    
    /**
     * 모든 Todo 조회 테스트
     */
    private static void testGetAllTodos() {
        TestRunner.startTest("모든 Todo 조회");
        cleanupTestFile();
        
        TodoService service = createService();
        
        service.addTodo("제목1", "설명1", null);
        service.addTodo("제목2", "설명2", null);
        service.addTodo("제목3", "설명3", null);
        
        List<Todo> todos = service.getAllTodos();
        TestRunner.assertEquals(3, todos.size(), "모든 Todo가 조회됨");
        TestRunner.assertEquals("제목1", todos.get(0).getTitle(), "첫 번째 Todo 확인");
        TestRunner.assertEquals("제목2", todos.get(1).getTitle(), "두 번째 Todo 확인");
        TestRunner.assertEquals("제목3", todos.get(2).getTitle(), "세 번째 Todo 확인");
    }
    
    /**
     * Todo 상태 토글 테스트
     */
    private static void testToggleTodo() {
        TestRunner.startTest("Todo 상태 토글");
        cleanupTestFile();
        
        TodoService service = createService();
        service.addTodo("테스트", "설명", null);
        
        Todo todo = service.getTodoAt(0);
        TestRunner.assertFalse(todo.isCompleted(), "초기 상태는 미완료");
        
        boolean result = service.toggleTodo(0);
        TestRunner.assertTrue(result, "토글 성공");
        TestRunner.assertTrue(todo.isCompleted(), "완료 상태로 변경됨");
        
        service.toggleTodo(0);
        TestRunner.assertFalse(todo.isCompleted(), "다시 미완료 상태로 변경됨");
    }
    
    /**
     * Todo 삭제 테스트
     */
    private static void testDeleteTodo() {
        TestRunner.startTest("Todo 삭제");
        cleanupTestFile();
        
        TodoService service = createService();
        service.addTodo("삭제될 Todo", "설명", null);
        service.addTodo("남을 Todo", "설명", null);
        
        TestRunner.assertEquals(2, service.getTodoCount(), "초기 Todo 개수");
        
        boolean result = service.deleteTodo(0);
        TestRunner.assertTrue(result, "삭제 성공");
        TestRunner.assertEquals(1, service.getTodoCount(), "Todo 개수가 줄어듦");
        TestRunner.assertEquals("남을 Todo", service.getTodoAt(0).getTitle(), "남은 Todo 확인");
    }
    
    /**
     * 완료/미완료 Todo 조회 테스트
     */
    private static void testGetCompletedAndIncompleteTodos() {
        TestRunner.startTest("완료/미완료 Todo 조회");
        cleanupTestFile();
        
        TodoService service = createService();
        service.addTodo("미완료1", "설명", null);
        service.addTodo("미완료2", "설명", null);
        service.addTodo("완료될거", "설명", null);
        
        service.toggleTodo(2); // 세 번째 Todo 완료
        
        List<Todo> completed = service.getCompletedTodos();
        List<Todo> incomplete = service.getIncompleteTodos();
        
        TestRunner.assertEquals(1, completed.size(), "완료된 Todo 개수");
        TestRunner.assertEquals(2, incomplete.size(), "미완료 Todo 개수");
        TestRunner.assertEquals("완료될거", completed.get(0).getTitle(), "완료된 Todo 확인");
        TestRunner.assertEquals("미완료1", incomplete.get(0).getTitle(), "미완료 Todo 확인");
    }
    
    /**
     * 특정 인덱스 Todo 조회 테스트
     */
    private static void testGetTodoAt() {
        TestRunner.startTest("특정 인덱스 Todo 조회");
        cleanupTestFile();
        
        TodoService service = createService();
        service.addTodo("첫번째", "설명1", null);
        service.addTodo("두번째", "설명2", null);
        service.addTodo("세번째", "설명3", null);
        
        Todo todo1 = service.getTodoAt(0);
        Todo todo2 = service.getTodoAt(1);
        Todo todo3 = service.getTodoAt(2);
        
        TestRunner.assertEquals("첫번째", todo1.getTitle(), "첫 번째 Todo");
        TestRunner.assertEquals("두번째", todo2.getTitle(), "두 번째 Todo");
        TestRunner.assertEquals("세번째", todo3.getTitle(), "세 번째 Todo");
    }
    
    /**
     * Todo 개수 확인 테스트
     */
    private static void testGetTodoCount() {
        TestRunner.startTest("Todo 개수 확인");
        cleanupTestFile();
        
        TodoService service = createService();
        TestRunner.assertEquals(0, service.getTodoCount(), "초기 개수는 0");
        
        service.addTodo("1", "설명", null);
        TestRunner.assertEquals(1, service.getTodoCount(), "하나 추가 후");
        
        service.addTodo("2", "설명", null);
        service.addTodo("3", "설명", null);
        TestRunner.assertEquals(3, service.getTodoCount(), "세 개 추가 후");
        
        service.deleteTodo(0);
        TestRunner.assertEquals(2, service.getTodoCount(), "하나 삭제 후");
    }
    
    /**
     * 빈 리스트 테스트
     */
    private static void testEmptyList() {
        TestRunner.startTest("빈 리스트 처리");
        cleanupTestFile();
        
        TodoService service = createService();
        
        TestRunner.assertEquals(0, service.getTodoCount(), "초기 개수는 0");
        TestRunner.assertEquals(0, service.getAllTodos().size(), "빈 리스트 반환");
        TestRunner.assertEquals(0, service.getCompletedTodos().size(), "완료된 Todo 없음");
        TestRunner.assertEquals(0, service.getIncompleteTodos().size(), "미완료 Todo 없음");
        TestRunner.assertNull(service.getTodoAt(0), "인덱스 0에 Todo 없음");
        TestRunner.assertFalse(service.toggleTodo(0), "토글 실패");
        TestRunner.assertFalse(service.deleteTodo(0), "삭제 실패");
    }
    
    /**
     * 잘못된 인덱스 테스트
     */
    private static void testInvalidIndex() {
        TestRunner.startTest("잘못된 인덱스 처리");
        cleanupTestFile();
        
        TodoService service = createService();
        service.addTodo("유일한 Todo", "설명", null);
        
        // 음수 인덱스
        TestRunner.assertNull(service.getTodoAt(-1), "음수 인덱스");
        TestRunner.assertFalse(service.toggleTodo(-1), "음수 인덱스 토글");
        TestRunner.assertFalse(service.deleteTodo(-1), "음수 인덱스 삭제");
        
        // 범위 초과 인덱스
        TestRunner.assertNull(service.getTodoAt(1), "범위 초과 인덱스");
        TestRunner.assertFalse(service.toggleTodo(1), "범위 초과 토글");
        TestRunner.assertFalse(service.deleteTodo(1), "범위 초과 삭제");
        
        // 큰 인덱스
        TestRunner.assertNull(service.getTodoAt(100), "큰 인덱스");
        TestRunner.assertFalse(service.toggleTodo(100), "큰 인덱스 토글");
        TestRunner.assertFalse(service.deleteTodo(100), "큰 인덱스 삭제");
    }
    
    /**
     * 여러 Todo 관리 테스트
     */
    private static void testMultipleTodos() {
        TestRunner.startTest("여러 Todo 관리");
        cleanupTestFile();
        
        TodoService service = createService();
        
        // 10개 Todo 추가
        for (int i = 0; i < 10; i++) {
            service.addTodo("Todo " + i, "설명 " + i, null);
        }
        
        TestRunner.assertEquals(10, service.getTodoCount(), "10개 Todo 추가됨");
        
        // 짝수 인덱스 완료 처리
        for (int i = 0; i < 10; i += 2) {
            service.toggleTodo(i);
        }
        
        TestRunner.assertEquals(5, service.getCompletedTodos().size(), "5개 완료됨");
        TestRunner.assertEquals(5, service.getIncompleteTodos().size(), "5개 미완료");
        
        // 중간 삭제
        service.deleteTodo(5);
        TestRunner.assertEquals(9, service.getTodoCount(), "하나 삭제 후 9개");
        
        // 모두 삭제
        while (service.getTodoCount() > 0) {
            service.deleteTodo(0);
        }
        TestRunner.assertEquals(0, service.getTodoCount(), "모두 삭제됨");
    }
    
    /**
     * 영속성 테스트
     */
    private static void testPersistence() {
        TestRunner.startTest("데이터 영속성");
        cleanupTestFile();
        
        // 첫 번째 서비스로 데이터 저장
        TodoService service1 = createService();
        service1.addTodo("영속성 테스트", "저장될 데이터", LocalDate.now());
        service1.toggleTodo(0);
        
        // 두 번째 서비스로 데이터 로드
        TodoService service2 = createService();
        TestRunner.assertEquals(1, service2.getTodoCount(), "저장된 데이터 로드됨");
        
        Todo loaded = service2.getTodoAt(0);
        TestRunner.assertEquals("영속성 테스트", loaded.getTitle(), "제목 확인");
        TestRunner.assertTrue(loaded.isCompleted(), "완료 상태 확인");
        TestRunner.assertNotNull(loaded.getDueDate(), "마감일 확인");
    }
    
    private static TodoService createService() {
        TodoRepository repository = new TodoCsvRepository(TEST_FILE);
        return new TodoServiceImpl(repository);
    }
    
    private static void cleanupTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}