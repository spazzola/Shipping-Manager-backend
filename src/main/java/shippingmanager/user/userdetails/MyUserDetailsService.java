package shippingmanager.user.userdetails;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shippingmanager.user.User;
import shippingmanager.user.UserDao;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.List;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserDao userDao;

    public MyUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public MyUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final User user = userDao.findByLogin(login)
                .orElseThrow(NoSuchElementException::new);
        return new MyUserDetails(user);
    }
/*
    public boolean login(LoginForm loginForm) {
        final UserDetails userDetails = loadUserByUsername(loginForm.getLogin());
        return passwordEncoder.matches(loginForm.getPassword(), userDetails.getPassword());
    }
*/

    public String extractRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.toString().replace("[", "").replace("]", "");
    }
}