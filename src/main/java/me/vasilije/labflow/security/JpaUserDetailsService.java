package me.vasilije.labflow.security;

import lombok.RequiredArgsConstructor;
import me.vasilije.labflow.model.SecurityUser;
import me.vasilije.labflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with given username was not found."));
    }
}
