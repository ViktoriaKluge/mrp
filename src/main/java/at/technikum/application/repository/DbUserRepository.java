package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.dto.auth.UserLoggedInDto;
import at.technikum.application.dto.auth.UserLoginDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.dto.sql.SQLRecommendationDto;
import at.technikum.application.dto.users.UserProfile;
import at.technikum.application.dto.users.UserUpdateDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.enums.UserType;
import at.technikum.application.exception.*;
import at.technikum.application.model.LeaderboardEntry;
import at.technikum.application.model.User;

import java.sql.*;
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

    private static final String COUNT_CREATED_MEDIA
            = "SELECT COUNT(*) AS media_count FROM media WHERE owner_id = ?";

    private static final String COUNT_FAVS
            = "SELECT COUNT(*) AS favs_count FROM favorite WHERE user_id = ?";

    private static final String COUNT_LIKES
            = "SELECT COUNT(*) AS likes_count FROM rating_likes WHERE user_id = ?";

    private static final String COUNT_RATINGS
            = "SELECT COUNT(*) AS ratings_count FROM ratings WHERE user_id = ?";

    private static final String FAV_GENRES
            = "SELECT g.genres, COUNT(*) AS cnt FROM favorite f JOIN media m ON m.mid = f.media_id "
                + "CROSS JOIN LATERAL unnest(m.genres) AS g(genres) "
                + "WHERE f.user_id = ? GROUP BY g.genres ORDER BY cnt DESC, g.genres LIMIT 3;";

    private static final String LEADERBOARD
            = "SELECT u.uid, u.username, COUNT(r.rid) AS ratings_count "
                + "FROM users u JOIN ratings r ON r.user_id = u.uid "
                + "GROUP BY u.uid, u.username ORDER BY ratings_count DESC, u.username LIMIT 5;";

    private static final String RECOMMENDATIONS
            = "WITH fav AS (SELECT f.media_id FROM favorite f WHERE f.user_id = ?),"
                + " fav_profile AS (SELECT m.media_type, unnest(m.genres) AS genre FROM favorite f "
                    + "  JOIN media m ON m.mid = f.media_id WHERE f.user_id = ?) "
                + " SELECT m.mid, m.title, m.media_type, m.genres, m.age_restriction, COUNT(*) AS match_score "
                + " FROM media m JOIN fav_profile p ON p.media_type = m.media_type AND p.genre = ANY(m.genres) "
                + " WHERE m.mid NOT IN (SELECT media_id FROM fav) GROUP BY m.mid, m.title, m.media_type, "
                    + " m.genres, m.age_restriction "
                + " ORDER BY match_score DESC, m.title LIMIT 10;";


    public DbUserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<User> findByID(UUID id) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1, id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find user by ID "+e.getMessage());
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
            throw new DatabaseConnectionException("Could not find user by username "+e.getMessage());
        }
    }

    // to reset password
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
            throw new DatabaseConnectionException("Could not find user by email "+e.getMessage());
        }
    }

    @Override
    public List<User> userList() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_USERS)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                List<User> userList = new ArrayList<>();

                while (rs.next()) {
                    userList.add(setUser(rs));
                }

                return userList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show userlist "+e.getMessage());
        }
    }

    @Override
    public Optional<UserProfile> profile(User user) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement preCountMedia = conn.prepareStatement(COUNT_CREATED_MEDIA);
                PreparedStatement preCountFavs = conn.prepareStatement(COUNT_FAVS);
                PreparedStatement preCountLikes = conn.prepareStatement(COUNT_LIKES);
                PreparedStatement preCountRatings = conn.prepareStatement(COUNT_RATINGS);
                PreparedStatement preFavGenres = conn.prepareStatement(FAV_GENRES)
        ) {
            UserProfile userProfile = new UserProfile();
            preCountMedia.setObject(1, user.getId());
            preCountFavs.setObject(1, user.getId());
            preCountLikes.setObject(1, user.getId());
            preCountRatings.setObject(1, user.getId());
            preFavGenres.setObject(1, user.getId());

            // created Media
            try (ResultSet rs = preCountMedia.executeQuery()) {
                if (rs.next()) {
                    userProfile.setCountCreatedMedia(rs.getInt("media_count"));
                }
            }

            // favs
            try (ResultSet rs = preCountFavs.executeQuery()) {
                if (rs.next()) {
                    userProfile.setCountFavs(rs.getInt("favs_count"));
                }
            }

            // likes
            try (ResultSet rs = preCountLikes.executeQuery()) {
                if (rs.next()) {
                    userProfile.setCountLikes(rs.getInt("likes_count"));
                }
            }

            // ratings
            try (ResultSet rs = preCountRatings.executeQuery()) {
                if (rs.next()) {
                    userProfile.setCountRatings(rs.getInt("ratings_count"));
                }
            }

            // fav genres
            try (ResultSet rs = preFavGenres.executeQuery()) {
                List<String> favGenres = new ArrayList<>();
                while (rs.next()) {
                    favGenres.add(rs.getString("genres"));
                }
                userProfile.setFavGenre(favGenres);
            }

            return Optional.of(userProfile);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not set up profile "+e.getMessage());
        }
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
            throw new DatabaseConnectionException("Could not update user "+e.getMessage());
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
           throw new DatabaseConnectionException("Could not delete user "+e.getMessage());
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
            throw new DatabaseConnectionException("Could not save user "+e.getMessage());
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

    @Override
    public List<SQLRecommendationDto> recommendations(User user) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(RECOMMENDATIONS)
        ) {
            prestmt.setObject(1, user.getId());
            prestmt.setObject(2, user.getId());

            try (ResultSet rs = prestmt.executeQuery()) {
                List<SQLRecommendationDto> recomms = new ArrayList<>();
                while (rs.next()) {
                    recomms.add(setRecomm(rs));
                }
                return recomms;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not get recommendations "+e.getMessage());
        }
    }

    @Override
    public List<LeaderboardEntry> leaderboard() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(LEADERBOARD)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                List<LeaderboardEntry> leaderboard = new ArrayList<>();
                while (rs.next()) {
                    leaderboard.add(setEntry(rs));
                }
                return leaderboard;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not get leaderboard "+e.getMessage());
        }
    }

    private UserLoggedInDto rsToLoggedIn(ResultSet rs) throws SQLException {
        try {
            UserLoggedInDto userUpdated = new UserLoggedInDto();
            userUpdated.setUsername(rs.getString("username"));
            userUpdated.setId(rs.getObject("uid", UUID.class));
            return userUpdated;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up logged in user "+e.getMessage());
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
            throw new SQLToObjectException("Can not set up user "+e.getMessage());
        }
    }

    private LeaderboardEntry setEntry(ResultSet rs) throws SQLException {
        try {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setUsername(rs.getString("username"));
            entry.setPoints(rs.getInt("ratings_count"));
            return entry;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up leaderboard entry "+e.getMessage());
        }
    }

    private SQLRecommendationDto setRecomm(ResultSet rs) throws SQLException {
        try {
            SQLRecommendationDto recomm = new SQLRecommendationDto();
            recomm.setMediaId(rs.getObject("mid", UUID.class));
            recomm.setTitle(rs.getString("title"));
            String type = rs.getString("media_type");
            recomm.setMediaType(setType(type));
            recomm.setAgeRestriction(rs.getInt("age_restriction"));
            Array genreArray = rs.getArray("genres");
            recomm.setGenres(List.of((String[]) genreArray.getArray()));
            recomm.setScore(rs.getInt("match_score"));
            return recomm;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up recommendation "+e.getMessage());
        }
    }

    private MediaType setType(String type) throws SQLException {
        return switch (type) {
            case "movie" -> MediaType.MOVIE;
            case "series" -> MediaType.SERIES;
            case "game" -> MediaType.GAME;
            default -> throw new SQLToObjectException("Can not set up mediaType");
        };
    }
}
