package todo.domain;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

/**
 * Todo 항목을 나타내는 도메인 클래스
 * 
 * 이 클래스는 할 일의 핵심 정보와 상태를 관리합니다.
 * - 제목과 설명
 * - 완료 상태
 * - 마감일 (선택사항)
 * - 추가 메타데이터
 */
public class Todo {
    private String title;       // 할 일의 제목 (간단한 요약)
    private String description; // 할 일의 상세 설명
    private boolean completed;  // 완료 여부
    private LocalDate dueDate;  // 마감일 (선택사항)
    // 추가 정보를 저장할 수 있는 메타데이터 (키/값 쌍)
    private Map<String, String> metadata;

    /**
     * 기본 생성자: 제목과 설명만으로 Todo 생성
     * @param title 할 일 제목
     * @param description 할 일 설명
     */
    public Todo(String title, String description) {
        this(title, description, null, new HashMap<>());
    }

    /**
     * 마감일을 포함한 생성자
     * @param title 할 일 제목
     * @param description 할 일 설명
     * @param dueDate 마감일
     */
    public Todo(String title, String description, LocalDate dueDate) {
        this(title, description, dueDate, new HashMap<>());
    }

    /**
     * 전체 필드를 포함한 생성자
     * @param title 할 일 제목
     * @param description 할 일 설명
     * @param dueDate 마감일
     * @param metadata 추가 메타데이터
     */
    public Todo(String title, String description, LocalDate dueDate, Map<String, String> metadata) {
        this.title = title;
        this.description = description;
        this.completed = false; // 새로 생성된 할 일은 미완료 상태
        this.dueDate = dueDate;
        this.metadata = new HashMap<>();
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    /**
     * 할 일 제목 반환
     * @return 제목
     */
    public String getTitle() {
        return title;
    }

    /**
     * 할 일 설명 반환
     * @return 설명
     */
    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * 완료 상태 확인
     * @return 완료 여부
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 완료 상태 변경
     * @param completed 새로운 완료 상태
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadataField(String key, String value) {
        metadata.put(key, value);
    }

    public String getMetadataField(String key) {
        return metadata.get(key);
    }

    /**
     * Todo를 문자열로 표현 (이모지 사용)
     * @return 포맷된 문자열
     */
    @Override
    public String toString() {
        String base = (completed ? "✅ " : "❌ ") + title + " - " + description;
        if (dueDate != null) {
            base += " (마감일: " + dueDate + ")";
        }
        return base;
    }
}
