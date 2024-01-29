package org.example;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceWithoutTx implements UserService{
  private UserRepository userRepository;
  @Autowired
  public UserServiceWithoutTx(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  public void addUser(User user) {
    userRepository.save(user);
  }

  public User getUserById(String userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    return userOpt.orElseThrow(EntityNotFoundException::new);
  }

  public void transfer(String fromUserId, String toUserId, long money) {
    User fromUser = getUserById(fromUserId);
    User toUser = getUserById(toUserId);
    long fromBalance = fromUser.getBalance();

    if (fromBalance < money) {
      return;
    }
    fromUser.setBalance(fromUser.getBalance() - money);
    toUser.setBalance(toUser.getBalance() + money);
  }
}
