package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.dto.sql.SQLLikeDto;
import at.technikum.application.dto.sql.SQLRatingDto;
import at.technikum.application.exception.DatabaseConnectionException;
import at.technikum.application.exception.ObjectToSQLException;
import at.technikum.application.exception.SQLToObjectException;
import at.technikum.application.model.Like;
import at.technikum.application.model.Media;
import at.technikum.application.model.Rating;
import at.technikum.application.model.User;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
            = "INSERT INTO ratings (rid, user_id, media_id, stars, comment, timestamp, visibility) VALUES "
                    +"(?, ?, ?, ?, ?, ?, ?) RETURNING *";

    private static final String UPDATE_RATING
            ="UPDATE ratings SET stars = ?, comment = ? WHERE rid = ? RETURNING *";

    private static final String LIKE
            = "INSERT INTO rating_likes (user_id, rating_id) VALUES (?, ?) RETURNING *";

    private static final String CONFIRM
            = "UPDATE ratings SET visibility = TRUE WHERE rid = ? RETURNING *";

    private static final String DELETE_RATING
            = "DELETE FROM ratings WHERE rid = ? RETURNING *";

    private static final String SELECT_ALL_RATINGS
            = "SELECT * FROM ratings";

    public DbRatingRepository(ConnectionPool connectionPool, UserRepository userRepository,
                              MediaRepository mediaRepository) {
        this.connectionPool = connectionPool;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Optional<SQLRatingDto> findByID(UUID id) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1, id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setRatingDto(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find rating");
        }
    }

    @Override
    public Optional<Rating> findByIDRating(UUID id) {
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
    public Optional<SQLRatingDto> save(Rating rating) {
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
            prestmt.setString(5, rating.getComment());
            Instant ts = rating.getTimestamp();
            OffsetDateTime odt = ts.atOffset(ZoneOffset.UTC);
            prestmt.setObject(6, odt, Types.TIMESTAMP_WITH_TIMEZONE);
            prestmt.setBoolean(7, rating.isVisibility());

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setRatingDto(rs));
            }
        } catch (SQLException e) {
            throw new ObjectToSQLException("Could not save rating"+e.getMessage());
        }
    }

    @Override
    public Optional<SQLRatingDto> update(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(UPDATE_RATING)
        ) {
            prestmt.setObject(1, rating.getStars());
            prestmt.setObject(2, rating.getComment());
            prestmt.setObject(3, rating.getId());

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setRatingDto(rs));
            }
        } catch (SQLException e) {
            throw new ObjectToSQLException("Could not update rating");
        }
    }

    @Override
    public Optional<SQLLikeDto> like(Like like) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(LIKE)
        ) {
            UUID uid = like.getUser().getId();
            UUID rid = like.getRating().getId();
            prestmt.setObject(1, uid);
            prestmt.setObject(2, rid);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setLike(rs));
            }
        }catch (SQLException e) {
            throw new ObjectToSQLException("Could not add favorite");
        }
    }

    @Override
    public Optional<SQLRatingDto> confirm(Rating rating) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(CONFIRM)
        ) {

            prestmt.setObject(1, rating.getId());

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setRatingDto(rs));
            }
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
    public List<SQLRatingDto> findAll() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_RATINGS)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return List.of();
                }

                List<SQLRatingDto> ratingList = new ArrayList<>();

                while (rs.next()) {
                    ratingList.add(setRatingDto(rs));
                }

                return ratingList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show ratingList");
        }
    }

    private SQLRatingDto setRatingDto(ResultSet rs) throws SQLException {
        try {
            SQLRatingDto rating = new SQLRatingDto();
            rating.setId(rs.getObject("rid", UUID.class));
            rating.setUserId(rs.getObject("user_id", UUID.class));
            rating.setMediaId(rs.getObject("media_id", UUID.class));
            rating.setStars(rs.getInt("stars"));
            rating.setComment(rs.getString("comment"));
            OffsetDateTime odt = rs.getObject("timestamp", OffsetDateTime.class);
            rating.setCreatedAt(odt.toInstant());
            rating.setVisibility(rs.getBoolean("visibility"));
            return rating;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up ratingDto");
        }
    }

    private Rating setRating(ResultSet rs) throws SQLException {
        try {
            Rating rating = new Rating();
            rating.setId(rs.getObject("rid", UUID.class));
            UUID userId = rs.getObject("user_id", UUID.class);
            rating.setCreator(this.userRepository.findByID(userId).get());
            UUID mediaId = rs.getObject("media_id", UUID.class);
            rating.setRatedMedia(this.mediaRepository.findByIdMedia(mediaId).get());
            rating.setStars(rs.getInt("stars"));
            rating.setComment(rs.getString("comment"));
            OffsetDateTime odt = rs.getObject("timestamp", OffsetDateTime.class);
            rating.setTimestamp(odt.toInstant());
            rating.setVisibility(rs.getBoolean("visibility"));
            return rating;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up rating");
        }
    }

    private SQLLikeDto setLike(ResultSet rs) throws SQLException {
        try {
            SQLLikeDto like = new SQLLikeDto();
            like.setRatingId(rs.getObject("rating_id", UUID.class));
            like.setUserId(rs.getObject("user_id", UUID.class));
            return like;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up like");
        }
    }
}
