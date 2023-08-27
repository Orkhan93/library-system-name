package az.developia.librarysystemname.security;

import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private User userDetail;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        User user = userRepository.findFirstByEmail(username);
        userDetail = userRepository.findFirstByEmail(username);
        if (user == null) throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                new BCryptPasswordEncoder().encode(user.getPassword()), new ArrayList<>());
    }

    public User getUserDetail() {
        return userDetail;
    }

}