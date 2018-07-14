package com.enihsyou.android.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
@Controller
public class NoteApplication {

    private final UserRepository repository;

    @Autowired
    public NoteApplication(UserRepository repository) {this.repository = repository;}

    @PostMapping("/users/register")
    ResponseEntity register(@RequestParam String username, @RequestParam String password) {
        if (repository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            final User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setNote(new ArrayList<>());
            repository.save(user);
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/users/login")
    ResponseEntity login(@RequestParam String username, @RequestParam String password) {
        final Optional<User> optional = repository.findByUsername(username);
        if (optional.isPresent()) {
            final User user = optional.get();
            if (!user.getPassword().equals(password)) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("notes")
    ResponseEntity upload(@RequestParam String username, @RequestParam String password, @RequestBody Note note) {
        final Optional<User> optional = repository.findByUsername(username);
        if (optional.isPresent()) {
            final User user = optional.get();
            if (!user.getPassword().equals(password)) return ResponseEntity.badRequest().build();
            note.setUser(user);
            user.getNote().add(note);
            repository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("notes")
    ResponseEntity download(@RequestParam String username, @RequestParam String password) {
        final Optional<User> optional = repository.findByUsername(username);
        if (optional.isPresent()) {
            final User user = optional.get();
            if (!user.getPassword().equals(password)) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(user.getNote());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }
}
