package inc.foodie.controller;

import inc.foodie.dto.ResponseDto;
import inc.foodie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import inc.foodie.bean.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://ec2-35-87-48-128.us-west-2.compute.amazonaws.com:4000")
public class UserController
{
    @Autowired
    UserService service;

    /**
     *
     * @return
     */
    @GetMapping("/user")
    public List<User> getAllUsers()
    {
        return service.getAllUsers();
    }

    /**
     *
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    public ResponseDto getUserById(@PathVariable int userId)
    {
        Optional<User> optionalUser = service.getUserByUserId(userId);
        ResponseDto response = new ResponseDto();

        if(optionalUser.isPresent())
        {
            User myUser = optionalUser.get();
            response.setMessage("The user was found.");
            response.setStatus(HttpStatus.OK.value());
            response.setTimestamp(new Date());
            response.setData(myUser);
        }
        else
        {
            response.setMessage("Error! User not found.");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            response.setTimestamp(new Date());
            response.setData(null);
        }

        return response;
    }

    /**
     *
     * @param myUser : The user trying to log in (email address and password.).
     * @return : ResponseDto
     */
    @PostMapping("/user/login")
    public ResponseDto loginUser(@RequestBody User myUser)
    {
       ResponseDto response = new ResponseDto();
       String encryptedPassword = myUser.getPassword();

       User databaseUser = service.login(myUser.getEmailAddress());

       boolean samePassword = BCrypt.checkpw(myUser.getPassword(), databaseUser.getPassword());

        if(samePassword)
        {
            response.setMessage("The user was successfully logged in.");
            response.setStatus(HttpStatus.OK.value());
            response.setTimestamp(new Date());
            response.setData(databaseUser);
        }
        else
        {
            response.setMessage("Error! The email address or password was incorrect.");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            response.setTimestamp(new Date());
            response.setData(null);
        }

        return response;
    }

    /**
     *
     * @param myUser
     * @return
     */
    @PostMapping("/user")
    public ResponseDto createUser(@RequestBody User myUser)
    {
        String rawPassword = myUser.getPassword();
        String encryptedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        myUser.setPassword(encryptedPassword);

        ResponseDto response = new ResponseDto();

        if(myUser.getFirstName() == null ||
           myUser.getLastName() == null ||
           myUser.getEmailAddress() == null ||
           myUser.getMyAddress() == null)
        {
            response.setMessage("The user was not successfully saved because one of the fields was blank.");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            response.setTimestamp(new Date());
            response.setData(null);

            return response;
        }

        User savedUser = service.createUser(myUser);

        if(savedUser.getUserId() > 0)
        {
            response.setMessage("The user was successfully saved.");
            response.setStatus(HttpStatus.OK.value());
            response.setTimestamp(new Date());
            response.setData(savedUser);
        }
        else
        {
            response.setMessage("The user was not successfully saved.");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            response.setTimestamp(new Date());
            response.setData(null);
        }

        return response;
    }

    /**
     *
     * @param myUser
     * @return
     */
    @PutMapping("/user")
    public ResponseDto updateUser(@RequestBody User myUser)
    {
        ResponseDto response = new ResponseDto();

        User updatedUser = service.updateUser(myUser);

        response.setMessage("The user was updated successfully.");
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(new Date());
        response.setData(updatedUser);

        return response;
    }

    @DeleteMapping("/user/{userId}")
    public ResponseDto deleteUser(@PathVariable int userId)
    {
        ResponseDto response = new ResponseDto();

        int result = service.deleteUser(userId);
        if(result == 1)
        {
            response.setMessage("The user was successfully deleted.");
            response.setStatus(HttpStatus.OK.value());
        }
        else
        {
            response.setMessage("The user was not successfully deleted.");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        }
        response.setTimestamp(new Date());
        response.setData(null);

        return response;
    }
}
