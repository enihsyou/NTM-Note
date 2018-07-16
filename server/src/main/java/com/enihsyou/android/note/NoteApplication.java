package com.enihsyou.android.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
@CrossOrigin
public class NoteApplication {

    public static final ResponseEntity<Message> PASSWORD_MISS_MATCH =
        ResponseEntity.badRequest().body(Message.text("密码不正确"));

    public static final ResponseEntity<Message> USER_NOT_FOUND =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.text("用户不存在"));

    public static final ResponseEntity<Message> NOTE_NOT_FOUND =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.text("笔记不存在"));

    public static final ResponseEntity<Message> USER_EXIST = ResponseEntity.badRequest().body(Message.text("用户名已存在"));

    private final UserRepository repository;

    @Autowired
    public NoteApplication(UserRepository repository) {this.repository = repository;}

    @PostMapping("/users/register")
    ResponseEntity<Message> register(@RequestParam String username, @RequestParam String password) {
        if (repository.findByUsername(username) != null) {
            return USER_EXIST;
        } else {
            final User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            repository.save(user);
            return ResponseEntity.ok(Message.body(user));
        }
    }

    @PostMapping("/users/login")
    ResponseEntity<Message> login(@RequestParam String username, @RequestParam String password) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            return ResponseEntity.ok(Message.body(user));
        }
        return USER_NOT_FOUND;
    }

    private static boolean passwordMissMatched(User user, String inputPassword) {
        return !user.getPassword().equals(inputPassword);
    }

    @PostMapping("notes")
    ResponseEntity<Message> upload(@RequestParam String username, @RequestParam String password, @RequestBody Note note) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            note.setUser(user);
            user.getNotes().add(note);
            repository.save(user);
            return ResponseEntity.ok(Message.body(user));
        }
        return USER_NOT_FOUND;
    }

    @PatchMapping("notes")
    ResponseEntity<Message> modify(@RequestParam int noteId,
                          @RequestParam String username,
                          @RequestParam String password,
                          @RequestBody Note note) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            Note target = user.getNotes().stream().filter(n -> n.getId() == noteId).findFirst().orElse(null);
            if (target == null) return NOTE_NOT_FOUND;

            target.setAlarmTime(note.getAlarmTime());
            target.setLabel(note.getLabel());
            target.setContent(note.getContent());
            target.setStatus(note.getStatus());
            target.setLastModifiedTime(note.getLastModifiedTime());

            repository.save(user);
            return ResponseEntity.ok(Message.body(target));
        }
        return USER_NOT_FOUND;
    }

    @PatchMapping("notes/archive")
    ResponseEntity<Message> archive(@RequestParam int noteId,
                           @RequestParam String username,
                           @RequestParam String password) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            Note target = user.getNotes().stream().filter(n -> n.getId() == noteId).findFirst().orElse(null);
            if (target == null) return NOTE_NOT_FOUND;

            target.setStatus("ARCHIVED");

            repository.save(user);
            return ResponseEntity.ok(Message.body(target));
        }
        return USER_NOT_FOUND;
    }

    @PatchMapping("notes/trash")
    ResponseEntity<Message> trash(@RequestParam int noteId,
                           @RequestParam String username,
                           @RequestParam String password) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            Note target = user.getNotes().stream().filter(n -> n.getId() == noteId).findFirst().orElse(null);
            if (target == null) return NOTE_NOT_FOUND;

            target.setStatus("TRASH");

            repository.save(user);
            return ResponseEntity.ok(Message.body(target));
        }
        return USER_NOT_FOUND;
    }

    @GetMapping("notes")
    ResponseEntity<Message> download(@RequestParam String username, @RequestParam String password) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            if (passwordMissMatched(user, password)) return PASSWORD_MISS_MATCH;
            return ResponseEntity.ok(Message.body(user));
        }
        return USER_NOT_FOUND;
    }

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }
}
