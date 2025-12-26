package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.exception.DatabaseConnectionException;
import at.technikum.application.exception.ObjectToSQLException;
import at.technikum.application.exception.SQLToRatingException;
import at.technikum.application.exception.SQLToUserException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DbRatingRepository implements RatingRepository{
    private final ConnectionPool connectionPool;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    private static final String SELECT_BY_ID
            = "SELECT * FROM ratings WHERE rid = ?";

    private static final String SAVE
            = "INSERT INTO ratings (rid, user_id, media_id, stars, comment, timestamp, visibility) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_RATING
            ="UPDATE ratings SET stars = ?, comment = ?, timestamp = ?, visibility = ? WHERE rid = ?";

    private static final String LIKE
            = "INSERT INTO rating_likes (user_id, rating_id) VALUES (?, ?)";

    private static final String CONFIRM
            = "UPDATE ratings SET visibility = TRUE WHERE rid = ?";

    private static final String DELETE_RATING
            = "DELETE FROM ratings WHERE rid = ?";

    private static final String SELECT_ALL_RATINGS
            = "SELECT * FROM ratings";

    public DbRatingRepository(ConnectionPool connectionPool, UserRepository userRepository,
                              MediaRepository mediaRepository) {
        this.connectionPool = connectionPool;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Optional<Rating> findByID(UUID id) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1, id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setRating(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find rating");
        }
    }

    @Override
    public Optional<Rating> save(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SAVE)
        ) {
            prestmt.setObject(1, rating.getId());
            User creator = rating.getCreator();
            prestmt.setObject(2, creator.getId());
            Media media = rating.getRatedMedia();
            prestmt.setObject(3, media.getId());
            prestmt.setObject(4, rating.getStars());
            prestmt.setObject(5, rating.getComment());
            prestmt.setObject(6, rating.getTimestamp());
            prestmt.setBoolean(7, rating.isVisibility());
            prestmt.executeUpdate();

            return Optional.of(rating);
        } catch (SQLException e) {
            throw new ObjectToSQLException("Could not save rating");
        }
    }

    @Override
    public Optional<Rating> update(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(UPDATE_RATING)
        ) {
            prestmt.setObject(1, rating.getStars());
            prestmt.setObject(2, rating.getComment());
            prestmt.setObject(3, rating.getTimestamp());
            prestmt.setBoolean(4, rating.isVisibility());
            prestmt.setObject(5, rating.getId());
            prestmt.executeUpdate();

            return Optional.of(rating);
        } catch (SQLException e) {
            throw new ObjectToSQLException("Could not update rating");
        }
    }

    @Override
    public Optional<Like> like(Like like) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(LIKE)
        ) {
            UUID uid = like.getUser().getId();
            UUID rid = like.getRating().getId();
            prestmt.setObject(1, uid);
            prestmt.setObject(2, rid);
            prestmt.executeUpdate();

            return Optional.of(like);
        }catch (SQLException e) {
            throw new ObjectToSQLException("Could not add favorite");
        }
    }

    @Override
    public Optional<Rating> confirm(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(CONFIRM)
        ) {

            prestmt.setObject(1, rating.getId());
            prestmt.executeUpdate();

            return Optional.of(rating);
        }catch (SQLException e) {
            throw new ObjectToSQLException("Could not confirm rating");
        }
    }

    @Override
    public Optional<String> delete(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(DELETE_RATING)
        ) {
            prestmt.setObject(1,rating.getId());

            try (ResultSet rs = prestmt.executeQuery()){
                if (!rs.next()) {
                    return Optional.empty();
                }
                Media media = rating.getRatedMedia();

                return Optional.of(media.getTitle());
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not delete media");
        }
    }

    @Override
    public List<Rating> findAll() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_RATINGS)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return List.of();
                }

                List<Rating> ratingList = new ArrayList<>();

                while (rs.next()) {
                    ratingList.add(setRating(rs));
                }

                return ratingList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show ratingList");
        }
    }

    private Rating setRating(ResultSet rs) throws SQLException {
        try {
            Rating rating = new Rating();
            rating.setId(rs.getObject("rid", UUID.class));
            UUID creatorId = rs.getObject("user_id", UUID.class);
            rating.setCreator(this.userRepository.findByID(creatorId).get());
            UUID mediaId = rs.getObject("media_id", UUID.class);
            rating.setRatedMedia(this.mediaRepository.findById(mediaId).get());
            rating.setStars(rs.getInt("stars"));
            rating.setComment(rs.getString("comment"));
            rating.setTimestamp(rs.getObject("timestamp", Instant.class));
            rating.setVisibility(rs.getBoolean("visibility"));
            return rating;
        } catch (SQLException e) {
            throw new SQLToRatingException("Can not set up rating");
        }
    }
}
