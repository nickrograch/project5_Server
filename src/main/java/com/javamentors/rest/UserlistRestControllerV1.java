package com.javamentors.rest;

import com.javamentors.entity.AppUser;
import com.javamentors.entity.Role;
import com.javamentors.repository.RoleRepository;
import com.javamentors.repository.UserRepository;
import com.javamentors.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/api/v1/admin/")
public class UserlistRestControllerV1 {


    private UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    private UserlistRestControllerV1(UserService userService, RoleRepository roleRepository, UserRepository userRepository){
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/userlist")
    public ResponseEntity<List<AppUser>> listAllUsers(Model model) {
        List<AppUser> appUsers = userService.getUsers();
        model.addAttribute(appUsers);
        if (appUsers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(appUsers, HttpStatus.OK);
        }
    }

    @PostMapping("/add/{role}")
    public ResponseEntity<?> createUser(@RequestBody AppUser appUser, @PathVariable("role")String role){
        Role userRole = roleRepository.findByName(role);
        appUser.getRoles().add(userRole);
        userService.addUser(appUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{role}")
    public ResponseEntity<?> updateUser(@RequestBody AppUser appUser, @PathVariable("role") String role) {

        AppUser updateUser = userRepository.getById(appUser.getId());
        updateUser.getRoles().clear();
        if (appUser.getPassword().isEmpty()){
            updateUser.setName(appUser.getName());
            updateUser.setEmail(appUser.getEmail());
        }
        else {
            updateUser.setName(appUser.getName());
            updateUser.setEmail(appUser.getEmail());
            updateUser.setPassword(appUser.getPassword());
        }

        Role userRole = roleRepository.findByName("USER");
        Role adminRole = roleRepository.findByName("ADMIN");

        if (role.contains("USER")) {
            updateUser.getRoles().add(userRole);
        } else {
            updateUser.getRoles().add(adminRole);
        }
        userService.editUser(updateUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id){
        AppUser getAppUser = userService.getUserById(Long.parseLong(id));
        userService.deleteUser(getAppUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
