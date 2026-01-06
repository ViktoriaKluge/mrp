package at.technikum.application.dto.auth;

import at.technikum.application.enums.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateDtoTest {

    // positive
    // valid input
    @Test
    void givenValidInput_whenIsUser_thenReturnTrue() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("username");
        userCreateDto.setUserType(UserType.User.getType());
        userCreateDto.setPassword1("password");
        userCreateDto.setPassword2(userCreateDto.getPassword1());
        userCreateDto.setEmail("email@example.com");

        boolean result = userCreateDto.isUser();

        assertTrue(result);

    }

    // negative
    // no username
    @Test
    void givenNoUsername_whenIsUser_thenReturnFalse() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUserType(UserType.User.getType());
        userCreateDto.setPassword1("password");
        userCreateDto.setPassword2(userCreateDto.getPassword1());
        userCreateDto.setEmail("email@example.com");

        boolean result = userCreateDto.isUser();

        assertFalse(result);

    }

    // invalid usertype
    @Test
    void givenInvalidUsertype_whenIsUser_thenReturnFalse() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("username");
        userCreateDto.setUserType("invalidUserType");
        userCreateDto.setPassword1("password");
        userCreateDto.setPassword2(userCreateDto.getPassword1());
        userCreateDto.setEmail("email@example.com");

        boolean result = userCreateDto.isUser();

        assertFalse(result);
    }

    // passwords dont match
    @Test
    void givenInvalidPasswords_whenIsUser_thenReturnFalse() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("username");
        userCreateDto.setUserType(UserType.User.getType());
        userCreateDto.setPassword1("password");
        userCreateDto.setPassword2("otherpassword");
        userCreateDto.setEmail("email@example.com");

        boolean result = userCreateDto.isUser();

        assertFalse(result);
    }

    // invalid email adress
    @Test
    void givenInvalidEmail_whenIsUser_thenReturnFalse() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("username");
        userCreateDto.setUserType(UserType.User.getType());
        userCreateDto.setPassword1("password");
        userCreateDto.setPassword2(userCreateDto.getPassword1());
        userCreateDto.setEmail("noemail");

        boolean result = userCreateDto.isUser();

        assertFalse(result);
    }

}