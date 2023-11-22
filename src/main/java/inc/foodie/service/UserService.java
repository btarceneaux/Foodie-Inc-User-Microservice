package inc.foodie.service;

import inc.foodie.bean.User;
import inc.foodie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository repository;

    public List<User> getAllUsers()
    {
        return repository.findAll();
    }

    public Optional<User> getUserByUserId(int userId)
    {
        boolean exists = repository.existsById(userId);

        if(exists)
        {
            return repository.findById(userId);
        }
        else
        {
            return Optional.empty();
        }
    }

    public User createUser(User myUser)
    {
        Optional<User> tempUser = repository.findById(myUser.getUserId());
        if(tempUser.isPresent())
        {
            return null;
        }
        else
        {
            return repository.save(myUser);
        }
    }

    public int deleteUser(int userId)
    {
        int result = 0;

        if(repository.existsById(userId))
        {
            repository.deleteById(userId);
            result = 1;
        }

        return result;
    }

    public User updateUser(User myUser)
    {
        if(repository.existsById(myUser.getUserId()))
        {
            return repository.save(myUser);
        }
        else
        {
            return null;
        }
    }

    public User login(String emailAddress)
    {
        return repository.findByEmailAddress(emailAddress);
    }
}