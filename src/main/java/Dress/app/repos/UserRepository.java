package Dress.app.repos;

import Dress.app.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.password = :password")
    User findUserByNameAndPassword(@Param("name") String name, @Param("password") String password);
}
