package Service;

import Model.Message;

import java.util.ArrayList;
import java.util.List;

import DAO.MessageDAO;
import DAO.AccountDAO;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    // constructor for creating a new MessageService with a new MessageDAO
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    // Use the MessageDAO to create a new message
    public Message createMessage(Message message) throws Exception {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            throw new Exception("Message can not be empty");
        } else if (message.getMessage_text().length() > 255) {
            throw new Exception("Message can not be more than 255 characters long");
        }

        if (!accountDAO.findById(message.getPosted_by())) {
            throw new Exception("user does not exist");
        }

        Message createdMessage = messageDAO.createMessage(message);

        if (createdMessage == null) {
            throw new Exception("Message can not be empty");
        }

        return createdMessage;
    }

    // Use the MessageDAO to retrieve all Messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Use the MessageDAO to retrieve a message by ID
    public Message getMessageById(int message_id) throws Exception {
        Message message = messageDAO.getMessageById(message_id);
        
        if (message_id != message.getMessage_id()) {
            throw new Exception("Message does not exist at this id");
        }

        return messageDAO.getMessageById(message_id);
    }

    // Use the MessageDAO to retrieve all messages by UserID
    public List<Message> getAllMessagesByPosterId(int posted_by) {
        List<Message> messages = messageDAO.getAllMessagesByPosterId(posted_by);

        if (messages != null) {
            return messages;
        } else {
            return new ArrayList<>();
        }
    }

    // Use the MessageDAO to update a message by ID
    public Message updateMessage(int message_id, String content) throws Exception {
        if (content == null || content.trim().isEmpty()) {
            throw new Exception("Invalid message content");
        } else if (content.length() > 255) {
            throw new Exception("Message can not be more than 255 characters long");
        }

        Message message = messageDAO.updateMessage(message_id, content);
        
        if (message == null) {
            throw new Exception("Message not found");
        }

        return message;
    }

    // Use the MessageDAO to delete a message by ID
    public Message deleteMessageById(int message_id) {
         if (messageDAO.getMessageById(message_id) == null) {
             return null;
        }

        return messageDAO.deleteMessageById(message_id);
    }
}
