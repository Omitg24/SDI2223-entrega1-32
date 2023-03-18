package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Page<User> getUsers(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).get();
    }

    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    public Page<User> searchByNameAndLastName(Pageable pageable, String searchText, User user) {
        Page<User> users = new PageImpl<>(new LinkedList<User>());
        searchText = "%" + searchText + "%";
        users = usersRepository.searchByNameAndLastName(pageable, searchText);
        return users;
    }
}