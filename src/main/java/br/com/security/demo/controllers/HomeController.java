package br.com.security.demo.controllers;

import br.com.security.demo.models.DTO.UserDTO;
import br.com.security.demo.models.User;
import br.com.security.demo.service.MessageService;
import br.com.security.demo.service.UserService;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class HomeController {
    private UserService userService;
    private MessageService messageService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(UserService userService, PasswordEncoder passwordEncoder, MessageService messageService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    @GetMapping
    public List<UserDTO> findAll() {

        ArrayList<UserDTO> usersDTO = new ArrayList<>();

        userService.findAll().forEach(user -> {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .cpf(user.getCpf())
                    .build();
            usersDTO.add(userDTO);
        });

        return usersDTO;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<User> userOptional = this.userService.findById(id);

        if (userOptional.isPresent()) {
            UserDTO userDTO = UserDTO.builder()
                    .id(userOptional.get().getId())
                    .login(userOptional.get().getLogin())
                    .cpf(userOptional.get().getCpf())
                    .build();
            return ResponseEntity.status(302).body(userDTO);
        } else {
            return ResponseEntity.status(404).body(messageService.sendMessage("Usu??rio n??o encontrado"));
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user) {
        List<User> users = this.userService.findAll();
        boolean flag = false;

        if (users != null) {
            for(int i = 0; i < users.size(); i++) {
                if (users.get(i).getCpf().equals(user.getCpf())) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            return ResponseEntity.status(409).body(messageService.sendMessage("J?? existe um usu??rio cadastrado com esse cpf"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userService.save(user);
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .cpf(user.getCpf())
                    .build();
            return ResponseEntity.status(201).body(userDTO);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody User user) {
        Optional<User> userOptional = this.userService.findById(id);

        if (userOptional.isPresent()) {
            userOptional.get().setId(user.getId());
            this.userService.save(user);
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .cpf(user.getCpf())
                    .build();
            return ResponseEntity.status(200).body(userDTO);
        } else {
            return ResponseEntity.status(404).body(messageService.sendMessage("O usu??rio n??o existe"));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Optional<User> userOptional = this.userService.findById(id);

        if (userOptional.isPresent()) {
            this.userService.deleteById(id);
            return ResponseEntity.status(204).body(messageService.sendMessage("Us??rio deletado com sucesso"));
        } else {
            return ResponseEntity.status(404).body(messageService.sendMessage("O usu??rio n??o existe"));
        }
    }
}
