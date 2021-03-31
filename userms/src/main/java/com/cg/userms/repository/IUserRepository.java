package com.cg.userms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.userms.entity.User;

public interface IUserRepository extends JpaRepository<User,Long>
{
    User findUserByUsername(String username);
}