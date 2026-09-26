package UserManagementSystem.config;

import UserManagementSystem.entity.Role;
import UserManagementSystem.entity.User;
import UserManagementSystem.repository.RoleRepository;
import UserManagementSystem.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role managerRole = new Role();
            managerRole.setName("MANAGER");

            Role userRole = new Role();
            userRole.setName("USER");

            roleRepository.save(adminRole);
            roleRepository.save(managerRole);
            roleRepository.save(userRole);

            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.getRoles().add(adminRole);

            userRepository.save(admin);

            User manager = new User();
            manager.setName("Manager User");
            manager.setEmail("manager@gmail.com");
            manager.setPassword(passwordEncoder.encode("Manager@123"));
            manager.getRoles().add(managerRole);

            userRepository.save(manager);

            User user = new User();
            user.setName("Normal User");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("User@123"));
            user.getRoles().add(userRole);

            userRepository.save(user);

            System.out.println("=================================");
            System.out.println("Sample data inserted successfully!");
            System.out.println("Admin   : admin@gmail.com / Admin@123");
            System.out.println("Manager : manager@gmail.com / Manager@123");
            System.out.println("User    : user@gmail.com / User@123");
            System.out.println("=================================");
        };
    }
}