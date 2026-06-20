package com.rental.domain.repository;

import com.rentalapp.domain.entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
