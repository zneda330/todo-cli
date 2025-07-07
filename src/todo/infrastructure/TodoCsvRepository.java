package todo.infrastructure;
import todo.domain.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

/**
 * CSV 파일을 사용하여 Todo를 저장하고 로드하는 저장소 구현체
 * 
 * TodoRepository 인터페이스를 구현하여 파일 기반의 영속성을 제공합니다.
 * CSV 형식: "제목","설명",완료여부,메타데이터
 */
public class TodoCsvRepository implements TodoRepository {
    private final String filePath;  // CSV 파일 경로

    public TodoCsvRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * CSV 파일에서 모든 Todo를 로드
     * 각 줄의 형식: "제목","설명",완료여부,메타데이터
     * @return 전체 Todo 목록
     */
    public List<Todo> load() {
        return load(TodoFilter.ALL);
    }

    /**
     * 완료 상태에 따라 필터링된 Todo를 로드
     * @param filter 로드할 Todo의 필터 (ALL, COMPLETED, INCOMPLETE)
     * @return 필터링된 Todo 목록
     */
    public List<Todo> load(TodoFilter filter) {
        List<Todo> todos = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return todos;  // 파일이 없으면 빈 목록 반환
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // 콤마로 분리 (큰따옴표 내부의 콤마는 무시): 제목, 설명, 완료여부, 메타데이터
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 4);
                if (parts.length >= 3) {
                    String title = unquote(parts[0]);
                    String description = unquote(parts[1]);
                    boolean completed = Boolean.parseBoolean(parts[2]);
                    Map<String, String> metadata = new HashMap<>();
                    LocalDate dueDate = null;
                    
                    // 메타데이터 파싱 (마감일 포함)
                    if (parts.length == 4) {
                        for (String entry : parts[3].split(";")) {
                            if (entry.isEmpty()) continue;
                            String[] kv = entry.split("=", 2);
                            if (kv.length == 2) {
                                if ("due".equals(kv[0])) {
                                    try {
                                        dueDate = LocalDate.parse(kv[1]);  // 마감일 파싱
                                    } catch (Exception ignored) {}
                                } else {
                                    metadata.put(kv[0], kv[1]);
                                }
                            }
                        }
                    }
                    Todo todo = new Todo(title, description, dueDate, metadata);
                    todo.setCompleted(completed);
                    
                    // 필터 적용
                    if (filter == TodoFilter.ALL
                            || (filter == TodoFilter.COMPLETED && todo.isCompleted())
                            || (filter == TodoFilter.INCOMPLETE && !todo.isCompleted())) {
                        todos.add(todo);
                    }
                }
            }
        } catch (IOException e) {
            // 파일 읽기 오류는 무시하고 빈 목록 반환
        }
        return todos;
    }

    /**
     * 인덱스로 특정 Todo를 가져오기
     * @param index 0부터 시작하는 인덱스
     * @return 해당 인덱스의 Todo 또는 범위를 벗어나면 null
     */
    public Todo get(int index) {
        List<Todo> all = load();
        if (index >= 0 && index < all.size()) {
            return all.get(index);
        }
        return null;
    }

    /**
     * Todo 목록을 CSV 파일로 저장
     * @param todos 저장할 Todo 목록
     */
    public void save(List<Todo> todos) {
        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Todo todo : todos) {
                // 메타데이터 문자열 구성
                StringBuilder metadata = new StringBuilder();
                if (todo.getDueDate() != null) {
                    metadata.append("due=").append(todo.getDueDate());  // 마감일 추가
                }
                for (Map.Entry<String, String> e : todo.getMetadata().entrySet()) {
                    if (metadata.length() > 0) metadata.append(';');
                    metadata.append(e.getKey()).append('=').append(e.getValue());
                }
                String metaString = metadata.toString();
                String line = String.format("%s,%s,%b",
                        quote(todo.getTitle()),
                        quote(todo.getDescription()),
                        todo.isCompleted());
                if (!metaString.isEmpty()) {
                    line = line + "," + metaString;
                }
                writer.println(line);
            }
        } catch (IOException e) {
            // 파일 쓰기 오류는 무시
        }
    }

    /**
     * 문자열을 CSV용 큰따옴표로 감싸기
     * @param text 원본 문자열
     * @return 큰따옴표로 감싼 문자열
     */
    private String quote(String text) {
        String escaped = text.replace("\"", "\"\"");  // 큰따옴표 이스케이프
        return "\"" + escaped + "\"";
    }

    /**
     * CSV용 큰따옴표를 제거하고 원본 문자열 반환
     * @param text 큰따옴표로 감싼 문자열
     * @return 원본 문자열
     */
    private String unquote(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            String inner = text.substring(1, text.length() - 1);
            return inner.replace("\"\"", "\"");  // 이스케이프된 큰따옴표 복원
        }
        return text;
    }
    
    /**
     * 단일 Todo를 저장소에 추가
     * 기존 Todo 목록을 읽고, 새 Todo를 추가한 후 전체를 다시 저장합니다.
     * @param todo 추가할 Todo 항목
     */
    public void save(Todo todo) {
        // 기존 Todo 목록을 읽어옴
        List<Todo> todos = load();
        // 새 Todo 추가
        todos.add(todo);
        // 전체 목록을 다시 저장
        save(todos);
    }
}
