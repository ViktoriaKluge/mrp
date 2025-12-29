package at.technikum.application.repository;

import at.technikum.application.common.ConnectionPool;
import at.technikum.application.dto.sql.SQLFavoriteDto;
import at.technikum.application.dto.sql.SQLMediaDto;
import at.technikum.application.enums.MediaType;
import at.technikum.application.exception.*;
import at.technikum.application.model.Favorite;
import at.technikum.application.model.Media;
import at.technikum.application.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DbMediaRespository implements MediaRepository {
    private final ConnectionPool connectionPool;
    private final UserRepository userRepository;

    private static final String SELECT_BY_ID
            = "SELECT * FROM media WHERE mid = ?";

    private static final String SELECT_ALL_MEDIA
            = "SELECT * FROM media";

    private static final String SAVE
            = "INSERT INTO media (mid, owner_id, title, description, media_type, release_year, age_restriction, genres)"
                    +" VALUES (?,?,?,?,?,?,?,?) RETURNING *";

    private static final String DELETE_MEDIA
            = "DELETE FROM media WHERE mid = ? RETURNING *";

    private static final String UPDATE_MEDIA
            = "UPDATE media SET title = ?, description = ?, media_type = ?, release_year = ?, age_restriction = ?, "
                    +"genres = ? WHERE mid = ? RETURNING *";

    private static final String ADD_FAVORITE
            = "INSERT INTO favorite (user_id, media_id) VALUES (?, ?) RETURNING *";

    private static final String DELETE_FAVORITE
            = "DELETE FROM favorite WHERE user_id = ? AND media_id = ? RETURNING *";

    private static final String SELECT_ALL_FAVS_BY_UID
            = "SELECT * FROM favorite WHERE user_id = ?";

    public DbMediaRespository(ConnectionPool connectionPool, UserRepository userRepository) {
        this.connectionPool = connectionPool;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<SQLMediaDto> findById(UUID id) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1, id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setMediaDto(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find media");
        }
    }

    public Optional<Media> findByIdMedia(UUID id) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement prestmt = conn.prepareStatement(SELECT_BY_ID)
        ) {
            prestmt.setObject(1, id);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setMedia(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not find media");
        }
    }

    @Override
    public List<SQLMediaDto> mediaList() {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_MEDIA)
        ) {
            try (ResultSet rs = prestmt.executeQuery()) {
                List<SQLMediaDto> mediaList = new ArrayList<>();

                while (rs.next()) {
                    mediaList.add(setMediaDto(rs));
                }

                return mediaList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show medialist");
        }
    }

    @Override
    public Optional<SQLMediaDto> save(Media media) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SAVE)
        ) {
            prestmt.setObject(1, media.getId());
            User creator = media.getCreator();
            prestmt.setObject(2, creator.getId());
            prestmt.setString(3, media.getTitle());
            prestmt.setString(4, media.getDescription());
            prestmt.setString(5, media.getMediaType().getType());
            prestmt.setInt(6, media.getReleaseYear());
            prestmt.setInt(7, media.getAgeRestriction());
            List<String> genres = media.getGenre();
            Array genresArray = conn.createArrayOf("text", genres.toArray());
            prestmt.setArray(8, genresArray);

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setMediaDto(rs));
            }
        } catch (SQLException e) {
            throw new ObjectToSQLException("Could not save media"+e.getMessage());
        }
    }

    @Override
    public Optional<String> delete(Media media) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(DELETE_MEDIA)
        ) {
            prestmt.setObject(1,media.getId());

            try (ResultSet rs = prestmt.executeQuery()){
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(rs.getString("title"));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Could not delete media");
        }
    }

    @Override
    public Optional<SQLMediaDto> update(Media media) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(UPDATE_MEDIA)
        ) {

            prestmt.setString(1, media.getTitle());
            prestmt.setString(2, media.getDescription());
            prestmt.setString(3, media.getMediaType().getType());
            prestmt.setInt(4, media.getReleaseYear());
            prestmt.setInt(5, media.getAgeRestriction());
            List<String> genres = media.getGenre();
            Array genresArray = conn.createArrayOf("text", genres.toArray());
            prestmt.setArray(6, genresArray);
            prestmt.setObject(7, media.getId());

            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setMediaDto(rs));
            }
        } catch (SQLException e) {
            throw new SQLToObjectException("Could not update media");
        }

    }

    @Override
    public Optional<SQLFavoriteDto> addFavorite(Favorite favorite) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(ADD_FAVORITE)
        ) {
            UUID uid = favorite.getUser().getId();
            UUID mid = favorite.getMedia().getId();
            prestmt.setObject(1, uid);
            prestmt.setObject(2, mid);
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(setFavorite(rs));
            }
        }catch (SQLException e) {
            throw new SQLToObjectException("Could not add favorite");
        }
    }

    @Override
    public Optional<String> deleteFavorite(Favorite favorite) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(DELETE_FAVORITE)
        ) {
            UUID uid = favorite.getUser().getId();
            UUID mid = favorite.getMedia().getId();
            prestmt.setObject(1, uid);
            prestmt.setObject(2, mid);
            try (ResultSet rs = prestmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                String mediaTitle = favorite.getMedia().getTitle();
                return Optional.of(mediaTitle);
            }
        }catch (SQLException e) {
            throw new SQLToObjectException("Could not delete favorite");
        }
    }

    @Override
    public List<SQLFavoriteDto> findFavsByUserId(User user) {
        try (
                Connection conn = connectionPool.getConnection();
                PreparedStatement prestmt = conn.prepareStatement(SELECT_ALL_FAVS_BY_UID)
        ) {
            prestmt.setObject(1, user.getId());
            try (ResultSet rs = prestmt.executeQuery()) {
                List<SQLFavoriteDto> favsList = new ArrayList<>();

                while (rs.next()) {
                    favsList.add(setFavorite(rs));
                }

                return favsList;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Can not show favorite list by user");
        }
    }

    private SQLMediaDto setMediaDto(ResultSet rs) throws SQLException {
        try {
            SQLMediaDto media = new SQLMediaDto();
            media.setId(rs.getObject("mid", UUID.class));
            media.setTitle(rs.getString("title"));
            media.setDescription(rs.getString("description"));
            media.setCreatorId(rs.getObject("owner_id", UUID.class));
            String type = rs.getString("media_type");
            media.setMediaType(setType(type));
            media.setReleaseYear(rs.getObject("release_year", Integer.class));
            media.setAgeRestriction(rs.getObject("age_restriction", Integer.class));
            Array sqlGenres = rs.getArray("genres");
            media.setGenres(List.of((String[]) sqlGenres.getArray()));
            return media;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up mediaDto"+ e.getMessage());
        }
    }

    private Media setMedia(ResultSet rs) throws SQLException {
        try {
            Media media = new Media();
            media.setId(rs.getObject("mid", UUID.class));
            media.setTitle(rs.getString("title"));
            media.setDescription(rs.getString("description"));
            UUID creatorId = rs.getObject("owner_id", UUID.class);
            media.setCreator(this.userRepository.findByID(creatorId).get());
            String type = rs.getString("media_type");
            media.setMediaType(setType(type));
            media.setReleaseYear(rs.getObject("release_year", Integer.class));
            media.setAgeRestriction(rs.getObject("age_restriction", Integer.class));
            Array sqlGenres = rs.getArray("genres");
            media.setGenre(List.of((String[]) sqlGenres.getArray()));
            return media;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up media");
        }
    }
            // favs leer bei users7.../favs
    private SQLFavoriteDto setFavorite(ResultSet rs) throws SQLException {
        try {
            SQLFavoriteDto sqlFav = new SQLFavoriteDto();
            sqlFav.setUserId(rs.getObject("user_id", UUID.class));
            sqlFav.setMediaId(rs.getObject("media_id", UUID.class));
            return sqlFav;
        } catch (SQLException e) {
            throw new SQLToObjectException("Can not set up favorite");
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
