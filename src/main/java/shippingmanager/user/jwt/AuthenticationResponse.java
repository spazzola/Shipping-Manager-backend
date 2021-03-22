package shippingmanager.user.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationResponse {

    private final String jwt;
    private final String role;
    private final String login;
    private final String name;
    private final String surname;

}