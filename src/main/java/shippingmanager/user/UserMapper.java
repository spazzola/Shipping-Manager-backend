package shippingmanager.user;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .role(user.getRole())
                .build();
    }

    public List<UserDto> toDto(List<User> buyers) {
        return buyers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}