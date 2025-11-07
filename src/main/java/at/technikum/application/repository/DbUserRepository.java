package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.dto.users.UserUpdatedDto;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.DatabaseConnectionException;
import at.technikum.application.exception.SQLToUserException;
import at.technikum.application.exception.UniqueViolationException;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUserRepository implements UserRepository {

    private final ConnectionPool connectionPool;

    private static final String SELECT_ALL_USERS
            = "SELECT * FROM users";

    private static final String SELECT_BY_ID
            = "SELECT * FROM users WHERE uid = ?";

    private static final String SELECT_BY_USERNAME
            = "SELECT * FROM users WHERE username = ?";

    private static final String SELECT_BY_EMAIL
            = "SELECT * FROM users WHERE email = ?";

    private static final String UPDATE_USER
            = "UPDATE users SET username = ?, password = ? WHERE uid = ? ";

    private static final String SAVE
            = "INSERT INTO users (uid,username,password,email,usertype) VALUES (?,?,?,?,?)";

    private static final String DELETE
            = "DELETE FROM users WHERE uid = ? RETURNING username";

    public DbUserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User findByID(String id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID);
        ) {
            prestmt.setObject(1,java.util.UUID.fromString(id));

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return setUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public User findByUsername(String username) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_USERNAME);
        ) {
            prestmt.setString(1,username);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return setUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public User findByEmail(String email) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_EMAIL);
        ) {
            prestmt.setString(1,email);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return setUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public List<User> userList() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_USERS);
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                List<User> userList = new ArrayList<>();

                while (rs.next()) {
                    userList.add(setUser(rs));
                }

                return userList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show userlist");
        }
    }

    @Override
    public List<Rating> ratings(String id) {
        Rating rating = new Rating();
        return rating.createMockRatings();
    }

    @Override
    public List<Media> favorites(String id) {
        Media media = new Media();
        return media.createMockMedia();
    }

    @Override
    public UserUpdatedDto update(UserUpdateDto update) {
        User user = findByID(update.getId());
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(UPDATE_USER);
        ) {
            if (user.getPassword().equals(update.getPasswordOld())) {
                String newPsw = checkPassword(update);
                prestmt.setString(1,update.getUsername());
                prestmt.setString(2,newPsw);
                prestmt.setObject(3,java.util.UUID.fromString(update.getId()));
                prestmt.executeUpdate();
                user.setUsername(update.getUsername());
                user.setPassword(newPsw);
                return updateToUpdated(update, user);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new UniqueViolationException("Username or email already exists");
            }
            throw new DatabaseConnectionException("Could not update user");
        }
        return null;
    }

    @Override
    public String delete(String id) {
       try (
               Connection conn = connectionPool.getConnection();
               PreparedStatement prestmt = conn.prepareStatement(DELETE);
           ) {
           prestmt.setObject(1,java.util.UUID.fromString(id));

           try (ResultSet rs = prestmt.executeQuery()){
               if (!rs.next()) {
                   return null;
               }
               return rs.getString("username");
           }
       } catch (SQLException e) {
           throw new DatabaseConnectionException("Could not delete user");
       }
    }

    @Override
    public User save(User user) {
        try (
            Connection conn = connectionPool.getConnection();
            PreparedStatement prestmt = conn.prepareStatement(SAVE);
        ) {
            prestmt.setObject(1, java.util.UUID.fromString(user.getId()));
            prestmt.setString(2, user.getUsername());
            prestmt.setString(3, user.getPassword());
            prestmt.setString(4, user.getEmail());
            prestmt.setString(5,user.getUserType().getType());
            prestmt.executeUpdate();

            return user;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new UniqueViolationException("Username or email already exists");
            }
            throw new SQLToUserException("Could not save user");
        }
    }

    @Override
    public UserLoggedInDto login(UserLoginDto userLogin) {
        User checkUser = findByUsername(userLogin.getUsername());
        if (checkUser == null) {
            return null;
        }
        if (checkUser.getPassword().equals(userLogin.getPassword())) {
            return new UserLoggedInDto(checkUser.getUsername(),checkUser.getId());
        }
        return null;
    }

    private String checkPassword(UserUpdateDto update) {
        if (update.getPasswordNew1()!=null && !update.getPasswordNew1().isEmpty()) {
            return update.getPasswordNew1();
        }
        return update.getPasswordOld();
    }

    private UserUpdatedDto updateToUpdated(UserUpdateDto update, User user) {
        UserUpdatedDto userUpdated = new UserUpdatedDto();
        userUpdated.setUsername(update.getUsername());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setId(user.getId());
        return userUpdated;
    }

    private User setUser(ResultSet rs) throws SQLException {
        try {
            User user = new User();
            user.setId(rs.getString("uid"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            String ut = rs.getString("usertype");
            if (ut.equals("user")){
                user.setUserType(UserType.User);
            } else if (ut.equals("admin")){
                user.setUserType(UserType.Admin);
            }
            return user;
        } catch (SQLException e) {
            throw new SQLToUserException("Can not set up user");
        }
    }
}
