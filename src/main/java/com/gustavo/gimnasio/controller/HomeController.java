package com.gustavo.gimnasio.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.gimnasio.entity.User;
import com.gustavo.gimnasio.models.UserModel;
import com.gustavo.gimnasio.repositories.UserRepository;
import com.gustavo.gimnasio.services.UserService;

@RestController
// @RequestMapping("/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private AuthenticationManager authentication;

    @PostMapping("/register")
    public User register(@RequestBody UserModel userModel) {
        User newUser = new User();
        newUser.setEmail(userModel.getEmail());
        newUser.setPhotoURL(userModel.getPhotoURL());
        newUser.setPassword(bcryptEncoder.encode(userModel.getPassword()));
        return userService.saveUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody UserModel userModel) throws Exception {
        Authentication authObject;
        try {
            authObject = authentication.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
        } catch (Exception e) {
            throw new Exception("Credentials Invalid");
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @GetMapping("/authStateChange")
    public User getAuthState() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return user;
        } else {
            return user;
        }
    }

    @GetMapping("/get_users")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/search_users/{usuario}")
    public ArrayList<User> searchUsers(@PathVariable String usuario) {
        // Lista de personas segun el id del usuario que inicio sesion
        ArrayList<User> usersAll = (ArrayList<User>) userRepository.findAll();
        ArrayList<User> usersFilter = new ArrayList<>();
        ArrayList<User> usersWithoutUserCurrent = new ArrayList<>();

        try {
            // Obteniedo el usuario que inicio sesion
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = new User();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                user = userRepository.findByEmail(currentUserName)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            }

            for (User iterable_element : usersAll) {
                if ((iterable_element.getEmail() == user.getEmail()) == false) {
                    usersWithoutUserCurrent.add(iterable_element);
                }

            }

            for (User iterable_element : usersWithoutUserCurrent) {
                if (iterable_element.getEmail().contains(usuario)) {
                    usersFilter.add(iterable_element);
                }
            }
        } catch (Exception e) {
            System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR: " + e);
        }

        return usersFilter;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "This is dashboard contents";
    }

}
