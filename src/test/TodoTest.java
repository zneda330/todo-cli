package test;

import todo.domain.Todo;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Todo 도메인 클래스 테스트
 * 
 * Todo 클래스의 기본 기능들을 검증합니다.
 */
public class TodoTest {
    
    public static void main(String[] args) {
        TestRunner.reset();
        
        // 테스트 실행
        testTodoCreation();
        testTodoCompletion();
        testTodoWithDueDate();
        testTodoWithMetadata();
        testTodoToString();
        
        // 엣지 케이스 테스트
        testTodoWithEmptyStrings();
        testTodoWithSpecialCharacters();
        testTodoWithVeryLongStrings();
        testTodoWithNullValues();
        
        // 결과 출력
        TestRunner.printSummary();
    }
    
    /**
     * Todo 생성 테스트
     */
    private static void testTodoCreation() {
        TestRunner.startTest("Todo 생성");
        
        Todo todo = new Todo("테스트 제목", "테스트 설명");
        
        TestRunner.assertEquals("테스트 제목", todo.getTitle(), "제목이 올바르게 설정됨");
        TestRunner.assertEquals("테스트 설명", todo.getDescription(), "설명이 올바르게 설정됨");
        TestRunner.assertFalse(todo.isCompleted(), "새 Todo는 미완료 상태");
        TestRunner.assertNull(todo.getDueDate(), "기본 생성자는 마감일이 없음");
    }
    
    /**
     * Todo 완료 상태 변경 테스트
     */
    private static void testTodoCompletion() {
        TestRunner.startTest("Todo 완료 상태 변경");
        
        Todo todo = new Todo("테스트", "설명");
        
        TestRunner.assertFalse(todo.isCompleted(), "초기 상태는 미완료");
        
        todo.setCompleted(true);
        TestRunner.assertTrue(todo.isCompleted(), "완료 상태로 변경됨");
        
        todo.setCompleted(false);
        TestRunner.assertFalse(todo.isCompleted(), "다시 미완료 상태로 변경됨");
    }
    
    /**
     * 마감일이 있는 Todo 테스트
     */
    private static void testTodoWithDueDate() {
        TestRunner.startTest("마감일이 있는 Todo");
        
        LocalDate dueDate = LocalDate.of(2025, 12, 31);
        Todo todo = new Todo("연말 목표", "올해 목표 달성", dueDate);
        
        TestRunner.assertNotNull(todo.getDueDate(), "마감일이 설정됨");
        TestRunner.assertEquals(dueDate, todo.getDueDate(), "마감일이 올바르게 설정됨");
        
        // 마감일 변경 테스트
        LocalDate newDueDate = LocalDate.of(2026, 1, 1);
        todo.setDueDate(newDueDate);
        TestRunner.assertEquals(newDueDate, todo.getDueDate(), "마감일이 변경됨");
    }
    
    /**
     * 메타데이터가 있는 Todo 테스트
     */
    private static void testTodoWithMetadata() {
        TestRunner.startTest("메타데이터가 있는 Todo");
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("priority", "high");
        metadata.put("category", "work");
        
        Todo todo = new Todo("중요 업무", "긴급 처리 필요", null, metadata);
        
        TestRunner.assertEquals("high", todo.getMetadataField("priority"), "우선순위 메타데이터 확인");
        TestRunner.assertEquals("work", todo.getMetadataField("category"), "카테고리 메타데이터 확인");
        
        // 메타데이터 추가
        todo.setMetadataField("status", "in-progress");
        TestRunner.assertEquals("in-progress", todo.getMetadataField("status"), "새 메타데이터 추가됨");
    }
    
    /**
     * Todo toString 메서드 테스트
     */
    private static void testTodoToString() {
        TestRunner.startTest("Todo toString 메서드");
        
        // 기본 Todo
        Todo todo1 = new Todo("제목", "설명");
        String str1 = todo1.toString();
        TestRunner.assertTrue(str1.contains("❌"), "미완료 Todo는 ❌ 포함");
        TestRunner.assertTrue(str1.contains("제목"), "제목이 포함됨");
        TestRunner.assertTrue(str1.contains("설명"), "설명이 포함됨");
        
        // 완료된 Todo
        todo1.setCompleted(true);
        String str2 = todo1.toString();
        TestRunner.assertTrue(str2.contains("✅"), "완료 Todo는 ✅ 포함");
        
        // 마감일이 있는 Todo
        LocalDate dueDate = LocalDate.of(2025, 12, 31);
        Todo todo2 = new Todo("마감일 테스트", "설명", dueDate);
        String str3 = todo2.toString();
        TestRunner.assertTrue(str3.contains("마감일:"), "마감일 정보 포함");
        TestRunner.assertTrue(str3.contains("2025-12-31"), "마감일 날짜 포함");
    }
    
    
    /**
     * 빈 문자열로 Todo 생성 테스트
     */
    private static void testTodoWithEmptyStrings() {
        TestRunner.startTest("빈 문자열 Todo");
        
        Todo todo1 = new Todo("", "");
        TestRunner.assertEquals("", todo1.getTitle(), "빈 제목 허용");
        TestRunner.assertEquals("", todo1.getDescription(), "빈 설명 허용");
        
        Todo todo2 = new Todo(" ", " ");
        TestRunner.assertEquals(" ", todo2.getTitle(), "공백만 있는 제목");
        TestRunner.assertEquals(" ", todo2.getDescription(), "공백만 있는 설명");
    }
    
    /**
     * 특수 문자가 포함된 Todo 테스트
     */
    private static void testTodoWithSpecialCharacters() {
        TestRunner.startTest("특수 문자 Todo");
        
        String specialTitle = "Todo with @#$%^&*()_+-={}[]|\\:\";<>?,./";
        String specialDesc = "설명: 한글, English, 123, 🎉🎊✨";
        
        Todo todo = new Todo(specialTitle, specialDesc);
        TestRunner.assertEquals(specialTitle, todo.getTitle(), "특수 문자 제목 저장");
        TestRunner.assertEquals(specialDesc, todo.getDescription(), "이모지 포함 설명 저장");
        
        // CSV 저장시 문제가 될 수 있는 문자들
        Todo csvProblem = new Todo("제목,쉼표", "설명\"큰따옴표\"");
        TestRunner.assertEquals("제목,쉼표", csvProblem.getTitle(), "쉼표가 있는 제목");
        TestRunner.assertEquals("설명\"큰따옴표\"", csvProblem.getDescription(), "큰따옴표가 있는 설명");
    }
    
    /**
     * 매우 긴 문자열 테스트
     */
    private static void testTodoWithVeryLongStrings() {
        TestRunner.startTest("매우 긴 문자열 Todo");
        
        // 1000자 문자열 생성
        StringBuilder longTitle = new StringBuilder();
        StringBuilder longDesc = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longTitle.append("긴제목테스트");
            longDesc.append("긴설명테스트");
        }
        
        Todo todo = new Todo(longTitle.toString(), longDesc.toString());
        TestRunner.assertEquals(longTitle.toString(), todo.getTitle(), "긴 제목 저장");
        TestRunner.assertEquals(longDesc.toString(), todo.getDescription(), "긴 설명 저장");
        TestRunner.assertTrue(todo.getTitle().length() >= 500, "제목 길이 확인");
        TestRunner.assertTrue(todo.getDescription().length() >= 500, "설명 길이 확인");
    }
    
    /**
     * null 값 처리 테스트
     */
    private static void testTodoWithNullValues() {
        TestRunner.startTest("null 값 처리");
        
        // null 메타데이터로 생성
        Todo todo1 = new Todo("제목", "설명", null, null);
        TestRunner.assertNotNull(todo1.getMetadata(), "null 메타데이터는 빈 Map으로 초기화");
        TestRunner.assertEquals(0, todo1.getMetadata().size(), "메타데이터 Map이 비어있음");
        
        // null 마감일 설정
        todo1.setDueDate(null);
        TestRunner.assertNull(todo1.getDueDate(), "null 마감일 설정 가능");
        
        // 메타데이터에서 존재하지 않는 키 조회
        TestRunner.assertNull(todo1.getMetadataField("nonexistent"), "존재하지 않는 메타데이터는 null");
    }
}