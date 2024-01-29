package org.example;

public interface UserService {
  void addUser(User user);
  User getUserById(String userId);
  void transfer(String fromUserId, String toUserId, long money);
}
