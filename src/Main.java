public class Main {
    public static void main(String[] args) {
        ITodoUI ui;
        
        if (args.length > 0 && "fancy".equalsIgnoreCase(args[0])) {
            ui = new FancyTodoUI();
        } else {
            ui = new BasicTodoUI();
        }
        
        ui.start();
    }
}
