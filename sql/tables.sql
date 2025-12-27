CREATE TABLE IF NOT EXISTS users (
                       uid           UUID        PRIMARY KEY,
                       username      TEXT        NOT NULL UNIQUE,
                       email         TEXT        NOT NULL UNIQUE,
                       password      TEXT        NOT NULL,
                       usertype      TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS media (
                       mid             UUID       PRIMARY KEY,
                       owner_id        UUID       NOT NULL REFERENCES users(uid),
                       title           TEXT       NOT NULL,
                       description     TEXT,
                       media_type      TEXT       NOT NULL,
                       release_year    SMALLINT,
                       age_restriction SMALLINT,
                       genres          TEXT[]     NOT NULL DEFAULT '{}',
                        CONSTRAINT uq_media_title_media_type UNIQUE (title, media_type)
);

CREATE TABLE IF NOT EXISTS ratings (
                         rid        UUID        PRIMARY KEY,
                         user_id    UUID        NOT NULL REFERENCES users(uid),
                         media_id   UUID        NOT NULL REFERENCES media(mid) ON DELETE CASCADE,
                         stars      SMALLINT    NOT NULL,
                         comment    TEXT,
                         timestamp  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         visibility BOOLEAN     NOT NULL DEFAULT FALSE,
                         CONSTRAINT uq_ratings_user_media UNIQUE (user_id, media_id)
);

CREATE TABLE IF NOT EXISTS favorite (
                           user_id    UUID       NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
                           media_id   UUID       NOT NULL REFERENCES media(mid) ON DELETE CASCADE,
                           PRIMARY KEY (user_id, media_id)
);

CREATE TABLE IF NOT EXISTS rating_likes (
                              user_id    UUID       NOT NULL REFERENCES users(uid),
                              rating_id  UUID       NOT NULL REFERENCES ratings(rid) ON DELETE CASCADE,
                              PRIMARY KEY (user_id, rating_id)
);

CREATE INDEX idx_favorite_media ON favorite(media_id);
CREATE INDEX idx_rating_likes_rating ON rating_likes(rating_id);