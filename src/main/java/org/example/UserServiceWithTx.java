package org.example;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceWithTx implements UserService {
  private UserRepository userRepository;
  @Autowired
  public UserServiceWithTx(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void addUser(User user) {userRepository.save(user);}

  @Override
  public User getUserById(String userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    return userOpt.orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
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
