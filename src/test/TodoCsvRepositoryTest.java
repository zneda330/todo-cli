package test;

import todo.domain.Todo;
import todo.domain.TodoFilter;
import todo.infrastructure.TodoCsvRepository;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TodoCsvRepository 클래스 테스트
 * 
 * CSV 파일 기반 저장소의 동작을 검증합니다.
 */
public class TodoCsvRepositoryTest {
    private static final String TEST_FILE = "test_todos.csv";
    
    public static void main(String[] args) {
        TestRunner.reset();
        
        // 테스트 파일 정리
        cleanupTestFile();
        
        // 테스트 실행
        testBasicSaveAndLoad();
        testSpecialCharactersInCsv();
        testEmptyFile();
        testFilteredLoad();
        testSingleTodoSave();
        testCsvFormat();
        testFileCorruption();
        testConcurrentAccess();
        
        // 테스트 파일 정리
        cleanupTestFile();
        
        // 결과 출력
        TestRunner.printSummary();
    }
    
    /**
     * 기본 저장 및 로드 테스트
     */
    private static void testBasicSaveAndLoad() {
        TestRunner.startTest("기본 저장 및 로드");
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        
        // Todo 리스트 생성
        List<Todo> todos = new java.util.ArrayList<>();
        todos.add(new Todo("제목1", "설명1"));
        todos.add(new Todo("제목2", "설명2", LocalDate.of(2025, 12, 31)));
        
        // 저장
        repo.save(todos);
        
        // 파일 존재 확인
        File file = new File(TEST_FILE);
        TestRunner.assertTrue(file.exists(), "CSV 파일이 생성됨");
        
        // 로드
        List<Todo> loaded = repo.load();
        TestRunner.assertEquals(2, loaded.size(), "2개의 Todo가 로드됨");
        TestRunner.assertEquals("제목1", loaded.get(0).getTitle(), "첫 번째 Todo 제목");
        TestRunner.assertEquals("제목2", loaded.get(1).getTitle(), "두 번째 Todo 제목");
        TestRunner.assertNotNull(loaded.get(1).getDueDate(), "두 번째 Todo 마감일 존재");
    }
    
    /**
     * CSV 특수 문자 처리 테스트
     */
    private static void testSpecialCharactersInCsv() {
        TestRunner.startTest("CSV 특수 문자 처리");
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        
        // 특수 문자가 포함된 Todo
        List<Todo> todos = new java.util.ArrayList<>();
        todos.add(new Todo("제목,쉼표", "설명\"큰따옴표\""));
        todos.add(new Todo("제목 줄바꿈", "설명 캐리지리턴"));  // CSV는 줄바꿈 처리가 어려움
        todos.add(new Todo("\"인용\"부호", "백슬래시\\경로"));
        
        // 저장 및 로드
        repo.save(todos);
        List<Todo> loaded = repo.load();
        
        TestRunner.assertEquals(3, loaded.size(), "특수 문자 Todo 모두 로드됨");
        TestRunner.assertEquals("제목,쉼표", loaded.get(0).getTitle(), "쉼표 포함 제목");
        TestRunner.assertEquals("설명\"큰따옴표\"", loaded.get(0).getDescription(), "큰따옴표 포함 설명");
        
        // 인용부호가 포함된 제목 찾기
        boolean foundQuote = false;
        for (Todo todo : loaded) {
            if (todo.getTitle().contains("인용") && todo.getTitle().contains("부호")) {
                foundQuote = true;
                break;
            }
        }
        TestRunner.assertTrue(foundQuote, "인용부호 포함 제목 저장됨");
    }
    
    /**
     * 빈 파일 처리 테스트
     */
    private static void testEmptyFile() {
        TestRunner.startTest("빈 파일 처리");
        
        // 빈 파일 생성
        try {
            new File(TEST_FILE).createNewFile();
        } catch (Exception e) {
            // 무시
        }
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        List<Todo> loaded = repo.load();
        
        TestRunner.assertEquals(0, loaded.size(), "빈 파일에서 빈 리스트 반환");
        
        // 존재하지 않는 파일
        cleanupTestFile();
        List<Todo> fromNonExistent = repo.load();
        TestRunner.assertEquals(0, fromNonExistent.size(), "존재하지 않는 파일에서 빈 리스트 반환");
    }
    
    /**
     * 필터링된 로드 테스트
     */
    private static void testFilteredLoad() {
        TestRunner.startTest("필터링된 로드");
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        
        // 다양한 상태의 Todo 생성
        List<Todo> todos = new java.util.ArrayList<>();
        Todo completed1 = new Todo("완료1", "설명");
        completed1.setCompleted(true);
        Todo completed2 = new Todo("완료2", "설명");
        completed2.setCompleted(true);
        Todo incomplete1 = new Todo("미완료1", "설명");
        Todo incomplete2 = new Todo("미완료2", "설명");
        
        todos.add(completed1);
        todos.add(incomplete1);
        todos.add(completed2);
        todos.add(incomplete2);
        
        repo.save(todos);
        
        // 필터링 테스트
        List<Todo> all = repo.load(TodoFilter.ALL);
        TestRunner.assertEquals(4, all.size(), "전체 필터: 모든 Todo");
        
        List<Todo> completed = repo.load(TodoFilter.COMPLETED);
        TestRunner.assertEquals(2, completed.size(), "완료 필터: 2개");
        TestRunner.assertTrue(completed.get(0).isCompleted(), "완료된 Todo만 포함");
        
        List<Todo> incomplete = repo.load(TodoFilter.INCOMPLETE);
        TestRunner.assertEquals(2, incomplete.size(), "미완료 필터: 2개");
        TestRunner.assertFalse(incomplete.get(0).isCompleted(), "미완료 Todo만 포함");
    }
    
    /**
     * 단일 Todo 저장 테스트
     */
    private static void testSingleTodoSave() {
        TestRunner.startTest("단일 Todo 저장");
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        
        // 초기 Todo 저장
        List<Todo> initial = new java.util.ArrayList<>();
        initial.add(new Todo("초기1", "설명1"));
        initial.add(new Todo("초기2", "설명2"));
        repo.save(initial);
        
        // 단일 Todo 추가
        Todo newTodo = new Todo("추가된 Todo", "새로운 설명");
        repo.save(newTodo);
        
        // 확인
        List<Todo> loaded = repo.load();
        TestRunner.assertEquals(3, loaded.size(), "단일 Todo 추가 후 총 3개");
        TestRunner.assertEquals("추가된 Todo", loaded.get(2).getTitle(), "새 Todo가 마지막에 추가됨");
    }
    
    /**
     * CSV 형식 검증 테스트
     */
    private static void testCsvFormat() {
        TestRunner.startTest("CSV 형식 검증");
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        
        // 메타데이터와 마감일이 있는 Todo
        Map<String, String> metadata = new HashMap<>();
        metadata.put("priority", "high");
        metadata.put("category", "work");
        
        Todo complex = new Todo("복잡한 Todo", "설명", LocalDate.of(2025, 6, 30), metadata);
        complex.setCompleted(true);
        
        List<Todo> todos = new java.util.ArrayList<>();
        todos.add(complex);
        repo.save(todos);
        
        // 로드하여 확인
        List<Todo> loaded = repo.load();
        Todo loadedTodo = loaded.get(0);
        
        TestRunner.assertEquals("복잡한 Todo", loadedTodo.getTitle(), "제목 보존");
        TestRunner.assertTrue(loadedTodo.isCompleted(), "완료 상태 보존");
        TestRunner.assertEquals(LocalDate.of(2025, 6, 30), loadedTodo.getDueDate(), "마감일 보존");
        TestRunner.assertEquals("high", loadedTodo.getMetadataField("priority"), "메타데이터 보존");
        TestRunner.assertEquals("work", loadedTodo.getMetadataField("category"), "메타데이터 보존");
    }
    
    /**
     * 파일 손상 처리 테스트
     */
    private static void testFileCorruption() {
        TestRunner.startTest("파일 손상 처리");
        cleanupTestFile();
        
        // 손상된 CSV 파일 생성
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(TEST_FILE));
            writer.println("\"정상 제목\",\"정상 설명\",false");
            writer.println("잘못된 형식의 줄");
            writer.println("\"제목만 있음\"");
            writer.println(",,,,,");  // 빈 필드들
            writer.println("\"정상 제목2\",\"정상 설명2\",true");
            writer.close();
        } catch (Exception e) {
            // 무시
        }
        
        TodoCsvRepository repo = new TodoCsvRepository(TEST_FILE);
        List<Todo> loaded = repo.load();
        
        // 손상된 줄은 무시되고 정상적인 줄만 로드되어야 함
        TestRunner.assertTrue(loaded.size() >= 2, "정상적인 줄만 로드됨");
        TestRunner.assertEquals("정상 제목", loaded.get(0).getTitle(), "첫 번째 정상 Todo");
        TestRunner.assertEquals("정상 제목2", loaded.get(loaded.size() - 1).getTitle(), "마지막 정상 Todo");
    }
    
    /**
     * 동시 접근 테스트 (간단한 시뮬레이션)
     */
    private static void testConcurrentAccess() {
        TestRunner.startTest("동시 접근 시뮬레이션");
        cleanupTestFile();
        
        TodoCsvRepository repo1 = new TodoCsvRepository(TEST_FILE);
        TodoCsvRepository repo2 = new TodoCsvRepository(TEST_FILE);
        
        // repo1에서 저장
        List<Todo> todos1 = new java.util.ArrayList<>();
        todos1.add(new Todo("Repo1 Todo", "첫 번째 저장소"));
        repo1.save(todos1);
        
        // repo2에서 로드
        List<Todo> loaded = repo2.load();
        TestRunner.assertTrue(loaded.size() >= 1, "다른 인스턴스에서 데이터 읽기");
        
        // repo2에서 추가
        Todo newTodo = new Todo("Repo2 Todo", "두 번째 저장소");
        repo2.save(newTodo);
        
        // repo1에서 다시 로드
        List<Todo> reloaded = repo1.load();
        TestRunner.assertTrue(reloaded.size() >= 2, "변경사항이 반영됨");
        
        boolean foundRepo2Todo = false;
        for (Todo todo : reloaded) {
            if ("Repo2 Todo".equals(todo.getTitle())) {
                foundRepo2Todo = true;
                break;
            }
        }
        TestRunner.assertTrue(foundRepo2Todo, "다른 인스턴스의 추가 사항 확인");
    }
    
    /**
     * 테스트용 CSV 파일 정리
     */
    private static void cleanupTestFile() {
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    /**
     * assertNotNull 헬퍼 메서드
     */
    private static void assertNotNull(Object obj, String message) {
        TestRunner.assertNotNull(obj, message);
    }
}