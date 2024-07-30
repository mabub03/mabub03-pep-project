package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    // Make a constructor that makes a new AccountService and MessageService Objects
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // endpoint /messages with CRUD operations
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        // endpoint //register
        app.post("/register", this::postRegisterHandler);

        // endpoint /login
        app.post("/login", this::postLoginHandler);

        return app;
    }

    // Handler to retrieve all messages
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    // Handler to get message by message id
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        try {
            //ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message getMessage = messageService.getMessageById(message_id);
            ctx.status(200).json(getMessage);
            //System.out.println(getMessage);
        } catch (Exception e) {
            ctx.status(200).result("");
        }
    }

    // Handler to get all messages by an account id
    private void getAllMessagesForUserHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByPosterId(account_id);
        ctx.status(200).json(messages);
    }

    // Handler to post a new message
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        //Message message = ctx.bodyAsClass(Message.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(ctx.body(), Message.class);
        
        try {
            Message createdMessage = messageService.createMessage(message);
            ctx.status(200).json(createdMessage);
        } catch (Exception e) {
            ctx.status(400).result("");
        }
    }

    // Handler to update a message's text
    private void updateMessageTextHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        //String message = ctx.body();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(ctx.body(), Message.class);
        
        try {
            Message updatedMessage = messageService.updateMessage(message_id, message.getMessage_text());
            
            if (message != null) {
                ctx.status(200).json(updatedMessage);
            } 
        } catch (Exception e) {
            ctx.status(400).result("");
        }
    }

    // Handler to delete a message by a message id
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.deleteMessageById(message_id);
        
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200).result("");
        }

    }

    // Handler to register an account
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account addedAccount = accountService.createAccount(account);
            ctx.status(200).json(addedAccount);
        } catch (Exception e) {
            ctx.status(400).result("");
        }
    }

    // Handler to log into an account
    private void postLoginHandler(Context ctx) throws Exception {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());

            ctx.status(200).json(loggedInAccount);
        } catch (Exception e) {
            ctx.status(401).result("");
        }
    }
}