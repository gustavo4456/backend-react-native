package com.gustavo.gimnasio.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.gimnasio.entity.Friends;
import com.gustavo.gimnasio.entity.Posts;
import com.gustavo.gimnasio.entity.User;
import com.gustavo.gimnasio.models.PostUserModel;
import com.gustavo.gimnasio.repositories.FriendsRepository;
import com.gustavo.gimnasio.repositories.PostsRepository;
import com.gustavo.gimnasio.repositories.UserRepository;

// rechazada 
// enviada

@RestController
// @RequestMapping("/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class FriendsController {

    @Autowired
    FriendsRepository friendsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostsRepository postsRepository;

    // Enviar solicitud
    @PostMapping("/friend/save/sent")
    public Friends saveFriendSent(@RequestBody Friends friend) {
        friend.setEstado("enviada");
        return friendsRepository.save(friend);
    }

    // Confirmar la solicitud
    @PostMapping("/friend/save/confirm")
    public Friends confirmRequest(@RequestBody Friends friend) {
        ArrayList<Friends> getFriends = getRequestReceivedFriends();

        for (Friends friendsItem : getFriends) {
            if ((friendsItem.getIdAmigo() == friend.getIdAmigo()) && (friendsItem.getEstado().equals("enviada"))) {
                friend.setId(friendsItem.getId());
                friend.setEstado("confirmada");

            }
        }
        return friendsRepository.save(friend);
    }

    // Rechazar solicitud
    @PostMapping("/friend/save/rejectedRequest")
    public void saveFriendRejectedRequest(@RequestBody Friends friend) {
        ArrayList<Friends> getFriends = getRequestReceivedFriends();

        for (Friends friendsItem : getFriends) {
            if ((friendsItem.getIdAmigo() == friend.getIdAmigo()) && (friendsItem.getEstado().equals("enviada"))) {
                friend.setId(friendsItem.getId());
                friend.setEstado("rechazada");
                friendsRepository.deleteById(friend.getId());
            }
        }
        // return friendsRepository.save(friend);
    }

    // Cancelar solicitud
    @PostMapping("/friend/save/rejected")
    public void saveFriendRejected(@RequestBody Friends friend) {
        ArrayList<Friends> getFriends = getRequestSentFriends();

        for (Friends friendsItem : getFriends) {
            if ((friendsItem.getIdAmigo() == friend.getIdAmigo()) && (friendsItem.getEstado().equals("enviada"))) {
                friend.setId(friendsItem.getId());
                friend.setEstado("rechazada");
                friendsRepository.deleteById(friend.getId());
            }
        }
        // return friendsRepository.save(friend);
    }

    // Eliminar usuario
    @DeleteMapping("/friends/delete_friend/id/{id}")
    public void deleteFriend(@PathVariable(name = "id") Long id) {
        ArrayList<Friends> friendsAll = (ArrayList<Friends>) friendsRepository.findAll();
        ArrayList<Friends> friendsFilter = new ArrayList<>();

        for (Friends friend : friendsAll) {
            if (((friend.getIdAmigo() == id) && (friend.getEstado().equals("confirmada")))
                    || ((friend.getIdUsuario() == id) && (friend.getEstado().equals("confirmada")))) {
                friendsRepository.deleteById(friend.getId());

            }
        }

    }

    // Muestra las solicitudes de amistad que llegan al usuario que inicio sesion
    @GetMapping("/friends/requests_received")
    public ArrayList<Friends> getRequestReceivedFriends() {
        // Obteniedo el usuario que inicio sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        }

        // Listas
        ArrayList<Friends> friendsAll = (ArrayList<Friends>) friendsRepository.findAll();
        ArrayList<Friends> friendsFilter = new ArrayList<>();
        // ArrayList<Friends> friendsResult = new ArrayList<>();

        for (Friends friend : friendsAll) {
            if ((friend.getIdAmigo() == user.getId()) && (friend.getEstado().equals("enviada"))) {
                friendsFilter.add(friend);
            }
        }

        return friendsFilter;
    }

    // Muestra las solicitudes enviadas por el usuario que inicio sesion
    @GetMapping("/friends/requests_sent")
    public ArrayList<Friends> getRequestSentFriends() {
        // Obteniedo el usuario que inicio sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        }

        // Listas
        ArrayList<Friends> friendsAll = (ArrayList<Friends>) friendsRepository.findAll();
        ArrayList<Friends> friendsFilter = new ArrayList<>();
        // ArrayList<Friends> friendsResult = new ArrayList<>();

        for (Friends friend : friendsAll) {
            if ((friend.getIdUsuario() == user.getId()) && (friend.getEstado().equals("enviada"))) {
                friendsFilter.add(friend);
            }
        }

        return friendsFilter;
    }

    // Muestra las solicitudes confirmadas por el usuario que inicio sesion
    @GetMapping("/friends/requests_sent_confirmed")
    public ArrayList<Friends> getRequestSentFriendsConfirmed() {
        // Obteniedo el usuario que inicio sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        }

        // Listas
        ArrayList<Friends> friendsAll = (ArrayList<Friends>) friendsRepository.findAll();
        ArrayList<Friends> friendsFilter = new ArrayList<>();
        // ArrayList<Friends> friendsResult = new ArrayList<>();

        for (Friends friend : friendsAll) {
            if ((friend.getIdUsuario() == user.getId()) && (friend.getEstado().equals("confirmada"))
                    || (friend.getIdAmigo() == user.getId()) && (friend.getEstado().equals("confirmada"))) {
                friendsFilter.add(friend);
            }
        }

        return friendsFilter;
    }

    // Devuelve true si el id del usuario que se ingreso tiene una solicitud, se usa
    // desde el usuario que envia la solicitud
    @GetMapping("/friends/request_send/id/{id}")
    public String requestExists(@PathVariable(name = "id") Long id) {
        ArrayList<Friends> friendsList = getRequestSentFriends();
        ArrayList<Friends> friendListConfirmed = getRequestSentFriendsConfirmed();
        String flag = "Agregar";

        for (Friends friend : friendsList) {
            if (friend.getIdAmigo() == id) {
                flag = "Cancelar solicitud";
            }
        }

        for (Friends friend : friendListConfirmed) {
            if (friend.getIdAmigo() == id || friend.getIdUsuario() == id) {
                flag = "Eliminar";
            }
        }

        return flag;
    }

    // Devuelve todos los usuarios que me enviaron una solicitud de amistad

    @GetMapping("/friends/requests_received/users_all")
    public ArrayList<User> requestReceivedUsers() {
        ArrayList<Friends> requesReceivedList = getRequestReceivedFriends();

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
            // Para no mostrar el usuario que inicio sesion, agrega a esta lista todos los
            // usuarios menos el que esta con sesion iniciada
            for (User iterable_element : usersAll) {
                if ((iterable_element.getEmail() == user.getEmail()) == false) {
                    usersWithoutUserCurrent.add(iterable_element);
                }

            }

            for (Friends friend : requesReceivedList) {
                for (User userItem : usersWithoutUserCurrent) {
                    if (friend.getIdUsuario() == userItem.getId()) {
                        usersFilter.add(userItem);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR: " + e);
        }

        return usersFilter;
    }

    // Muestra los id de las solicitudes de amistad CONFIRMADAS que LE llegan al
    // usuario que
    // inicio sesion tanto enviadas como recibidas
    @GetMapping("/friends/requests_confimed")
    public ArrayList<Long> getRequestConfirmedFriends() {
        // Obteniedo el usuario que inicio sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        }

        // Listas
        ArrayList<Friends> friendsAll = (ArrayList<Friends>) friendsRepository.findAll();
        ArrayList<Friends> friendsFilter = new ArrayList<>();
        // para devolver solo los id de usarios que son amigos tanto si enviaron o
        // recibieron la solicitud
        ArrayList<Long> friendsResult = new ArrayList<>();

        for (Friends friend : friendsAll) {

            if (((friend.getIdAmigo() == user.getId()) || (friend.getIdUsuario() == user.getId()))
                    && (friend.getEstado().equals("confirmada"))) {
                friendsFilter.add(friend);
            }
        }

        for (Friends friend : friendsFilter) {
            if (friend.getIdAmigo() == user.getId()) {
                friendsResult.add(friend.getIdUsuario());
            }
            if (friend.getIdUsuario() == user.getId()) {
                friendsResult.add(friend.getIdAmigo());
            }
        }

        return friendsResult;
    }

    // Obtener lista de amigos (usuarios y posts)
    @GetMapping("/friends/getfriendsposts")
    public ArrayList<PostUserModel> getFriendsPosts() {
        // Listas
        ArrayList<Posts> postsAll = (ArrayList<Posts>) postsRepository.findAll();
        ArrayList<Long> friendsConfirmed = getRequestConfirmedFriends();
        ArrayList<PostUserModel> postsFilter = new ArrayList<>();

        for (Long friendId : friendsConfirmed) {
            for (Posts post : postsAll) {
                if (post.getIdUser() == friendId) {
                    PostUserModel postUserModel = new PostUserModel();
                    postUserModel.setId(post.getId());
                    postUserModel.setTitle(post.getTitle());
                    postUserModel.setContent(post.getContent());
                    postUserModel.setImage(post.getImage());
                    postUserModel.setIdUser(post.getIdUser());

                    postUserModel.setEmail(userRepository.findById(friendId).get().getEmail());
                    postUserModel.setPhotoURL(userRepository.findById(friendId).get().getPhotoURL());
                    postsFilter.add(postUserModel);

                }
            }
        }

        return postsFilter;
    }

    // // Obtener lista de amigos
    // @GetMapping("/friends/getfriendsposts")
    // public ArrayList<Posts> getFriendsPosts() {
    // // Listas
    // ArrayList<Posts> postsAll = (ArrayList<Posts>) postsRepository.findAll();
    // ArrayList<Long> friendsConfirmed = getRequestConfirmedFriends();
    // ArrayList<Posts> postsFilter = new ArrayList<>();

    // for (Long friendId : friendsConfirmed) {
    // for (Posts post : postsAll) {
    // if (post.getIdUser() == friendId) {
    // postsFilter.add(post);
    // }
    // }
    // }

    // return postsFilter;
    // }
}
