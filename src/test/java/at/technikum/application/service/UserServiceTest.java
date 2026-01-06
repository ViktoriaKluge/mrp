package at.technikum.application.service;

import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.UnprocessableEntityException;
import at.technikum.application.model.User;
import at.technikum.application.repository.MediaRepository;
import at.technikum.application.repository.RatingRepository;
import at.technikum.application.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private MediaRepository mediaRepository;
    @InjectMocks
    private UserService userService;

    // positive
    // update valid input with old password
    @Test
    void givenValidOnlyOldPassword_whenUpdateUser_thenReturnUserLoggedIn() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("email@example.com");
        UserUpdateDto update = spy(new UserUpdateDto());
        update.setUsername("username");
        update.setPasswordOld("password");
        UserLoggedInDto dto = new UserLoggedInDto();

        doReturn(true).when(update).isUpdate();
        when(userRepository.update(any(UserUpdateDto.class))).thenReturn(Optional.of(dto));

        UserLoggedInDto result = userService.update(user,update);

        assertNotNull(result);
        ArgumentCaptor<UserUpdateDto> captor = ArgumentCaptor.forClass(UserUpdateDto.class);
        verify(userRepository, times(1)).update(captor.capture());

        UserUpdateDto sentToRepo = captor.getValue();

        assertNotNull(sentToRepo);
        assertEquals("username",sentToRepo.getUsername());
        assertEquals("password",sentToRepo.getPasswordNew1());
        assertEquals("email@example.com",sentToRepo.getEmail());
        assertEquals(user.getId(),sentToRepo.getId());
    }

    // update valid input with new password
    @Test
    void givenValidNewPassword_whenUpdateUser_thenReturnUserLoggedIn() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("email@example.com");
        UserUpdateDto update = spy(new UserUpdateDto());
        update.setUsername("username");
        update.setPasswordOld("passwordOld");
        update.setPasswordNew1("newPassword");
        update.setEmail("new@email.com");
        UserLoggedInDto dto = new UserLoggedInDto();

        doReturn(true).when(update).isUpdate();
        when(userRepository.update(any(UserUpdateDto.class))).thenReturn(Optional.of(dto));

        UserLoggedInDto result = userService.update(user,update);

        assertNotNull(result);
        ArgumentCaptor<UserUpdateDto> captor = ArgumentCaptor.forClass(UserUpdateDto.class);
        verify(userRepository, times(1)).update(captor.capture());

        UserUpdateDto sentToRepo = captor.getValue();

        assertNotNull(sentToRepo);
        assertEquals("username",sentToRepo.getUsername());
        assertEquals("newPassword",sentToRepo.getPasswordNew1());
        assertEquals("new@email.com",sentToRepo.getEmail());
    }

    // delete valid input
    @Test
    void givenOptionalValid_whenDeleteUser_thenReturnString() {
        User user = new User();

        when(userRepository.delete(user)).thenReturn(Optional.of("deleted"));

        String result = userService.delete(user);

        assertNotNull(result);
        assertEquals("deleted", result);

        verify(userRepository, times(1)).delete(user);
    }

    // negative
    // update repo sends empty optional
    @Test
    void givenEmptyOptional_whenUpdateUser_thenThrowException() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("email@example.com");
        UserUpdateDto update = spy(new UserUpdateDto());
        update.setUsername("username");
        update.setPasswordOld("passwordOld");

        doReturn(true).when(update).isUpdate();
        when(userRepository.update(any(UserUpdateDto.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(user,update));
        verify(userRepository, times(1)).update(any(UserUpdateDto.class));
    }

    // update is no update
    @Test
    void givenIsUpdateFalse_whenUpdateUser_thenThrowException() {
        User user = new User();
        UserUpdateDto update = spy(new UserUpdateDto());

        doReturn(false).when(update).isUpdate();

        assertThrows(UnprocessableEntityException.class, () -> userService.update(user,update));
        verify(userRepository, never()).update(any(UserUpdateDto.class));
    }

    // delete optional empty
    @Test
    void givenOptionalEmpty_whenDeleteUser_thenThrowException() {
        User user = new User();

        when(userRepository.delete(user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(user));
        verify(userRepository, times(1)).delete(any(User.class));
    }

}