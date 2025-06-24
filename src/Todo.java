public class Todo {
    private String description; // 할일 내용
    private boolean completed;  // 완료 여부

    // 생성자: 새로운 할일을 만들 때 사용
    public Todo(String description) {
        this.description = description;
        this.completed = false; // 새로 만든 할일은 미완료 상태
    }

    // 할일 내용을 가져오는 메서드
    public String getDescription() {
        return description;
    }

    // 완료 여부를 확인하는 메서드
    public boolean isCompleted() {
        return completed;
    }

    // 완료 상태를 변경하는 메서드
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // 할일을 문자열로 표현하는 메서드 (이모지 사용)
    @Override
    public String toString() {
        return (completed ? "✅ " : "❌") + description;
    }
}
