// 代码生成时间: 2025-09-30 03:47:24
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.core.util.RouteOverviewPlugin;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HomeSchoolCommunicationServer {

    private static final int PORT = 7000; // Define the port number for the server
    private static final String MESSAGE_ENDPOINT = "/message";
    private static final String MESSAGES_ENDPOINT = "/messages";

    // A simple in-memory storage for messages
    private static final Map<String, String> messages = new HashMap<>();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        // Register routes
        app.routes(() -> {
            // Endpoint to send a message
            app.post(MESSAGE_ENDPOINT, HomeSchoolCommunicationServer::sendMessage);
            // Endpoint to retrieve all messages
            app.get(MESSAGES_ENDPOINT, HomeSchoolCommunicationServer::retrieveMessages);
        });

        // Enable route overview plugin for debugging
        app.registerPlugin(new RouteOverviewPlugin("/"));
    }

    // Send a message to the in-memory storage
    private static void sendMessage(Context ctx) {
        try {
            String senderId = ctx.header("SenderId");
            String recipientId = ctx.header("RecipientId");
            String content = ctx.bodyAsClass(String.class);

            if (senderId == null || recipientId == null || content == null) {
                ctx.status(400).result("Missing senderId, recipientId, or content");
                return;
            }

            // Store the message in the in-memory storage with a unique key
            String messageId = String.join("-", senderId, recipientId, String.valueOf(System.currentTimeMillis()));
            messages.put(messageId, content);

            // Respond with the message ID
            ctx.json(map("messageId", messageId));
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    // Retrieve all messages from the in-memory storage
    private static void retrieveMessages(Context ctx) {
        try {
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    // Helper method to create a map for JSON response
    private static Map<String, Object> map(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
