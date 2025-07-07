package test;

import todo.application.TodoManager;
import todo.domain.Todo;
import todo.domain.TodoFilter;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * TodoManager 클래스 테스트
 * 
 * TodoManager의 비즈니스 로직을 검증합니다.
 */
public class TodoManagerTest {
    
    public static void main(String[] args) {
        TestRunner.reset();
        
        // 테스트 파일 정리
        cleanupTestFile();
        
        // 테스트 실행
        testAddTodo();
        testFetchTodos();
        testToggleTodo();
        testDeleteTodo();
        testTodoWithDueDate();
        
        // 엣지 케이스 테스트
        testInvalidIndexOperations();
        testEmptyListOperations();
        testLargeNumberOfTodos();
        testSingletonBehavior();
        
        // 테스트 파일 정리
        cleanupTestFile();
        
        // 결과 출력
        TestRunner.printSummary();
    }
    
    /**
     * Todo 추가 기능 테스트
     */
    private static void testAddTodo() {
        TestRunner.startTest("Todo 추가");
        
        TodoManager manager = TodoManager.getInstance();
        int initialCount = manager.getTodoCount();
        
        // Todo 추가
        manager.addTodo("테스트 할일", "테스트 설명");
        
        TestRunner.assertEquals(initialCount + 1, manager.getTodoCount(), "Todo가 추가됨");
        
        Todo addedTodo = manager.getTodoAt(manager.getTodoCount() - 1);
        TestRunner.assertNotNull(addedTodo, "추가된 Todo를 가져올 수 있음");
        TestRunner.assertEquals("테스트 할일", addedTodo.getTitle(), "제목이 올바름");
        TestRunner.assertEquals("테스트 설명", addedTodo.getDescription(), "설명이 올바름");
    }
    
    /**
     * Todo 조회 기능 테스트
     */
    private static void testFetchTodos() {
        TestRunner.startTest("Todo 필터링 조회");
        
        TodoManager manager = TodoManager.getInstance();
        
        // 테스트를 위한 Todo 추가
        manager.addTodo("완료된 할일", "이미 끝남");
        manager.addTodo("미완료 할일", "아직 안끝남");
        
        // 마지막에서 두 번째 Todo를 완료 처리
        int completeIndex = manager.getTodoCount() - 2;
        manager.toggleTodo(completeIndex);
        
        // 전체 조회
        List<Todo> allTodos = manager.fetchTodos(TodoFilter.ALL);
        TestRunner.assertTrue(allTodos.size() >= 2, "전체 Todo 조회");
        
        // 완료된 Todo 조회
        List<Todo> completedTodos = manager.fetchTodos(TodoFilter.COMPLETED);
        TestRunner.assertTrue(completedTodos.size() >= 1, "완료된 Todo가 존재");
        
        // 미완료 Todo 조회
        List<Todo> incompleteTodos = manager.fetchTodos(TodoFilter.INCOMPLETE);
        TestRunner.assertTrue(incompleteTodos.size() >= 1, "미완료 Todo가 존재");
    }
    
    /**
     * Todo 상태 토글 테스트
     */
    private static void testToggleTodo() {
        TestRunner.startTest("Todo 상태 토글");
        
        TodoManager manager = TodoManager.getInstance();
        
        // 새 Todo 추가
        manager.addTodo("토글 테스트", "상태 변경 테스트");
        int index = manager.getTodoCount() - 1;
        
        Todo todo = manager.getTodoAt(index);
        TestRunner.assertFalse(todo.isCompleted(), "초기 상태는 미완료");
        
        // 토글
        manager.toggleTodo(index);
        TestRunner.assertTrue(manager.getTodoAt(index).isCompleted(), "완료 상태로 변경됨");
        
        // 다시 토글
        manager.toggleTodo(index);
        TestRunner.assertFalse(manager.getTodoAt(index).isCompleted(), "다시 미완료로 변경됨");
    }
    
    /**
     * Todo 삭제 테스트
     */
    private static void testDeleteTodo() {
        TestRunner.startTest("Todo 삭제");
        
        TodoManager manager = TodoManager.getInstance();
        
        // 삭제할 Todo 추가
        manager.addTodo("삭제될 할일", "이것은 삭제될 예정");
        int countBefore = manager.getTodoCount();
        int deleteIndex = countBefore - 1;
        
        // 삭제
        manager.deleteTodo(deleteIndex);
        
        TestRunner.assertEquals(countBefore - 1, manager.getTodoCount(), "Todo가 삭제됨");
        
        // 잘못된 인덱스로 삭제 시도
        int invalidIndex = manager.getTodoCount() + 10;
        manager.deleteTodo(invalidIndex);
        TestRunner.assertEquals(countBefore - 1, manager.getTodoCount(), "잘못된 인덱스는 무시됨");
    }
    
    /**
     * 마감일이 있는 Todo 테스트
     */
    private static void testTodoWithDueDate() {
        TestRunner.startTest("마감일이 있는 Todo 추가");
        
        TodoManager manager = TodoManager.getInstance();
        LocalDate dueDate = LocalDate.of(2025, 12, 31);
        
        // 마감일이 있는 Todo 추가
        manager.addTodo("연말 목표", "올해가 끝나기 전에 완료", dueDate);
        
        Todo todo = manager.getTodoAt(manager.getTodoCount() - 1);
        TestRunner.assertNotNull(todo.getDueDate(), "마감일이 설정됨");
        TestRunner.assertEquals(dueDate, todo.getDueDate(), "마감일이 올바르게 저장됨");
    }
    
    /**
     * 테스트용 CSV 파일 정리
     */
    private static void cleanupTestFile() {
        File testFile = new File("todos.csv");
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    /**
     * 잘못된 인덱스 연산 테스트
     */
    private static void testInvalidIndexOperations() {
        TestRunner.startTest("잘못된 인덱스 연산");
        
        TodoManager manager = TodoManager.getInstance();
        int count = manager.getTodoCount();
        
        // 음수 인덱스
        TestRunner.assertFalse(manager.isValidIndex(-1), "음수 인덱스는 유효하지 않음");
        manager.toggleTodo(-1);  // 예외가 발생하지 않아야 함
        manager.deleteTodo(-1);  // 예외가 발생하지 않아야 함
        TestRunner.assertEquals(count, manager.getTodoCount(), "음수 인덱스 삭제는 무시됨");
        
        // 범위를 벗어난 인덱스
        int largeIndex = count + 100;
        TestRunner.assertFalse(manager.isValidIndex(largeIndex), "큰 인덱스는 유효하지 않음");
        TestRunner.assertNull(manager.getTodoAt(largeIndex), "범위 밖 인덱스는 null 반환");
        
        // 경계값 테스트
        if (count > 0) {
            TestRunner.assertTrue(manager.isValidIndex(0), "첫 번째 인덱스는 유효");
            TestRunner.assertTrue(manager.isValidIndex(count - 1), "마지막 인덱스는 유효");
            TestRunner.assertFalse(manager.isValidIndex(count), "크기와 같은 인덱스는 무효");
        }
    }
    
    /**
     * 빈 리스트 연산 테스트
     */
    private static void testEmptyListOperations() {
        TestRunner.startTest("빈 리스트 연산");
        
        // 싱글톤이므로 기존 데이터를 모두 삭제
        TodoManager manager = TodoManager.getInstance();
        while (manager.getTodoCount() > 0) {
            manager.deleteTodo(0);
        }
        
        // 빈 리스트 확인
        TestRunner.assertEquals(0, manager.getTodoCount(), "초기 상태는 비어있음");
        TestRunner.assertFalse(manager.hasCompletedTodos(), "완료된 Todo 없음");
        TestRunner.assertFalse(manager.hasIncompleteTodos(), "미완료 Todo 없음");
        
        // 빈 리스트에서 연산
        manager.toggleTodo(0);  // 예외가 발생하지 않아야 함
        manager.deleteTodo(0);  // 예외가 발생하지 않아야 함
        TestRunner.assertNull(manager.getTodoAt(0), "빈 리스트에서 조회는 null");
        
        // 필터링 테스트
        TestRunner.assertEquals(0, manager.fetchTodos(TodoFilter.ALL).size(), "전체 필터링 결과 비어있음");
        TestRunner.assertEquals(0, manager.fetchTodos(TodoFilter.COMPLETED).size(), "완료 필터링 결과 비어있음");
        TestRunner.assertEquals(0, manager.fetchTodos(TodoFilter.INCOMPLETE).size(), "미완료 필터링 결과 비어있음");
    }
    
    /**
     * 많은 수의 Todo 처리 테스트
     */
    private static void testLargeNumberOfTodos() {
        TestRunner.startTest("많은 Todo 처리");
        
        TodoManager manager = TodoManager.getInstance();
        int initialCount = manager.getTodoCount();
        int testCount = 100;
        
        // 100개의 Todo 추가
        for (int i = 0; i < testCount; i++) {
            manager.addTodo("Todo #" + i, "Description #" + i);
        }
        
        TestRunner.assertEquals(initialCount + testCount, manager.getTodoCount(), 
            testCount + "개의 Todo 추가됨");
        
        // 절반을 완료 처리
        for (int i = 0; i < testCount / 2; i++) {
            manager.toggleTodo(initialCount + i);
        }
        
        List<Todo> completed = manager.fetchTodos(TodoFilter.COMPLETED);
        TestRunner.assertTrue(completed.size() >= testCount / 2, "절반 이상이 완료됨");
        
        // 마지막 50개 삭제
        for (int i = 0; i < testCount / 2; i++) {
            manager.deleteTodo(manager.getTodoCount() - 1);
        }
        
        TestRunner.assertEquals(initialCount + testCount / 2, manager.getTodoCount(), 
            "절반이 삭제됨");
    }
    
    /**
     * 싱글톤 동작 테스트
     */
    private static void testSingletonBehavior() {
        TestRunner.startTest("싱글톤 패턴 동작");
        
        TodoManager manager1 = TodoManager.getInstance();
        TodoManager manager2 = TodoManager.getInstance();
        
        TestRunner.assertTrue(manager1 == manager2, "같은 인스턴스 반환");
        
        // 한 인스턴스에서 추가
        int countBefore = manager1.getTodoCount();
        manager1.addTodo("싱글톤 테스트", "공유 인스턴스 확인");
        
        // 다른 참조에서 확인
        TestRunner.assertEquals(countBefore + 1, manager2.getTodoCount(), 
            "다른 참조에서도 변경사항 반영됨");
        
        Todo lastTodo = manager2.getTodoAt(manager2.getTodoCount() - 1);
        TestRunner.assertEquals("싱글톤 테스트", lastTodo.getTitle(), 
            "다른 참조에서도 같은 데이터 접근");
    }
}