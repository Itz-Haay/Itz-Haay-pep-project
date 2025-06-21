package Service;

import DAO.SocialMediaDAO;

public class SocialMediaService {
    SocialMediaDAO socialDAO;

    public SocialMediaService() {
        this.socialDAO = new SocialMediaDAO();
    }
    
}
