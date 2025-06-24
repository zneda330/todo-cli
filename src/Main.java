import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Todo> todos = new ArrayList<>(); // 할일 목록을 저장하는 리스트

        System.out.println("📝 할일 관리 프로그램에 오신 것을 환영합니다!");
        System.out.println("================================");

        while (true) {
            System.out.println("\n📋 메뉴를 선택하세요:");
            System.out.println("1. ➕ 할일 추가");
            System.out.println("2. 📃 할일 목록 보기");
            System.out.println("3. 🔄 할일 상태 변경");
            System.out.println("4. 🚪 프로그램 종료");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("📝 새로운 할일을 입력하세요: ");
                    String desc = scanner.nextLine();
                    if (!desc.trim().isEmpty()) {
                        todos.add(new Todo(desc));
                        System.out.println("✨ 할일이 추가되었습니다!\n");
                    } else {
                        System.out.println("❌ 할일 내용을 입력해주세요.\n");
                    }
                    break;
                case "2":
                    if (todos.isEmpty()) {
                        System.out.println("📭 등록된 할일이 없습니다.\n");
                    } else {
                        System.out.println("\n📋 현재 할일 목록:");
                        System.out.println("------------------");
                        for (int i = 0; i < todos.size(); i++) {
                            System.out.println((i + 1) + ". " + todos.get(i));
                        }
                        System.out.println("------------------");
                    }
                    break;
                case "3":
                    if (todos.isEmpty()) {
                        System.out.println("📭 변경할 할일이 없습니다.\n");
                        break;
                    }
                    
                    // 할일 목록 표시
                    System.out.println("\n📋 할일 목록:");
                    System.out.println("------------------");
                    for (int i = 0; i < todos.size(); i++) {
                        System.out.println((i + 1) + ". " + todos.get(i));
                    }
                    System.out.println("------------------");
                    
                    System.out.print("📝 변경할 할일 번호를 입력하세요: ");
                    int todoNum = readIndex(scanner);
                    
                    if (todoNum >= 1 && todoNum <= todos.size()) {
                        Todo selectedTodo = todos.get(todoNum - 1);
                        
                        System.out.println("\n🔄 어떻게 변경하시겠습니까?");
                        System.out.println("1. ✅ 완료로 변경");
                        System.out.println("2. ❌ 미완료로 변경");
                        System.out.print("선택: ");
                        
                        String actionChoice = scanner.nextLine();
                        
                        switch (actionChoice) {
                            case "1":
                                selectedTodo.setCompleted(true);
                                System.out.println("🎉 할일이 완료로 변경되었습니다!\n");
                                break;
                            case "2":
                                selectedTodo.setCompleted(false);
                                System.out.println("🔄 할일이 미완료로 변경되었습니다!\n");
                                break;
                            default:
                                System.out.println("❌ 잘못된 선택입니다.\n");
                        }
                    } else {
                        System.out.println("❌ 잘못된 번호입니다.\n");
                    }
                    break;
                case "4":
                    System.out.println("👋 프로그램을 종료합니다. 안녕히 가세요!");
                    return;
                default:
                    System.out.println("❌ 잘못된 선택입니다. 1-4 사이의 숫자를 입력해주세요.\n");
            }
        }
    }

    // 사용자가 입력한 문자열을 숫자로 변환하는 메서드
    private static int readIndex(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // 숫자가 아닌 경우 -1 반환
        }
    }
}
