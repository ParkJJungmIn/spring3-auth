package com.board.api.repositories;


import com.board.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo  extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
