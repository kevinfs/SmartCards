package com.iris.repository;

import com.iris.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(@Param("login") String login);
}

