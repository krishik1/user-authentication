package user.authentication.flow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.authentication.flow.model.UserSession;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<UserSession,Long> {
    Optional<UserSession> findByTokenAndUser_Id(String token, Long userid);
}
