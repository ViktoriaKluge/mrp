package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.*;
import at.technikum.application.model.LeaderboardEntry;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            = "UPDATE users SET username = ?, password = ?, email = ? WHERE uid = ? RETURNING *";

    private static final String SAVE
            = "INSERT INTO users (uid,username,password,email,usertype) VALUES (?,?,?,?,?) RETURNING *";

    private static final String DELETE
            = "DELETE FROM users WHERE uid = ? RETURNING *";

    public DbUserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<User> findByID(UUID id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1,id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_USERNAME)
        ) {
            prestmt.setString(1,username);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(setUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_EMAIL)
        ) {
            prestmt.setString(1,email);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(setUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user");
        }
    }

    @Override
    public List<User> userList() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_USERS)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return List.of();
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
    public List<SQLRatingDto> ratings(User user) {
        List<SQLRatingDto> ratings = new ArrayList<>();
        return ratings;
    }

    @Override
    public List<SQLFavoriteDto> favorites(User user) {
        List<SQLFavoriteDto> media = new  ArrayList<>();
        return media;
    }

    @Override
    public Optional<UserLoggedInDto> update(UserUpdateDto update) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(UPDATE_USER)
        ) {

            prestmt.setString(1,update.getUsername());
            prestmt.setString(2,update.getPasswordNew1());
            prestmt.setString(3,update.getEmail());
            prestmt.setObject(4,update.getId());

            try ( ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(rsToLoggedIn(rs));
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new UniqueViolationException("Username or email already exists");
            }
            throw new DatabaseConnectionException("Could not update user");
        }
    }

    @Override
    public Optional<String> delete(User user) {
       try (
               Connection conn = connectionPool.getConnection();
               PreparedStatement prestmt = conn.prepareStatement(DELETE)
           ) {
           prestmt.setObject(1,user.getId());

           try (ResultSet rs = prestmt.executeQuery()){
               if (!rs.next()) {
                   return Optional.empty();
               }
               return Optional.of(rs.getString("username"));
           }
       } catch (SQLException e) {
           throw new DatabaseConnectionException("Could not delete user");
       }
    }

    @Override
    public Optional<User> save(User user) {
        try (
            Connection conn = connectionPool.getConnection();
            PreparedStatement prestmt = conn.prepareStatement(SAVE)
        ) {
            prestmt.setObject(1, user.getId());
            prestmt.setString(2, user.getUsername());
            prestmt.setString(3, user.getPassword());
            prestmt.setString(4, user.getEmail());
            prestmt.setString(5, user.getUserType().getType());

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setUser(rs));
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new UniqueViolationException("Username or email already exists");
            }
            throw new ObjectToSQLException("Could not save user");
        }
    }

    @Override
    public Optional<UserLoggedInDto> login(UserLoginDto userLogin) {
        Optional<User> checkUser = findByUsername(userLogin.getUsername());
        if (checkUser.isEmpty()) {
            return Optional.empty();
        }
        User foundUser = checkUser.get();
        if (foundUser.getPassword().equals(userLogin.getPassword())) {
            UserLoggedInDto loggedIn = new UserLoggedInDto(foundUser.getUsername(),foundUser.getId());
            return Optional.of(loggedIn);
        }
        return Optional.empty();
    }

    private UserLoggedInDto rsToLoggedIn(ResultSet rs) throws SQLException {
        try {
            UserLoggedInDto userUpdated = new UserLoggedInDto();
            userUpdated.setUsername(rs.getString("username"));
            userUpdated.setId(rs.getObject("uid", UUID.class));
            return userUpdated;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up logged in user");
        }
    }

    private User setUser(ResultSet rs) throws SQLException {
        try {
            User user = new User();
            user.setId(rs.getObject("uid", UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            String ut = rs.getString("usertype");
            if (ut.equals("User")){
                user.setUserType(UserType.User);
            } else if (ut.equals("Admin")){
                user.setUserType(UserType.Admin);
            }
            return user;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up user");
        }
    }

    @Override
    public List<SQLMediaDto> recommendations(User user, MediaType mediaType) {
        return List.of();
    }

    @Override
    public List<LeaderboardEntry> leaderboard() {
        return List.of();
    }
}
