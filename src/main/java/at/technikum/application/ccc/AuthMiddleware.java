package at.technikum.application.ccc;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
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
            // first check token
            String token = requestDto.getToken();

            if (!token.isEmpty())
            {
                return getUserByToken(token);
            }
            else {
            // if no token, then check ID
                return getUserById(requestDto);
            }
        }
    }

    private boolean noNeed() {
        if (path[1].equals("users") && path.length==2) {
            return true;
        }
        else if (path[1].equals("users") && path.length==3) {
            return (path[2].equals("login") || path[2].equals("register"));
        } else if (path[1].equals("media")) {
           return method.equals(Method.GET);
        } else {
            return path[2].equals("leaderboard");
        }
    }

    private User getUserById(RequestDto requestDto) {
        String id ="";
        if (path[1].equals("users")) {
            id = path[2];
        } else {
            id = requestDto.getId();
            if (id == null || id.isEmpty()) {
                throw new NotAuthorizedException("No user id found");
            }
        }
        UUID uid = UUID.fromString(id);
        Optional<User> checkUser = this.userRepository.findByID(uid);
        if (checkUser.isEmpty()) {
            throw new EntityNotFoundException("Not authenticated (User not found)");
        }

        return checkUser.get();
    }

    private User getUserByToken(String token)
    {
        final String suffix = "-mrpToken";
        if (!token.endsWith(suffix))
        {
            throw new NotAuthorizedException("Not authorized (Token)");
        }
        String usernameFromToken = token.substring(0, token.length() - suffix.length());
        Optional<User> checkUser = this.userRepository.findByUsername(usernameFromToken);

        if (checkUser.isEmpty()) {
            throw new NotAuthorizedException("Not authenticated (Token - Username)");
        }
        return checkUser.get();
    }
}
