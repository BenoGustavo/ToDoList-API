package dev.gustavo.ToDoListAPI.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {

        // Check for user existence
        var user = this.userRepository.findByEmail(userModel.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        var hashedPassword = BCrypt.hashpw(userModel.getHashedPassword(), BCrypt.gensalt());

        userModel.setHashedPassword(hashedPassword);

        var userCreated = this.userRepository.save(userModel);
        userCreated.setHashedPassword(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
