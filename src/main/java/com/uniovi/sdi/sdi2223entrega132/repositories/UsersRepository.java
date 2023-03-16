package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT r FROM User r WHERE (LOWER(r.name) LIKE LOWER(?1) OR LOWER(r.lastName) LIKE LOWER(?1))")
    Page<User> searchByNameAndLastName(Pageable pageable, String searchText);

    @Modifying
    @Transactional
    @Query("UPDATE User SET amount = ?1 WHERE id = ?2")
    void updateAmount(Double amount, Long id);
}
