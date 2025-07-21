package cfg.proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cfg.proj.DTO.User;
import cfg.proj.bo.ResponseData;
import cfg.proj.exceptions.UserNotFoundException;
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
            User user = userService.createUser(userDto);
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
            User user = userService.getUserById(userId);
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
            List<User> users = userService.getAllUsers();
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
    @PutMapping("/{id}")
    public ResponseData updateUser(@PathVariable("id") int id, @RequestBody User updatedUser) {
        ResponseData response = new ResponseData();
        try {
            User updatedEntity = userService.updateUser(id, updatedUser);
            response.setStatus("success");
            response.setMessage("User updated successfully.");
            response.setData(updatedEntity);
        } catch (UserNotFoundException e) {
            response.setStatus("error");
            response.setMessage("User not found: " + e.getMessage());
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error updating user: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
