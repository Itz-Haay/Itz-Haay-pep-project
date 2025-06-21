package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {
    // Creating an Account
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prpStmt.setString(1, account.getUsername());
            prpStmt.setString(2, account.getPassword());
            prpStmt.executeUpdate();

            ResultSet accountIDResult = prpStmt.getGeneratedKeys();
            if (accountIDResult.next()) {
                int generated_id = (int) accountIDResult.getLong(1);
                return new Account(generated_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    };

    // Get account details for Login

    public Account userLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = (?) AND password = (?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql);
            prpStmt.setString(1, username);
            prpStmt.setString(2, password);

            ResultSet accountInfoResult = prpStmt.executeQuery();
            if (accountInfoResult.next()) {
                Account userAccount = new Account(accountInfoResult.getInt("account_id"),
                        accountInfoResult.getString("username"),
                        accountInfoResult.getString("password"));
                return userAccount;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    // Create New Message
    public Message createMessage(Message messagePost) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?, ?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prpStmt.setInt(2, messagePost.getPosted_by());
            prpStmt.setString(3, messagePost.getMessage_text());
            prpStmt.setLong(4, messagePost.getTime_posted_epoch());
            prpStmt.executeUpdate();

            ResultSet postResult = prpStmt.getGeneratedKeys();
            if (postResult.next()) {
                int generatedMessageID = (int) postResult.getLong(1);
                return new Message(generatedMessageID, messagePost.getPosted_by(), messagePost.getMessage_text(),
                        messagePost.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    // Retrieve All Messages
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement prpStmt = connection.prepareStatement(sql);
            ResultSet allMessages = prpStmt.executeQuery();
            while (allMessages.next()) {
                Message message = new Message(allMessages.getInt("message_id"),
                        allMessages.getInt("posted_by"),
                        allMessages.getString("message_text"),
                        allMessages.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return messages;
    }

    // Retrieve All Messages posted by Specific User using UserID
    public List<Message> getAllMessagesbyUserID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messagesByUser = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = (?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql);
            prpStmt.setInt(1, id);
            ResultSet userMessages = prpStmt.executeQuery();
            while (userMessages.next()) {
                Message message = new Message(userMessages.getInt("message_id"),
                        userMessages.getInt("posted_by"),
                        userMessages.getString("message_text"),
                        userMessages.getLong("time_posted_epoch"));
                messagesByUser.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return messagesByUser;
    }

    // Retrieve Message by ID
    public Message getMessagebyID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = (?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql);
            prpStmt.setInt(1, id);
            ResultSet messagesResult = prpStmt.executeQuery();
            if (messagesResult.next()) {
                Message message = new Message(messagesResult.getInt("message_id"),
                        messagesResult.getInt("posted_by"),
                        messagesResult.getString("message_text"),
                        messagesResult.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    // Update a message by ID
    public Message updateMessagebyID(Message id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = (?) WHERE message_id = (?)";
            PreparedStatement prpStmt = connection.prepareStatement(sql);
            prpStmt.setString(1, id.getMessage_text());
            prpStmt.setInt(2, id.getMessage_id());
            int updatedrows = prpStmt.executeUpdate();
            if (updatedrows > 0) {
                String sql2 = "SELECT * FROM Message WHERE message_id = (?)";
                PreparedStatement prpStmt2 = connection.prepareStatement(sql2);
                prpStmt2.setInt(1, id.getMessage_id());
                ResultSet updatedPost = prpStmt2.executeQuery();
                if (updatedPost.next()) {
                    Message message = new Message(updatedPost.getInt("message_id"),
                        updatedPost.getInt("posted_by"),
                        updatedPost.getString("message_text"),
                        updatedPost.getLong("time_posted_epoch"));
                    return message;
                }                
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

};
