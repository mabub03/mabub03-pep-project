package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.util.List;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDAO {
    // Create A Message
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        Message createdMessage = null;

        try {
            // create the sql
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

            // create preparedStatement and write preparedStatement's setString and setInt methods here
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();
            
            // if there are generated keys then set the message id to the generated keys
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setMessage_id(generatedKeys.getInt(1));
            }
            
            // could return the message from the parameter of the method but that seems like a bad practice thing
            // instead if there are affected rows then create a select statement that gets the record from the message_id that was just created via generated keys
            // and make a new Message object from that which will get returned by the function which will show what is really in the database
            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM message WHERE message_id = ?;";
                
                try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                    selectStatement.setInt(1, message.getMessage_id());
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        createdMessage = new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                        );
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return createdMessage;
    }

    // Retrieve all Messages
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            // Write SQL logic
            String sql = "SELECT * FROM message;";
            
            // Write prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // Write ResultSet and iterate through it to add to the messages List<Message>
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );

                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    // Retrieve Message by ID
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            // Write SQL logic
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            
            // Write prepared statement and their setString, setInt, or setLong method here
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            // Write ResultSet and interate through it to build the Message object and return it
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );

                return message;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    // Retrieve All Messages by UserID
    public List<Message> getAllMessagesByPosterId(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            // Write SQL logic
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            
            // Write prepared statement and their setString, setInt, or setLong method here
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);

            // Write ResultSet and interate through it to build the Message object and return it
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );

                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return messages;
    }

    // Update A Message by ID
    public Message updateMessage(int message_id, String content) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        
        try {
            // Write SQL logic
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            

            // Write prepared statement and their setString, setInt, or setLong method here
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, content);
            preparedStatement.setInt(2, message_id);
            
            int affectedRows = preparedStatement.executeUpdate();
            
            // if there are affected rows form a sql statement that would show the newly updated statement's results to the front end
            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM message WHERE message_id = ?";
                
                try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                    selectStatement.setInt(1, message_id);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        message = new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                        );
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return message;
    }

    // Delete A Message by ID
    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message oldMessage = this.getMessageById(message_id);
        
        try {
            // Write SQL logic
            String sql = "DELETE FROM message WHERE message_id = ?";
            
            // Write prepared statement and their setString, setInt, or setLong method here
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            // execute the sql delete statement with preparedStatement's executeUpdate method
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return oldMessage;
    }
}
