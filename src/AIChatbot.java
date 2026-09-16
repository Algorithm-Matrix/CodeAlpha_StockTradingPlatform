import java.util.*;

public class AIChatbot {
    private static final Map<String, String> knowledgeBase = new HashMap<>();

    public static void main(String[] args) {
        trainChatbot();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     WELCOME TO CODEALPHA AI CHATBOT (TASK 3)     ");
        System.out.println("==================================================");
        System.out.println("Type 'exit' or 'bye' to end the conversation.\n");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("bye")) {
                System.out.println("Bot: Goodbye! Have a great day ahead.");
                break;
            }

            if (userInput.isEmpty()) {
                System.out.println("Bot: Please say something so I can help you!");
                continue;
            }

            String response = getBotResponse(userInput);
            System.out.println("Bot: " + response);
        }

        scanner.close();
    }

    // Rule-based training with FAQs and general responses
    private static void trainChatbot() {
        knowledgeBase.put("hello", "Hello! How can I assist you today?");
        knowledgeBase.put("hi", "Hi there! What can I help you with?");
        knowledgeBase.put("how are you", "I'm an AI chatbot running smoothly! How are you doing?");
        knowledgeBase.put("name", "I am the CodeAlpha Assistant Bot, designed for Task 3!");
        knowledgeBase.put("internship", "CodeAlpha offers great hands-on Java internship projects.");
        knowledgeBase.put("java", "Java is a popular class-based, object-oriented programming language.");
        knowledgeBase.put("help", "You can ask me about CodeAlpha, Java programming, or basic FAQs.");
        knowledgeBase.put("who created you", "I was created by Touseef Akbar for the CodeAlpha Internship.");
    }

    // Pattern matching logic
    private static String getBotResponse(String input) {
        String cleanInput = input.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (cleanInput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "I'm sorry, I don't quite understand that. Try asking about 'Java', 'CodeAlpha', or 'internship'.";
    }
}