package cfg.proj.ems;
import cfg.proj.DTO.User;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.IdNotFoundException;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.UserRepository;
import cfg.proj.service.UserRegistrationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

	public class UserRegistrationServiceTest {

	    @InjectMocks
	    private UserRegistrationService userService;

	    @Mock
	    private UserRepository userRepository;

	    private User validUserDto;
	    private UserEntity savedUser;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);

	        validUserDto = new User();
	        validUserDto.setUser_id(1);
	        validUserDto.setUsername("varshini");
	        validUserDto.setPassword("securePass123");
	        validUserDto.setEmail("test@example.com");

	        savedUser = new UserEntity();
	        savedUser.setUserId(1);
	        savedUser.setUserName("testuser");
	        savedUser.setPassword("securePass123");
	        savedUser.setEmail("test@example.com");
	    }

	    @Test
	    void createUser_ValidUser_Success() {
	        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

	        UserEntity result = userService.createUser(validUserDto);

	        assertNotNull(result);
	        assertEquals("testuser", result.getUserName());
	        verify(userRepository, times(1)).save(any(UserEntity.class));
	    }

	    @Test
	    void createUser_InvalidUsername_ThrowsException() {
	        validUserDto.setUsername("");

	        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	            userService.createUser(validUserDto);
	        });

	        assertEquals("Username cannot be empty.", exception.getMessage());
	    }

	    @Test
	    void createUser_InvalidPassword_ThrowsException() {
	        validUserDto.setPassword("123");

	        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	            userService.createUser(validUserDto);
	        });

	        assertEquals("Password must be at least 8 characters long.", exception.getMessage());
	    }

	    @Test
	    void createUser_InvalidEmail_ThrowsException() {
	        validUserDto.setEmail("invalid-email");

	        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	            userService.createUser(validUserDto);
	        });

	        assertEquals("Invalid email format.", exception.getMessage());
	    }

	    @Test
	    void getUserById_UserExists_ReturnsUser() throws UserNotFoundException {
	        when(userRepository.findById(1)).thenReturn(Optional.of(savedUser));

	        UserEntity result = userService.getUserById(1);

	        assertNotNull(result);
	        assertEquals(1, result.getUserId());
	    }

	    @Test
	    void getUserById_NotFound_ThrowsException() {
	        when(userRepository.findById(1)).thenReturn(Optional.empty());

	        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
	    }

	 
	    @Test
	    void deleteUser_UserNotFound_ThrowsException() {
	        when(userRepository.findById(1)).thenReturn(Optional.empty());

	        assertThrows(IdNotFoundException.class, () -> userService.deleteUser(1));
	    }



	    @Test
	    void updateUser_UserNotFound_ThrowsException() {
	        when(userRepository.findById(1)).thenReturn(Optional.empty());

	        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1, validUserDto));
	    }

	    @Test
	    void getAllUsers_ReturnsList() {
	        when(userRepository.findAll()).thenReturn(List.of(savedUser));

	        List<UserEntity> users = userService.getAllUsers();

	        assertEquals(1, users.size());
	    }
	}


