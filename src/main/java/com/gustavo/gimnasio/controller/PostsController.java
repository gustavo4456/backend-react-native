package com.gustavo.gimnasio.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gustavo.gimnasio.entity.Posts;
import com.gustavo.gimnasio.entity.User;
import com.gustavo.gimnasio.repositories.PostsRepository;
import com.gustavo.gimnasio.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/save")
    public Posts savePosts(@RequestBody Posts posts) {
        return this.postsRepository.save(posts);
    }

    @GetMapping("/by_id")
    public ArrayList<Posts> getPosts() {

        // Obteniedo el usuario que inicio sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        }

        // Lista de personas segun el id del usuario que inicio sesion
        ArrayList<Posts> postsAll = (ArrayList<Posts>) postsRepository.findAll();
        ArrayList<Posts> postFilter = new ArrayList<>();

        for (Posts iterable_element : postsAll) {
            if (iterable_element.getIdUser() == user.getId()) {
                postFilter.add(iterable_element);
            }

        }

        return postFilter;
    }

    @GetMapping("/all_posts")
    public ArrayList<Posts> getAllPosts() {
        return (ArrayList<Posts>) postsRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePosts(@PathVariable(name = "id") Long id) {
        postsRepository.deleteById(id);
    }

}
