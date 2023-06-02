package org.example.sixthchapter;

import org.example.fifthchapter.User;

import java.util.List;

public interface UserDao {

    void deleteAll();
    void add(User user);
    void update(User user);
    User get(String id);
    List<User> getAll();

}
