package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceWithTx extends UserServiceWithoutTx {
  @Autowired
  public UserServiceWithTx(UserRepository userRepository) {
    super(userRepository);
  }

  @Transactional
  public void addUser(User user) {super.addUser(user);}

  @Override
  public User getUserById(String userId) {
    return super.getUserById(userId);
  }

  @Override
  @Transactional
  public void transfer(String fromUserId, String toUserId, long money) {
    super.transfer(fromUserId, toUserId, money);
  }
}
