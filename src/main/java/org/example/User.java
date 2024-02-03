package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "user_balance")
public class User {
  @Id
  @Column(name = "user_id", nullable = false)
  private String user;

  @Column(name = "balance", nullable = false)
  private long balance;
  public User(){
    super();
  }
  public User(String userId, long balance) {
    this.user = userId;
    this.balance = balance;
  }

  public String getUser() {
    return user;
  }

  public long getBalance() {
    return balance;
  }
  public void setBalance(long balance) {
    this.balance = balance;
  }
}
