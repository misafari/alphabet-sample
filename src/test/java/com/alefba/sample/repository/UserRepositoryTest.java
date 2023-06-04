package com.alefba.sample.repository;

import com.alefba.sample.model.User;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.config.BootstrapMode;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest(bootstrapMode = BootstrapMode.LAZY)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("UserRepositoryTest Persist tests")
    class UserRepositoryTest_Persist {
        @Test
        @DisplayName("should be ok")
        void persist_OK() {
            var user = getSimpleUser();
            User savedUser = entityManager.persistAndFlush(user);
            then(savedUser).isNotNull();
        }

        @Test
        @DisplayName("should be failed (username: null)")
        void persist_ValidationException_UsernameNull() {
            var user = getSimpleUser();
            user.setUsername(null);
            Throwable throwable = catchThrowable(() -> entityManager.persistAndFlush(user));
            BDDAssertions.then(throwable).isInstanceOf(PersistenceException.class);
        }

        @Test
        @DisplayName("should be failed (username: blank)")
        void persist_ValidationException_UsernameBlank() {
            var user = getSimpleUser();
            user.setUsername("");
            Throwable throwable = catchThrowable(() -> entityManager.persistAndFlush(user));
            BDDAssertions.then(throwable).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("should be failed (password: blank)")
        void persist_ValidationException_PasswordBlank() {
            var user = getSimpleUser();
            user.setPassword("");
            Throwable throwable = catchThrowable(() -> entityManager.persistAndFlush(user));
            BDDAssertions.then(throwable).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("should be failed (password: null)")
        void persist_ValidationException_PasswordNull() {
            var user = getSimpleUser();
            user.setPassword(null);
            Throwable throwable = catchThrowable(() -> entityManager.persistAndFlush(user));
            BDDAssertions.then(throwable).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("UserRepositoryTest Fetch tests")
    class UserRepositoryTest_Fetch {
        @Test
        @DisplayName("Find user by username and deletedAt is null should be ok")
        void findByUsernameAndDeletedAtIsNull_OK() {
            var user = getSimpleUser();
            var savedUser = entityManager.persistAndFlush(user);
            var fetchedUser = repository.findByUsernameAndDeletedAtIsNull("testUser");

            then(savedUser.getUsername()).isNotNull();
            then(fetchedUser.isPresent()).isTrue();
            then(fetchedUser.get().getUsername()).isEqualTo(savedUser.getUsername());
        }

        @Test
        @DisplayName("Find user by username and deletedAt is null should be empty (deleted)")
        void findByUsernameAndDeletedAtIsNull_Deleted() {
            var user = getSimpleUser();
            user.setDeletedAt(LocalDateTime.now());
            entityManager.persistAndFlush(user);
            var fetchedUser = repository.findByUsernameAndDeletedAtIsNull("testUser");

            then(fetchedUser.isPresent()).isFalse();
        }

        @Test
        @DisplayName("Find user by username and deletedAt is null should be null")
        void findByUsernameAndDeletedAtIsNull_Empty() {
            var fetchedUser = repository.findByUsernameAndDeletedAtIsNull("testUser");
            then(fetchedUser.isPresent()).isFalse();
        }
    }

    private User getSimpleUser() {
        User user = new User();

        user.setUsername("testUser");
        user.setPassword("testPass");

        return user;
    }
}
