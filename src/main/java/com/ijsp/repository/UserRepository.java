package com.ijsp.repository;

import com.ijsp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ijsp
 * @since
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
