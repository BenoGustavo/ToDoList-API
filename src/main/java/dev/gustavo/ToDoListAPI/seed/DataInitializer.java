package dev.gustavo.ToDoListAPI.seed;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCrypt;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.models.permission.RoleModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IRoleRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.utils.Enums.RoleName;

@Configuration
public class DataInitializer {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Value("${ADMIN_FIRST_NAME}")
    private String adminFirstName;

    @Value("${ADMIN_LAST_NAME}")
    private String adminLastName;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${ADMIN_AGE}")
    private int adminAge;

    @Bean
    @Order(1)
    public CommandLineRunner seedPermissions() {
        return args -> {
            if (roleRepository.findByName(RoleName.ROLE_ADMIN) == null) {
                RoleModel adminRole = new RoleModel();
                adminRole.setId(Long.valueOf(1));
                adminRole.setName(RoleName.ROLE_ADMIN);

                roleRepository.save(adminRole);
            }
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner seedAdminUser() {
        return args -> {
            if (userRepository.findByNickname("admin") == null) {
                createAdmin();
            }

        };
    }

    public void createAdmin() {
        RoleModel adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);
        Set<RoleModel> rolesSet = new HashSet<>();
        rolesSet.add(adminRole);

        UserModel adminUser = new UserModel();
        String hashedPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());

        UUID adminID = UUID.randomUUID();

        adminUser.setNickname("admin");
        adminUser.setId(adminID);
        adminUser.setFirstName(adminFirstName);
        adminUser.setLastName(adminLastName);
        adminUser.setAge(adminAge);
        adminUser.setRoles(rolesSet);
        adminUser.setEmail(adminEmail);
        adminUser.setHashedPassword(hashedPassword);

        System.out.println("\n\nAdmin user generated: \n" + adminUser + "\n\n");
        userRepository.save(adminUser);
    }
}