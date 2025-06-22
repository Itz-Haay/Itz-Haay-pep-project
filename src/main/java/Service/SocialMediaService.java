package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialDAO;

    //Plain Constructor
    public SocialMediaService() {
        this.socialDAO = new SocialMediaDAO();
    }

    //Constructor given a DAO
    public SocialMediaService(SocialMediaDAO socialDAO){
        this.socialDAO = socialDAO;
    }

    //Register new User
    public Account addUser(Account user){
        if (user.getUsername() == null || user.getUsername().isBlank()) return null;
        if (user.getPassword() == null || user.getPassword().length() < 4) return null;
        if (socialDAO.getAccountByUsername(user.getUsername()) != null) return null;
        return socialDAO.insertAccount(user);
    }

    //Login
    public Account login(Account user){
       return socialDAO.userLogin(user.getUsername(), user.getPassword());
    }

    //Create New Message
    public Message createMessage(Message newMessage){
        if (newMessage.getMessage_text() == null || newMessage.getMessage_text().isBlank() || newMessage.getMessage_text().length() > 255) return null; 
        if (socialDAO.getAccountByID(newMessage.getPosted_by()) != null) {
            return socialDAO.createMessage(newMessage);
        }
        return null;
    }

    //Retrieve All Messages
    public List<Message> getMessages(){
        return socialDAO.getAllMessages();
    }

    //Message By ID
    public Message getMessage(int id){
        return socialDAO.getMessagebyID(id);
    }

    //Delete Message by ID
    public Message deleteMessage(int id){
        return socialDAO.deleteMessagebyID(id);
    }

    //Update by ID
    public Message updateMessage(Message updatedMessage){
        if (socialDAO.getMessagebyID(updatedMessage.getMessage_id()) != null) {
            if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().isBlank() ||updatedMessage.getMessage_text().length() > 255) {
                return null;
            } else {
                return socialDAO.updateMessagebyID(updatedMessage);
            }
        }
        return null;
    }

    //All messages by User
    public List<Message> getMessagesFromUser(int id){
        return socialDAO.getAllMessagesbyUserID(id);
    }

}
