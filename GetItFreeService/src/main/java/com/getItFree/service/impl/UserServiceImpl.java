package com.getItFree.service.impl;

import com.getItFree.model.Advert;
import com.getItFree.model.Authority;
import com.getItFree.model.User;
import com.getItFree.repository.UserRepository;
import com.getItFree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void resetCredentials() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }

    @Override
    // @PreAuthorize("hasRole('USER')")
    public User findByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username);
        return u;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User findById(Long id) throws AccessDeniedException {
//        User u = userRepository.findOne(id);
        return null;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() throws AccessDeniedException {
        List<User> result = userRepository.findAll();
        return result;
    }

    @Override
    public User save(User userRequest) {
        userRequest.setAuthority(List.of(Authority.ROLE_USER));
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        this.userRepository.save(userRequest);
        return userRequest;
    }

    @Override
    public Advert addAdvert(Long id, Advert advert) {
        Optional<User> user = userRepository.findById(id);
//        user.ifPresentOrElse(u -> u.addAdvert(advert), () -> throwError(id));

        return advert;
    }

    private void throwError(Long id) {
        throw new RuntimeException("Not found " + id);
    }

    @Override
    public List<Advert> findAllUserAdverst(Long userId) {

        return userRepository.findById(userId)
                .map(User::getAdverts)
                .orElse(Collections.emptyList());
    }
}
