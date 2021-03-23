package com.cg.userms.repository;

import com.cg.userms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long>
{

}