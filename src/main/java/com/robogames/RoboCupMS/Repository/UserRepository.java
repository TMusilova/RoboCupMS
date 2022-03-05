package com.robogames.RoboCupMS.Repository;

import com.robogames.RoboCupMS.Entity.User;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repozitar pro uzivatele
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM user u where u.email = :email")
    User findByEmail(@Param("email") String email);

}
