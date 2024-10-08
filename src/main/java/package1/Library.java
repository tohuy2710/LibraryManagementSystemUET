package package1;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Document> documentList;
    private List<Admin> adminList;
    private List<User> userList;

    public Library() {
        documentList = new ArrayList<Document>();
        adminList = new ArrayList<Admin>();
        userList = new ArrayList<User>();
    }


    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> _documentList) {
        this.documentList = _documentList;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> _adminList) {
        this.adminList = _adminList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> _userList) {
        this.userList = _userList;
    }

    public Document findDocumentsById(String id) {
        try {
            for (Document document : documentList) {
                if (document.getId().equals(id)) {
                    return document;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document findDocumentsByName(String name) {
        try {
            for (Document document : documentList) {
                if (document.getName().equals(name)) {
                    return document;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Document> findDocumentsByAuthor(String author) {
        try {
            List<Document> result = new ArrayList<Document>();
            for (Document document : documentList) {
                if (document.getAuthor().equals(author)) {
                    result.add(document);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User newUser) {
        userList.add(newUser);
    }
}
