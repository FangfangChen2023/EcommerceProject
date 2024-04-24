package com.example.ecommerceproject.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//  JpaRepository extends PagingAndSortingRepository and CrudRepository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
