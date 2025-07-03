public interface ITodoUI {
    void start();
    void displayWelcome();
    void displayMenu();
    void displayGoodbye();
    void handleAddTodo();
    void handleDisplayTodos();
    void handleToggleTodo();
}