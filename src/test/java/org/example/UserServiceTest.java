package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class UserServiceTest {
  @Autowired private UserServiceWithoutTx userServiceWithoutTx;
  @Autowired private UserServiceWithTx userServiceWithTx;
  @Autowired private UserRepository userRepository;

  @AfterEach
  private void cleanDB() {
    userRepository.deleteAll();
  }

//  @Test
  @DisplayName(
          """
          멀티 스레드 환경에서 UserServiceWithoutTx를 통해 송금 작업이 DB의 일관성을 지키며 제대로 이루어지는지 테스트
          """
  )
  void userServiceWithoutTxTransferInMultiThread() throws InterruptedException{
    Assertions.assertThat(userServiceWithoutTx).isInstanceOf(UserServiceWithoutTx.class);
    execTestLogic(userServiceWithoutTx);
  }

  @Test
  @DisplayName(
          """
          멀티 스레드 환경에서 UserServiceWithTx를 통해 송금 작업이 DB의 일관성을 지키며 제대로 이루어지는지 테스트
          """
  )
  void userServiceWithTxTransferInMultiThread() throws InterruptedException{
    Assertions.assertThat(userServiceWithTx).isInstanceOf(UserServiceWithTx.class);
    execTestLogic(userServiceWithTx);
  }

  private void execTestLogic(UserService userService) throws InterruptedException {
    // given
    User user1 = new User("Jamal", 100);
    User user2 = new User("Nikola", 30);
    userService.addUser(user1);
    userService.addUser(user2);

    var numOfThread = 3;
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(numOfThread);

    // when
    for (int i = 0; i < numOfThread; i++) {
      new Thread(()->{
        try {
          startLatch.await();

          userService.transfer(user1.getUser(), user2.getUser(), 20);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        } finally {
          endLatch.countDown();
        }
      }).start();
    }
    startLatch.countDown();
    endLatch.await();

    // then
    User user1After = userService.getUserById(user1.getUser());
    User user2After = userService.getUserById(user2.getUser());
    Assertions.assertThat(user1After.getBalance()).isEqualTo(0);
    Assertions.assertThat(user2After.getBalance()).isEqualTo(user1.getBalance() + user2.getBalance());
  }
}
