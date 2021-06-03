package br.com.security.demo.service;

import br.com.security.demo.models.User;
import br.com.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return this.userRepository.findById(id);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public void deleteById(String id) {
        this.userRepository.deleteById(id);
    }
}
