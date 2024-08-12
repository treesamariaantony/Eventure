////package fr.epita.eventure.controller;
////
////import fr.epita.eventure.datamodel.User;
////import fr.epita.eventure.service.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////@RestController
////@RequestMapping("/api/users")
////public class UserController {
////
////    private final UserService userService;
////
////    @Autowired
////    public UserController(UserService userService) {
////        this.userService = userService;
////    }
////
////    @PostMapping("/register")
////    public ResponseEntity<User> registerUser(@RequestBody User user) {
////        User createdUser = userService.createUser(user);
////        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
////    }
////}
//package fr.epita.eventure.controller;
//
//import fr.epita.eventure.datamodel.UserRole;
//import fr.epita.eventure.dto.UserDTO;
//import fr.epita.eventure.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
////
////@RestController
////@RequestMapping("/api/users")
////public class UserController {
////
////    @Autowired
////    private UserService userService;
////
////    @PostMapping("/register")
////    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
////        System.out.println("Inside");
////        UserDTO createdUser = userService.registerUser(userDTO);
////        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
////    }
////
////    @PutMapping("/{id}/role")
////    public ResponseEntity<UserDTO> updateUserRole(@PathVariable String id, @RequestBody UserRole newRole) {
////        UserDTO updatedUser = userService.updateUserRole(id, newRole);
////        if (updatedUser != null) {
////            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
////        } else {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
////    }
////}
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
//        logger.debug("Register user request: {}", userDTO);
//        UserDTO createdUser = userService.registerUser(userDTO);
//        if (createdUser != null) {
//            logger.info("User registered successfully: {}", createdUser.getUsername());
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        } else {
//            logger.error("Failed to register user: {}", userDTO.getUsername());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping("/{id}/role")
//    public ResponseEntity<UserDTO> updateUserRole(@PathVariable String id, @RequestBody UserRole newRole) {
//        logger.debug("Update user role request for ID: {} to {}", id, newRole);
//        UserDTO updatedUser = userService.updateUserRole(id, newRole);
//        if (updatedUser != null) {
//            logger.info("User role updated successfully for ID: {}", id);
//            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//        } else {
//            logger.error("Failed to update user role for ID: {}", id);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}
//package fr.epita.eventure.controller;
//
//import fr.epita.eventure.datamodel.UserRole;
//import fr.epita.eventure.dto.UserDTO;
//import fr.epita.eventure.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
//        logger.debug("Register user request: {}", userDTO);
//        UserDTO createdUser = userService.registerUser(userDTO);
//        if (createdUser != null) {
//            logger.info("User registered successfully: {}", createdUser.getUsername());
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        } else {
//            logger.error("Failed to register user: {}", userDTO.getUsername());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/{id}/role")
//    public ResponseEntity<UserDTO> updateUserRole(@PathVariable String id, @RequestBody UserRole newRole) {
//        logger.debug("Update user role request for ID: {} to {}", id, newRole);
//        UserDTO updatedUser = userService.updateUserRole(id, newRole);
//        if (updatedUser != null) {
//            logger.info("User role updated successfully for ID: {}", id);
//            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//        } else {
//            logger.error("Failed to update user role for ID: {}", id);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}

package fr.epita.eventure.controller;

import fr.epita.eventure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username,
                                               @RequestParam String password,
                                               @RequestParam String email,
                                               @RequestParam String name) {
        try {
            userService.registerUser(username, password, email, name);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to register user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestParam String username,
                                                   @RequestParam String password) {
        try {
            String token = userService.authenticateUser(username, password);
            if (token != null) {
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to authenticate user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
