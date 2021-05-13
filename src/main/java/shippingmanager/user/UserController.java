package shippingmanager.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import shippingmanager.user.jwt.AuthenticationRequest;
import shippingmanager.user.jwt.AuthenticationResponse;
import shippingmanager.user.jwt.JwtUtil;
import shippingmanager.user.security.SecurityConfiguration;
import shippingmanager.user.userdetails.MyUserDetails;
import shippingmanager.user.userdetails.MyUserDetailsService;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
//@Log4j2
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private UserService userService;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService myUserDetailsService;
    private JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    //private Logger logger;


    @GetMapping("/getAll")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getUser")
    public UserDto getUser(@RequestParam("id") Long id) throws Exception {
        User user = userService.getUser(id);

        return userMapper.toDto(user);
    }

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserDto userDto) throws Exception {
        User user = userService.registerUser(userDto);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") Long id) throws Exception {
        userService.deleteUser(id);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                       HttpServletRequest httpServletRequest) throws Exception {

        String ipAddress = SecurityConfiguration.getClientIpAddress(httpServletRequest);

        //logger.info("Logowanie na konto: " + authenticationRequest.getLogin() + " , IP: " + ipAddress);

//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getPassword())
//            );
//        } catch (BadCredentialsException e) {
//            logger.error("Blad logowania, nieprawidlowe haslo");
//            throw new RuntimeException("Nieprawidłowe hasło!");
//        }

        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        boolean isMatch = passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword());
        String jwt;

        if (!isMatch) {
            //logger.error("Blad logowania, nieprawidlowe haslo");
            throw new RuntimeException("Nieprawidłowe hasło!");
        } else {

            jwt = jwtUtil.generateToken(userDetails);

            //logger.info("Zalogowano");
        }
        String role = myUserDetailsService.extractRole(userDetails.getAuthorities());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, role, userDetails.getUsername(), userDetails.getName(), userDetails.getSurname()));
    }

}