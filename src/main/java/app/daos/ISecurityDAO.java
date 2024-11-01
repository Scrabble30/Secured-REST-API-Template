package app.daos;

import app.entities.User;

public interface ISecurityDAO {

    User createUser(String username, String password);

    User getVerifiedUser(String username, String password);
}
