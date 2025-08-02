package cfg.proj.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public User createUser(User userDto) {
        validateUserCredentials(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDto.getUserId());
        userEntity.setUserName(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setRole(userDto.getRole());

        UserEntity savedUser = userrepo.save(userEntity);

        
        User responseDto = new User();
       responseDto.setUserId(savedUser.getUserId());
        responseDto.setUsername(savedUser.getUserName());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setRole(savedUser.getRole());

        return responseDto;
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

    public User updateUser(int userId, User updatedUser) throws UserNotFoundException {
        validateUserCredentials(updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getEmail());

        Optional<UserEntity> optionalUser = userrepo.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            existingUser.setUserName(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            UserEntity savedUser = userrepo.save(existingUser);
            User dto = new User();
            dto.setUserId(savedUser.getUserId());
            dto.setUsername(savedUser.getUserName());
            dto.setEmail(savedUser.getEmail());

            return dto;
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }


    public User getUserById(int userId) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userrepo.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            User dto = new User();
            dto.setUserId(userEntity.getUserId());
            dto.setUsername(userEntity.getUserName());
            dto.setEmail(userEntity.getEmail());
            //dto.setPassword(userEntity.getPassword());
            return dto;
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }
    
    public User loginUser(String username, String password) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userrepo.findByUserNameAndPassword(username, password);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            User dto = new User();
            dto.setUserId(userEntity.getUserId());
            dto.setUsername(userEntity.getUserName());
            dto.setEmail(userEntity.getEmail());
            dto.setRole(userEntity.getRole());
            return dto;
        } else {
            throw new UserNotFoundException("Invalid username or password.");
        }
    }



    public List<User> getAllUsers() {
        List<UserEntity> users = userrepo.findAll();
        return users.stream().map(user -> {
            User dto = new User();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUserName());
            dto.setEmail(user.getEmail());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public boolean updatePasswordByUsername(String username, String newPassword) {
        Optional<UserEntity> optionalUser = userrepo.findByUserName(username);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setPassword(newPassword); // You can hash it here if needed
            userrepo.save(user);
            return true;
        }
        return false;
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
