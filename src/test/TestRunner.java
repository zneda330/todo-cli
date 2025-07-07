package test;

/**
 * 간단한 테스트 러너 클래스
 * 
 * JUnit 없이 바닐라 자바로 테스트를 실행하기 위한 기본 프레임워크입니다.
 * assert 메서드들을 제공하여 테스트 검증을 수행합니다.
 */
public class TestRunner {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    
    /**
     * 테스트 시작을 알림
     * @param testName 테스트 이름
     */
    public static void startTest(String testName) {
        System.out.println("\n🧪 테스트 실행: " + testName);
        totalTests++;
    }
    
    /**
     * 두 값이 같은지 확인
     * @param expected 기대값
     * @param actual 실제값
     * @param message 테스트 설명
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            pass(message);
        } else if (expected != null && expected.equals(actual)) {
            pass(message);
        } else {
            fail(message + " (기대값: " + expected + ", 실제값: " + actual + ")");
        }
    }
    
    /**
     * 값이 참인지 확인
     * @param condition 검증할 조건
     * @param message 테스트 설명
     */
    public static void assertTrue(boolean condition, String message) {
        if (condition) {
            pass(message);
        } else {
            fail(message + " (조건이 거짓)");
        }
    }
    
    /**
     * 값이 거짓인지 확인
     * @param condition 검증할 조건
     * @param message 테스트 설명
     */
    public static void assertFalse(boolean condition, String message) {
        if (!condition) {
            pass(message);
        } else {
            fail(message + " (조건이 참)");
        }
    }
    
    /**
     * 값이 null이 아닌지 확인
     * @param object 검증할 객체
     * @param message 테스트 설명
     */
    public static void assertNotNull(Object object, String message) {
        if (object != null) {
            pass(message);
        } else {
            fail(message + " (null 값)");
        }
    }
    
    /**
     * 값이 null인지 확인
     * @param object 검증할 객체
     * @param message 테스트 설명
     */
    public static void assertNull(Object object, String message) {
        if (object == null) {
            pass(message);
        } else {
            fail(message + " (null이 아닌 값: " + object + ")");
        }
    }
    
    /**
     * 테스트 통과 처리
     * @param message 성공 메시지
     */
    private static void pass(String message) {
        System.out.println("  ✅ 통과: " + message);
        passedTests++;
    }
    
    /**
     * 테스트 실패 처리
     * @param message 실패 메시지
     */
    private static void fail(String message) {
        System.out.println("  ❌ 실패: " + message);
        failedTests++;
    }
    
    /**
     * 전체 테스트 결과 출력
     */
    public static void printSummary() {
        System.out.println("\n========================================");
        System.out.println("📊 테스트 결과 요약");
        System.out.println("========================================");
        System.out.println("전체: " + totalTests + "개");
        System.out.println("✅ 통과: " + passedTests + "개");
        System.out.println("❌ 실패: " + failedTests + "개");
        
        if (failedTests == 0) {
            System.out.println("\n🎉 모든 테스트가 통과했습니다! 🎉");
        } else {
            System.out.println("\n⚠️  일부 테스트가 실패했습니다.");
        }
    }
    
    /**
     * 테스트 통계 초기화
     */
    public static void reset() {
        totalTests = 0;
        passedTests = 0;
        failedTests = 0;
    }
}