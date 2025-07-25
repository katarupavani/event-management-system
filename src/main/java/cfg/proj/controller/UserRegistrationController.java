package cfg.proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cfg.proj.DTO.User;
import cfg.proj.Entities.UserEntity;
import cfg.proj.bo.ResponseData;
import cfg.proj.service.UserRegistrationService;

@RestController
@RequestMapping("/users")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userService;

    @PostMapping("/create")
    public ResponseData createUser(@RequestBody User userDto) {
        ResponseData response = new ResponseData();
        try {
            UserEntity user = userService.createUser(userDto);
            response.setStatus("success");
            response.setMessage("User created successfully.");
            response.setData(user);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error while creating user: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData getUserById(@PathVariable("id") int userId) {
        ResponseData response = new ResponseData();
        try {
            UserEntity user = userService.getUserById(userId);
            if (user != null) {
                response.setStatus("success");
                response.setMessage("User found.");
                response.setData(user);
            } else {
                response.setStatus("failed");
                response.setMessage("User not found with ID: " + userId);
                response.setData(null);
            }
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching user: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping
    public ResponseData getAllUsers() {
        ResponseData response = new ResponseData();
        try {
            List<UserEntity> users = userService.getAllUsers();
            response.setStatus("success");
            response.setMessage("Users fetched successfully.");
            response.setData(users);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching users: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteUser(@PathVariable("id") int userId) {
        ResponseData response = new ResponseData();
        try {
            boolean deleted = userService.deleteUser(userId);
            if (deleted) {
                response.setStatus("success");
                response.setMessage("User deleted successfully.");
            } else {
                response.setStatus("failed");
                response.setMessage("User not found with ID: " + userId);
            }
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error deleting user: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
