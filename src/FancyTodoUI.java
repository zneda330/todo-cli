import java.util.*;

public class FancyTodoUI implements ITodoUI {
    private Scanner scanner;
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String CYAN = "\033[96m";
    private static final String PURPLE = "\033[95m";
    private static final String GREEN = "\033[92m";
    private static final String YELLOW = "\033[93m";
    private static final String RED = "\033[91m";
    private static final String BLUE = "\033[94m";
    
    private static final int DEFAULT_WIDTH = 80;

    public FancyTodoUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        displayWelcome();
        
        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            
            if (handleChoice(choice)) {
                break;
            }
        }
        
        displayGoodbye();
    }

    public void displayWelcome() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String emptyLine = createLine("║", " ", "║", width);
        
        System.out.println(CYAN + topBorder + RESET);
        System.out.println(CYAN + emptyLine + RESET);
        
        String stars = repeatString("✨ ", (width - 6) / 4);
        System.out.println(CYAN + "║ " + BOLD + PURPLE + stars + RESET + CYAN + " ║" + RESET);
        
        String title = "🌟 FANCY TODO MANAGER 🌟";
        String titleLine = createCenteredLine("║", title, "║", width);
        System.out.println(CYAN + "║ " + BOLD + YELLOW + titleLine.substring(2, titleLine.length() - 2) + RESET + CYAN + " ║" + RESET);
        
        System.out.println(CYAN + "║ " + BOLD + PURPLE + stars + RESET + CYAN + " ║" + RESET);
        System.out.println(CYAN + emptyLine + RESET);
        
        String welcome = "🎉 Welcome! Let's manage your todos in style! 🎉";
        String welcomeLine = createCenteredLine("║", welcome, "║", width);
        System.out.println(CYAN + "║ " + BOLD + GREEN + welcomeLine.substring(2, welcomeLine.length() - 2) + RESET + CYAN + " ║" + RESET);
        
        System.out.println(CYAN + emptyLine + RESET);
        System.out.println(CYAN + bottomBorder + RESET);
        System.out.println();
        pause();
    }

    public void displayMenu() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String middleBorder = createBorder("╠", "═", "╣", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String emptyLine = createLine("║", " ", "║", width);
        
        System.out.println(BOLD + BLUE + topBorder + RESET);
        
        String menuTitle = "🎯 MENU SELECTION 🎯";
        String titleLine = createCenteredLine("║", menuTitle, "║", width);
        System.out.println(BOLD + BLUE + "║" + titleLine.substring(1, titleLine.length() - 1) + "║" + RESET);
        
        System.out.println(BOLD + BLUE + middleBorder + RESET);
        System.out.println(BOLD + BLUE + emptyLine + RESET);
        
        String[] menuItems = {
            "➕ 1. Add Todo",
            "📋 2. View Todo List", 
            "🔄 3. Toggle Todo Status",
            "🚪 4. Exit Program"
        };
        
        String[] colors = {GREEN, YELLOW, PURPLE, RED};
        
        for (int i = 0; i < menuItems.length; i++) {
            String menuLine = createLeftAlignedLine("║", menuItems[i], "║", width, 4);
            System.out.println(BOLD + BLUE + "║" + BOLD + colors[i] + menuLine.substring(1, menuLine.length() - 1) + RESET + BLUE + "║" + RESET);
        }
        
        System.out.println(BOLD + BLUE + emptyLine + RESET);
        System.out.println(BOLD + BLUE + bottomBorder + RESET);
        System.out.print(BOLD + CYAN + "✨ Choose (1-4): " + RESET);
    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                handleAddTodo();
                break;
            case "2":
                handleDisplayTodos();
                break;
            case "3":
                handleToggleTodo();
                break;
            case "4":
                return true;
            default:
                System.out.println(RED + "❌ Invalid choice! Please enter 1-4." + RESET);
                pause();
        }
        return false;
    }

    public void handleAddTodo() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        
        System.out.println(BOLD + GREEN + topBorder + RESET);
        
        String title = "🚀 ADD NEW TODO 🚀";
        String titleLine = createCenteredLine("║", title, "║", width);
        System.out.println(BOLD + GREEN + "║" + titleLine.substring(1, titleLine.length() - 1) + "║" + RESET);
        
        System.out.println(BOLD + GREEN + bottomBorder + RESET);
        System.out.print(BOLD + YELLOW + "✨ Enter your new todo: " + RESET);
        String todoDescription = scanner.nextLine();
        TodoManager.getInstance().addTodo(todoDescription);
        System.out.println(BOLD + GREEN + "🎉 Todo added successfully! 🎉" + RESET);
        pause();
    }

    public void handleDisplayTodos() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        
        System.out.println(BOLD + YELLOW + topBorder + RESET);
        
        String title = "📋 TODO LIST 📋";
        String titleLine = createCenteredLine("║", title, "║", width);
        System.out.println(BOLD + YELLOW + "║" + titleLine.substring(1, titleLine.length() - 1) + "║" + RESET);
        
        System.out.println(BOLD + YELLOW + bottomBorder + RESET);
        System.out.println();
        
        if (TodoManager.getInstance().getTodoCount() == 0) {
            System.out.println(BOLD + CYAN + "🌟 No todos yet! Add some new ones! 🌟" + RESET);
        } else {
            displayFormattedTodos(width);
        }
        System.out.println();
        pause();
    }

    public void handleToggleTodo() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        
        System.out.println(BOLD + PURPLE + topBorder + RESET);
        
        String title = "🔄 TOGGLE TODO STATUS 🔄";
        String titleLine = createCenteredLine("║", title, "║", width);
        System.out.println(BOLD + PURPLE + "║" + titleLine.substring(1, titleLine.length() - 1) + "║" + RESET);
        
        System.out.println(BOLD + PURPLE + bottomBorder + RESET);
        
        if (TodoManager.getInstance().getTodoCount() == 0) {
            System.out.println(BOLD + RED + "❌ No todos available!" + RESET);
            pause();
            return;
        }
        
        System.out.println();
        String menuTitle = "📋 Select todo type to view:";
        String menuTitleLine = createCenteredLine("", menuTitle, "", width);
        System.out.println(BOLD + YELLOW + menuTitleLine + RESET);
        
        String subMenuBorder = createBorder("╔", "═", "╗", width);
        String subMenuBottomBorder = createBorder("╚", "═", "╝", width);
        
        System.out.println(BOLD + BLUE + subMenuBorder + RESET);
        
        String[] subMenuItems = {
            "❌ 1. Incomplete Todos",
            "✅ 2. Completed Todos"
        };
        
        String[] subColors = {RED, GREEN};
        
        for (int i = 0; i < subMenuItems.length; i++) {
            String subMenuLine = createLeftAlignedLine("║", subMenuItems[i], "║", width, 4);
            System.out.println(BOLD + BLUE + "║" + BOLD + subColors[i] + subMenuLine.substring(1, subMenuLine.length() - 1) + RESET + BLUE + "║" + RESET);
        }
        
        System.out.println(BOLD + BLUE + subMenuBottomBorder + RESET);
        System.out.print(BOLD + CYAN + "✨ Choose (1-2): " + RESET);
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                handleIncompleteToggle(width);
                break;
            case "2":
                handleCompletedToggle(width);
                break;
            default:
                System.out.println(BOLD + RED + "❌ Invalid choice. Please enter 1 or 2." + RESET);
                pause();
        }
    }
    
    private void handleIncompleteToggle(int width) {
        if (!TodoManager.getInstance().hasIncompleteTodos()) {
            System.out.println(BOLD + GREEN + "🎉 All todos are completed! Great job! 🎉" + RESET);
            pause();
            return;
        }
        
        System.out.println();
        String incompleteTitle = "📋 INCOMPLETE TODOS 📋";
        String incompleteTitleLine = createCenteredLine("", incompleteTitle, "", width);
        System.out.println(BOLD + YELLOW + incompleteTitleLine + RESET);
        System.out.println();
        
        displayFormattedIncompleteTodos(width);
        
        System.out.println();
        System.out.print(BOLD + YELLOW + "⚡ Enter todo number to complete: " + RESET);
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (!TodoManager.getInstance().isValidIndex(todoIndex)) {
            System.out.println(BOLD + RED + "❌ Please enter a valid number!" + RESET);
            pause();
            return;
        }
        
        Todo todo = TodoManager.getInstance().getTodoAt(todoIndex);
        if (todo != null && todo.isCompleted()) {
            System.out.println(BOLD + RED + "❌ This todo is already completed!" + RESET);
            pause();
            return;
        }

        TodoManager.getInstance().toggleTodo(todoIndex);
        System.out.println(BOLD + GREEN + "🎉 Todo marked as completed! 🎉" + RESET);
        pause();
    }
    
    private void handleCompletedToggle(int width) {
        if (!TodoManager.getInstance().hasCompletedTodos()) {
            System.out.println(BOLD + CYAN + "🔔 No completed todos yet! Start completing some! 🔔" + RESET);
            pause();
            return;
        }
        
        System.out.println();
        String completedTitle = "📋 COMPLETED TODOS 📋";
        String completedTitleLine = createCenteredLine("", completedTitle, "", width);
        System.out.println(BOLD + YELLOW + completedTitleLine + RESET);
        System.out.println();
        
        displayFormattedCompletedTodos(width);
        
        System.out.println();
        System.out.print(BOLD + YELLOW + "🔄 Enter todo number to mark as incomplete: " + RESET);
        int todoIndex = scanner.nextInt();
        scanner.nextLine();

        if (!TodoManager.getInstance().isValidIndex(todoIndex)) {
            System.out.println(BOLD + RED + "❌ Please enter a valid number!" + RESET);
            pause();
            return;
        }
        
        Todo todo = TodoManager.getInstance().getTodoAt(todoIndex);
        if (todo != null && !todo.isCompleted()) {
            System.out.println(BOLD + RED + "❌ This todo is already incomplete!" + RESET);
            pause();
            return;
        }

        TodoManager.getInstance().toggleTodo(todoIndex);
        System.out.println(BOLD + PURPLE + "🔄 Todo marked as incomplete! 🔄" + RESET);
        pause();
    }

    public void displayGoodbye() {
        clearScreen();
        int width = getTerminalWidth();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String emptyLine = createLine("║", " ", "║", width);
        
        System.out.println(BOLD + PURPLE + topBorder + RESET);
        System.out.println(BOLD + PURPLE + emptyLine + RESET);
        
        String stars = repeatString("🌟 ✨ ", (width - 6) / 8);
        System.out.println(BOLD + PURPLE + "║ " + BOLD + YELLOW + stars + RESET + PURPLE + " ║" + RESET);
        
        String goodbye = "🎉 Thank you for using Fancy Todo! 🎉";
        String goodbyeLine = createCenteredLine("║", goodbye, "║", width);
        System.out.println(BOLD + PURPLE + "║ " + BOLD + GREEN + goodbyeLine.substring(2, goodbyeLine.length() - 2) + RESET + PURPLE + " ║" + RESET);
        
        String message = "Have a productive day!";
        String messageLine = createCenteredLine("║", message, "║", width);
        System.out.println(BOLD + PURPLE + "║ " + BOLD + CYAN + messageLine.substring(2, messageLine.length() - 2) + RESET + PURPLE + " ║" + RESET);
        
        System.out.println(BOLD + PURPLE + "║ " + BOLD + YELLOW + stars + RESET + PURPLE + " ║" + RESET);
        System.out.println(BOLD + PURPLE + emptyLine + RESET);
        System.out.println(BOLD + PURPLE + bottomBorder + RESET);
        System.out.println();
    }

    private void displayFormattedTodos(int width) {
        TodoManager manager = TodoManager.getInstance();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String middleBorder = createBorder("╠", "═", "╣", width);
        
        System.out.println(BOLD + GREEN + topBorder + RESET);
        
        for (int i = 0; i < manager.getTodoCount(); i++) {
            String todoText = String.format("[%2d] %s", i, getTodoAt(i));
            String todoLine = createLeftAlignedLine("║", todoText, "║", width, 2);
            System.out.println(BOLD + GREEN + "║" + todoLine.substring(1, todoLine.length() - 1) + "║" + RESET);
            
            if (i < manager.getTodoCount() - 1) {
                System.out.println(BOLD + GREEN + middleBorder + RESET);
            }
        }
        
        System.out.println(BOLD + GREEN + bottomBorder + RESET);
    }
    
    private void displayFormattedIncompleteTodos(int width) {
        TodoManager manager = TodoManager.getInstance();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String middleBorder = createBorder("╠", "═", "╣", width);
        
        System.out.println(BOLD + GREEN + topBorder + RESET);
        
        boolean hasIncomplete = false;
        boolean firstItem = true;
        
        for (int i = 0; i < manager.getTodoCount(); i++) {
            Todo todo = manager.getTodoAt(i);
            if (todo != null && !todo.isCompleted()) {
                if (!firstItem) {
                    System.out.println(BOLD + GREEN + middleBorder + RESET);
                }
                String todoText = String.format("[%2d] %s", i, todo.toString());
                String todoLine = createLeftAlignedLine("║", todoText, "║", width, 2);
                System.out.println(BOLD + GREEN + "║" + todoLine.substring(1, todoLine.length() - 1) + "║" + RESET);
                hasIncomplete = true;
                firstItem = false;
            }
        }
        
        if (!hasIncomplete) {
            String noIncompleteText = "🎉 All todos are completed! Great job! 🎉";
            String noIncompleteLine = createCenteredLine("║", noIncompleteText, "║", width);
            System.out.println(BOLD + GREEN + "║" + noIncompleteLine.substring(1, noIncompleteLine.length() - 1) + "║" + RESET);
        }
        
        System.out.println(BOLD + GREEN + bottomBorder + RESET);
    }
    
    private void displayFormattedCompletedTodos(int width) {
        TodoManager manager = TodoManager.getInstance();
        String topBorder = createBorder("╔", "═", "╗", width);
        String bottomBorder = createBorder("╚", "═", "╝", width);
        String middleBorder = createBorder("╠", "═", "╣", width);
        
        System.out.println(BOLD + GREEN + topBorder + RESET);
        
        boolean hasCompleted = false;
        boolean firstItem = true;
        
        for (int i = 0; i < manager.getTodoCount(); i++) {
            Todo todo = manager.getTodoAt(i);
            if (todo != null && todo.isCompleted()) {
                if (!firstItem) {
                    System.out.println(BOLD + GREEN + middleBorder + RESET);
                }
                String todoText = String.format("[%2d] %s", i, todo.toString());
                String todoLine = createLeftAlignedLine("║", todoText, "║", width, 2);
                System.out.println(BOLD + GREEN + "║" + todoLine.substring(1, todoLine.length() - 1) + "║" + RESET);
                hasCompleted = true;
                firstItem = false;
            }
        }
        
        if (!hasCompleted) {
            String noCompletedText = "🔔 No completed todos yet! Start completing some! 🔔";
            String noCompletedLine = createCenteredLine("║", noCompletedText, "║", width);
            System.out.println(BOLD + GREEN + "║" + noCompletedLine.substring(1, noCompletedLine.length() - 1) + "║" + RESET);
        }
        
        System.out.println(BOLD + GREEN + bottomBorder + RESET);
    }
    
    private String getTodoAt(int index) {
        TodoManager manager = TodoManager.getInstance();
        if (index >= 0 && index < manager.getTodoCount()) {
            return manager.getTodoAt(index).toString();
        }
        return "";
    }

    private int getTerminalWidth() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = os.contains("win") ? "cmd /c echo %COLUMNS%" : "tput cols";
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            if (scanner.hasNextInt()) {
                int width = scanner.nextInt();
                scanner.close();
                return Math.max(width, 60);
            }
            scanner.close();
        } catch (Exception e) {
            // Fallback to default width
        }
        return DEFAULT_WIDTH;
    }

    private String createBorder(String left, String middle, String right, int width) {
        return left + repeatString(middle, width - 2) + right;
    }

    private String createLine(String left, String fill, String right, int width) {
        return left + repeatString(fill, width - 2) + right;
    }

    private String createCenteredLine(String left, String content, String right, int width) {
        int contentLength = getDisplayLength(content);
        int padding = (width - 2 - contentLength) / 2;
        int rightPadding = width - 2 - contentLength - padding;
        return left + repeatString(" ", padding) + content + repeatString(" ", rightPadding) + right;
    }

    private String createLeftAlignedLine(String left, String content, String right, int width, int leftPadding) {
        int contentLength = getDisplayLength(content);
        int rightPadding = width - 2 - contentLength - leftPadding;
        return left + repeatString(" ", leftPadding) + content + repeatString(" ", Math.max(0, rightPadding)) + right;
    }

    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    private int getDisplayLength(String text) {
        return text.replaceAll("[\uD83C-\uDBFF\uDC00-\uDFFF]", "XX").length();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pause() {
        System.out.println(BOLD + CYAN + "✨ Press Enter to continue... ✨" + RESET);
        scanner.nextLine();
    }
}