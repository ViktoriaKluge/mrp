package at.technikum.application.ccc;

import at.technikum.application.dto.authmiddleware.RequestDto;
import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.exception.NotAuthorizedException;
import at.technikum.application.model.User;
import at.technikum.application.repository.MemoryUserRepository;

public class AuthMiddleware {
    private final MemoryUserRepository userRepository;
    private String[] path;

    public AuthMiddleware(MemoryUserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public void authenticate (RequestDto requestDto) {
        this.path= requestDto.getPath();
        if (noNeed()){
            return;
        }

        String id = getUserId(requestDto);
        User user = this.userRepository.findByID(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        String token = requestDto.getToken();
        final String suffix = "-mrpToken";
        if (token == null || token.isEmpty() || !token.equals(user.getUsername()+suffix)) {
            throw new NotAuthorizedException("Not authorized (Token)");
        }
    }

    private boolean noNeed() {
        switch (path[1]){
            case "users":
                if (path[2].equals("login") || path[2].equals("register")) {
                    return true;
                }
                break;
            case "media":
                if (path[2] == null || path[2].isEmpty()) {
                    return true;
                }
                break;
            case "leaderboard":
                return true;
            default:
                return false;
        }
        return false;
    }

    private String getUserId(RequestDto requestDto) {
        if (path[1].equals("users")) {
            return path[2];
        } else {
            String id = requestDto.getId();
            if (id == null || id.isEmpty()) {
                throw new NotAuthorizedException("No user id found");
            } else {
                return id;
            }
        }
    }
}
