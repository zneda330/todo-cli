import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Todo> todos = new ArrayList<>();

        while (true) {
            System.out.println("1. Add todo");
            System.out.println("2. List todos");
            System.out.println("3. Complete todo");
            System.out.println("4. Cancel todo");
            System.out.println("5. Exit");
            System.out.print("Select: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter todo description: ");
                    String desc = scanner.nextLine();
                    todos.add(new Todo(desc));
                    System.out.println("Todo added.\n");
                    break;
                case "2":
                    if (todos.isEmpty()) {
                        System.out.println("No todos.\n");
                    } else {
                        System.out.println("Todo list:");
                        for (int i = 0; i < todos.size(); i++) {
                            System.out.println((i + 1) + ". " + todos.get(i));
                        }
                        System.out.println();
                    }
                    break;
                case "3":
                    System.out.print("Enter todo number to mark complete: ");
                    int num1 = readIndex(scanner);
                    if (num1 >= 1 && num1 <= todos.size()) {
                        todos.get(num1 - 1).setCompleted(true);
                        System.out.println("Todo completed.\n");
                    } else {
                        System.out.println("Invalid number.\n");
                    }
                    break;
                case "4":
                    System.out.print("Enter todo number to cancel: ");
                    int num2 = readIndex(scanner);
                    if (num2 >= 1 && num2 <= todos.size()) {
                        todos.get(num2 - 1).setCompleted(false);
                        System.out.println("Todo canceled.\n");
                    } else {
                        System.out.println("Invalid number.\n");
                    }
                    break;
                case "5":
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }

    private static int readIndex(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
