package at.technikum.application.ccc;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthenticatedException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.User;
import at.technikum.application.repository.UserRepository;
import at.technikum.server.http.Method;

import java.util.Optional;
import java.util.UUID;

public class AuthMiddleware {
    private final UserRepository userRepository;
    private String[] path;
    private Method method;

    public AuthMiddleware(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User authenticate (RequestDto requestDto) {
        this.path= requestDto.getPath();
        this.method = requestDto.getMethod();
        if (noNeed()){
           return null;
        } else {
            // check token and if there is an uid, compare
            String token = requestDto.getToken();
            UUID pathUserID = getUserId();

            if (token == null || token.isEmpty())
            {
                Optional<User> checkUser = this.userRepository.findByID(pathUserID);
                if (checkUser.isEmpty()) {
                    throw new EntityNotFoundException("Could not find user (id) ");
                }
                return checkUser.get();
            } else {
                User tokenUser =  getUserByToken(token);
                if (tokenUser.getId().equals(pathUserID) || pathUserID == null) {
                    return tokenUser;
                } else {
                    throw new NotAuthenticatedException("Token does not match user ID");
                }
            }
        }
    }

    private boolean noNeed() {
        if (path[1].equals("users") && path.length==2) {
            return true;
        } else if (path[1].equals("leaderboard")) {
            return true;
        } else if (path[1].equals("media")) {
            return method.equals(Method.GET);
        } else if (path[1].equals("ratings")) {
            return method.equals(Method.GET);
        } else if (path[1].equals("users") && path.length==3) {
            return (path[2].equals("login") || path[2].equals("register"));
        }  else {
            return false;
        }
    }

    private UUID getUserId() {
        String id ="";
        if (path[1].equals("users") && !path[2].equals("login") && !path[2].equals("register")) {
            id = path[2];

            try {
                UUID uid = UUID.fromString(id);
                return uid;
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException("ID is not a valid UUID");
            }
        }
        return null;
    }

    private User getUserByToken(String token)
    {
        final String suffix = "-mrpToken";
        if (!token.endsWith(suffix))
        {
            throw new NotAuthenticatedException("Not authorized (Token - setup)");
        }
        String usernameFromToken = token.substring(0, token.length() - suffix.length());
        Optional<User> checkUser = this.userRepository.findByUsername(usernameFromToken);

        if (checkUser.isEmpty()) {
            throw new EntityNotFoundException("Not authenticated (Token - Username)");
        }
        return checkUser.get();
    }
}
