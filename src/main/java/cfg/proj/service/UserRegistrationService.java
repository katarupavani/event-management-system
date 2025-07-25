package cfg.proj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.User;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.IdNotFoundException;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.UserRepository;
import lombok.Data;

@Data
@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userrepo;

    public UserEntity createUser(User userDto) {
        validateUserCredentials(userDto.getUsername(), userDto.getPassword(),userDto.getEmail());

        UserEntity user1 = new UserEntity();
        user1.setUserId(userDto.getUser_id());
        user1.setUserName(userDto.getUsername());
        user1.setEmail(userDto.getEmail());
        user1.setPassword(userDto.getPassword());

        return userrepo.save(user1);
    }

    public boolean deleteUser(int userId) throws IdNotFoundException {
        Optional<UserEntity> optionalUser = userrepo.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            if (user.getUserId() == userId) {
                userrepo.deleteById(userId);
                return true; 
            } else {
                throw new IdNotFoundException("User ID mismatch.");
            }

        } else {
            throw new IdNotFoundException("User not found with ID: " + userId);
        }
    }

    public UserEntity updateUser(int userId, User updatedUser) throws UserNotFoundException {
        validateUserCredentials(updatedUser.getUsername(), updatedUser.getPassword(),updatedUser.getEmail());

        Optional<UserEntity> optionalUser = userrepo.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();

            existingUser.setUserName(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());

            return userrepo.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    public UserEntity getUserById(int userId) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userrepo.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    public List<UserEntity> getAllUsers() {
        return userrepo.findAll();
    }

    private void validateUserCredentials(String username, String password,String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        if (username.length()<5) {
            throw new IllegalArgumentException("Username must be atleast 5 characters long.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        
    }
}
