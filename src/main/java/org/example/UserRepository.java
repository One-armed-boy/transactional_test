package org.example;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  @Transactional
  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  Optional<User> findForUpdateByUser(String id);
}
