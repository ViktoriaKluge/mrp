package at.technikum.application.dto.users;

import at.technikum.application.exception.UnprocessableEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDtoTest {

    // positive
    // valid input, only username
    @Test
    void givenValidUsername_whenIsUpdate_thenReturnTrue(){
        UserUpdateDto update = new UserUpdateDto();
        update.setUsername("username");

        boolean result = update.isUpdate();

        assertTrue(result);
    }

    // valid input, passwords match
    @Test
    void givenValidPasswords_whenIsUpdate_thenReturnTrue(){
        UserUpdateDto update = new UserUpdateDto();
        update.setUsername("username");
        update.setPasswordNew1("password");
        update.setPasswordNew2(update.getPasswordNew1());

        boolean result = update.isUpdate();

        assertTrue(result);
    }

    // valid email
    @Test
    void givenValidEmail_whenIsUpdate_thenReturnTrue(){
        UserUpdateDto update = new UserUpdateDto();
        update.setUsername("username");
        update.setEmail("email@example.com");

        boolean result = update.isUpdate();

        assertTrue(result);
    }


    // negative
    // no username
    @Test
    void givenNoUsername_whenIsUpdate_thenThrowsException(){
        UserUpdateDto update = new UserUpdateDto();

        UnprocessableEntityException ex =
                assertThrows(UnprocessableEntityException.class,()->update.isUpdate());
        assertEquals("Username cannot be empty", ex.getMessage());
    }

    // passwords dont match
    @Test
    void givenInalidPasswords_whenIsUpdate_thenThrowsException(){
        UserUpdateDto update = new UserUpdateDto();
        update.setUsername("username");
        update.setPasswordNew1("password");
        update.setPasswordNew2("otherpassword");

        UnprocessableEntityException ex =
                assertThrows(UnprocessableEntityException.class,()->update.isUpdate());
        assertEquals("New Passwords do not match", ex.getMessage());
    }

    // invalid email
    @Test
    void givenInvalidEmail_whenIsUpdate_thenThrowsException(){
        UserUpdateDto update = new UserUpdateDto();
        update.setUsername("username");
        update.setEmail("noemail");

        UnprocessableEntityException ex =
                assertThrows(UnprocessableEntityException.class,()->update.isUpdate());
        assertEquals("No valid email adress", ex.getMessage());
    }
}