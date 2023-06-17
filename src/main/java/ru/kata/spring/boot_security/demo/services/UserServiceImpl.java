package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role(1L,"ROLE_ADMIN"));
            roleRepository.save(new Role(2L,"ROLE_USER"));
        }
    }

    @Override
    public boolean createUser(User user) {
        User userCheck = userRepository.findUserByUsername(user.getUsername());

        if (userCheck != null) {
            return false;
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void update(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.getById(id).setRoles(null);
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return user;
    }



    public Role findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.get();
    }




    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
