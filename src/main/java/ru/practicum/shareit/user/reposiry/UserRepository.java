package ru.practicum.shareit.user.reposiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.name = ?1, u.email = ?2 where u.id = ?3")
    int updateNameAndEmailById(String name, String email, long id);
    @Transactional
    @Modifying
    @Query("update User u set u.email = ?1 where u.id = ?2")
    int updateEmailById(String email, long id);
    @Transactional
    @Modifying
    @Query("update User u set u.name = ?1 where u.id = ?2")
    int updateNameById(String name, long id);

    @Override
    boolean existsById(Long aLong);
}
