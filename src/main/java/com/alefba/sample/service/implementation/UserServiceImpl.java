package com.alefba.sample.service.implementation;

import com.alefba.sample.model.Role;
import com.alefba.sample.model.User;
import com.alefba.sample.repository.RoleRepository;
import com.alefba.sample.repository.UserRepository;
import com.alefba.sample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static com.alefba.sample.model.RoleName.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(User user) {
        var encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);

        Optional<Role> role = roleRepository.findByName(ROLE_USER);

        role.ifPresent(value -> user.setRoles(Set.of(value)));

        repository.save(user);

        return user;
    }

}
