package Controller;

import java.util.List;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    private SocialMediaService service;
    public SocialMediaController() {
        this.service = new SocialMediaService();
    }
    


    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // POST /register - Register new user
        app.post("/register", this::handleRegister);

        // POST /login - User login
        app.post("/login", this::handleLogin);

        // POST /messages - Create new message
        app.post("/messages", this::handleCreateMessage);

        // GET /messages - Retrieve all messages
        app.get("/messages", this::handleGetAllMessages);

        // GET /messages/{message_id} - Retrieve message by ID
        app.get("/messages/{message_id}", this::handleGetMessageById);

        // DELETE /messages/{message_id} - Delete message by ID
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);

        // PATCH /messages/{message_id} - Update message text by ID
        app.patch("/messages/{message_id}", this::handleUpdateMessageById);

        // GET /accounts/{account_id}/messages - Retrieve all messages by user
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUser);
        app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void handleRegister(Context ctx) {
        try {
            Account user = ctx.bodyAsClass(Account.class);
            Account newUser = service.addUser(user);
            if (newUser == null) {
                ctx.status(400);
                ctx.result("");
            } else {
                ctx.json(newUser);
                ctx.status(200);
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result(e.toString());
        }
    }

    private void handleLogin(Context ctx){
        Account user = ctx.bodyAsClass(Account.class);
        Account authUser = service.login(user);
        if (authUser != null) {
            ctx.status(200);
            ctx.json(authUser);            
        } else {
            ctx.status(401);
            ctx.result("");
        }
    }

    private void handleCreateMessage(Context ctx){
        Message message = ctx.bodyAsClass(Message.class);
        Message newMessage = service.createMessage(message);
        if (newMessage != null) {
            ctx.status(200);
            ctx.json(newMessage);            
        } else {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void handleGetAllMessages(Context ctx){
        ctx.status(200);
        ctx.json(service.getMessages());
    }

    private void handleGetMessageById(Context ctx){
        Integer retrievedMessageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message retrievedMessage = service.getMessage(retrievedMessageID);
        if (retrievedMessage != null) {
            ctx.status(200);
            ctx.json(retrievedMessage);            
        } else {
            ctx.result("");
        }
    }

    private void handleDeleteMessageById(Context ctx){
        Integer messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = service.deleteMessage(messageID);
        if (deletedMessage == null) {
            ctx.status(200);
            ctx.result("");            
        } else {
            ctx.status(200);
            ctx.json(deletedMessage);
        }

    }

    private void handleUpdateMessageById(Context ctx){
        Message message = ctx.bodyAsClass(Message.class);
        Integer messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message replaceMessage = new Message();
        replaceMessage.setMessage_id(messageID);
        replaceMessage.setMessage_text(message.getMessage_text());
        Message updatedMessage = service.updateMessage(replaceMessage);
        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(updatedMessage);            
        } else {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void handleGetMessagesByUser(Context ctx){
        Integer userID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> retrievedMessages = service.getMessagesFromUser(userID);
        ctx.status(200);
        ctx.json(retrievedMessages);
        
    }

}