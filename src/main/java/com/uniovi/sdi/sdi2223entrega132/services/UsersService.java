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


/**
 * Servicio que contiene capa de negocio de los usuarios
 *
 * @author Omar Teixeira González
 * @version 10/03/2023
 */
@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Método que devuelve todos los usuarios pertenecientes a una página de la base de datos
     * @param pageable pagina
     * @return paginación de usuarios
     */
    public Page<User> getUsers(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    /**
     * Método que devuelve un usuario concreto
     * @param id id del usuario
     * @return usuario
     */
    public User getUser(Long id) {
        return usersRepository.findById(id).get();
    }

    /**
     * Método que añade un usuario a la base de datos
     * @param user usuario a añadir
     */
    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    /**
     * Método que devuelve un usuario buscandolo por su email
     * @param email email del usuario
     * @return usuario
     */
    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    /**
     * Método que borra un usuario de la base de datos
     * @param id
     */
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    /**
     * Método que busca usuarios que tengan la cadena buscada
     * @param pageable pagina actual
     * @param searchText cadena buscada
     * @param user usuario
     * @return usuarios que tengan dicha cadena
     */
    public Page<User> searchByNameAndLastName(Pageable pageable, String searchText, User user) {
        Page<User> users = new PageImpl<>(new LinkedList<User>());
        searchText = "%" + searchText + "%";
        users = usersRepository.searchByNameAndLastName(pageable, searchText);
        return users;
    }
}