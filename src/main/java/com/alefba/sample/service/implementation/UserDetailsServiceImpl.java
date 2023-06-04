package com.alefba.sample.service.implementation;

import com.alefba.sample.repository.UserRepository;
import com.alefba.sample.util.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Cacheable(value = "user", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new RecordNotFoundException(username));
    }
}
