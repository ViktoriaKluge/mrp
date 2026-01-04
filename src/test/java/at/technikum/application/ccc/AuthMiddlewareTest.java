package at.technikum.application.ccc;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthenticatedException;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthMiddlewareTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthMiddleware authMiddleware;

    // positive
    @Test
    public void givenPathUsersIdWithToken_whenAuthenticate_thenReturnUser(){
        RequestDto requestDto = new RequestDto();
        UUID exptId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[] {"", "users", exptId.toString()});
        requestDto.setToken("user-mrpToken");
        User expectedUser = new User();
        expectedUser.setUsername("user");
        expectedUser.setId(exptId);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(expectedUser));

        User result = authMiddleware.authenticate(requestDto);

        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals(exptId, result.getId());
        verify(userRepository, times(1)).findByUsername("user");
    }

    @Test
    public void givenPathUsersIdWithoutToken_whenAuthenticate_thenReturnUser(){
        RequestDto requestDto = new RequestDto();
        UUID exptId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[] {"", "users", exptId.toString()});
        User expectedUser = new User();
        expectedUser.setUsername("user");
        expectedUser.setId(exptId);

        when(userRepository.findByID(exptId)).thenReturn(Optional.of(expectedUser));

        User result = authMiddleware.authenticate(requestDto);

        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals(exptId, result.getId());
        verify(userRepository, times(1)).findByID(exptId);
    }

    @Test
    public void givenPathUsersLogin_whenAuthenticate_thenReturnUser(){
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "login"});

        User result = authMiddleware.authenticate(requestDto);

        assertNull(result);
        verifyNoInteractions(userRepository);
    }

    // negative
    @Test
    public void givenPathUserIdInvalidToken_whenAuthenticate_thenThrowException(){
        RequestDto requestDto = new RequestDto();
        UUID mockId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[] {"", "users", mockId.toString()});
        requestDto.setToken("NotAToken");

        assertThrows(NotAuthenticatedException.class, ()->{
            authMiddleware.authenticate(requestDto);
        });
        verifyNoInteractions(userRepository);
    }

    @Test
    public void givenPathUserIdNoUserWithThisToken_whenAuthenticate_thenThrowException(){
        RequestDto requestDto = new RequestDto();
        UUID mockId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[] {"", "users", mockId.toString()});
        requestDto.setToken("user-mrpToken");

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()->{
            authMiddleware.authenticate(requestDto);
        });
    }

    @Test
    public void givenPathUsersInvalidUUID_whenAuthenticate_thenThrowException(){
        RequestDto requestDto = new RequestDto();
        requestDto.setPath(new String[] {"", "users", "notAnUUID"});

        assertThrows(EntityNotFoundException.class, ()->{
            authMiddleware.authenticate(requestDto);
        });
        verifyNoInteractions(userRepository);
    }

    @Test
    public void givenPathUsersNoUserWithThisIdWithoutToken_whenAuthenticate_thenThrowException() {
        RequestDto requestDto = new RequestDto();
        UUID mockId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[]{"", "users", mockId.toString()});

        when(userRepository.findByID(mockId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()->{
            authMiddleware.authenticate(requestDto);
        });
    }

    @Test
    public void givenPathUsersIdAndTokenDontMatch_whenAuthenticate_thenThrowException() {
        RequestDto requestDto = new RequestDto();
        UUID pathId = UUID.fromString("ef3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        UUID tokenId = UUID.fromString("ff3e67e1-9a5e-49ce-a2a7-95aa2209b295");
        requestDto.setPath(new String[]{"", "users", pathId.toString()});
        requestDto.setToken("user-mrpToken");
        User tokenUser = new User();
        tokenUser.setId(tokenId);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(tokenUser));

        assertThrows(NotAuthenticatedException.class, ()->{
            authMiddleware.authenticate(requestDto);
        });
    }
}