package todo.domain;

import java.util.List;

public interface TodoRepository {
    List<Todo> load();
    List<Todo> load(TodoFilter filter);
    Todo get(int index);
    void save(List<Todo> todos);
}
