package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> allUsers() {
        return userService.readAllUsers();
    }

    @GetMapping("/{id}")
    public User oneUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                stringBuilder.append(error.getField())
                             .append(error.getDefaultMessage())
                             .append("; ");
            }
            throw new UserNotCreatedException(stringBuilder.toString());
        }
        userService.createUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<HttpStatus> editUser(@RequestBody @Valid User user) {

        userService.update(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage(), new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("User Not Found", new Date(System.currentTimeMillis()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
