package inc.foodie.repository;

import inc.foodie.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    public User findByEmailAddress(String emailAddress);
}
