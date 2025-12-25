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
                       genres          TEXT[]     NOT NULL DEFAULT '{}'
);

CREATE TABLE ratings (
                         rid        UUID        PRIMARY KEY,
                         user_id    UUID        NOT NULL REFERENCES users(uid),
                         media_id   UUID        NOT NULL REFERENCES media(mid) ON DELETE CASCADE,
                         stars      SMALLINT    NOT NULL,
                         comment    TEXT,
                         timestamp  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         visibility BOOLEAN     NOT NULL DEFAULT FALSE,
                         CONSTRAINT uq_ratings_user_media UNIQUE (user_id, media_id)
);

CREATE TABLE favorite (
                           fid        UUID       PRIMARY KEY,
                           user_id    UUID       NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
                           media_id   UUID       NOT NULL REFERENCES media(mid) ON DELETE CASCADE,
                           CONSTRAINT uq_favorites_user_media UNIQUE (user_id, media_id)
);

CREATE TABLE rating_likes (
                              lid        UUID       PRIMARY KEY,
                              user_id    UUID       NOT NULL REFERENCES users(uid),
                              rating_id  UUID       NOT NULL REFERENCES ratings(rid) ON DELETE CASCADE,
                              CONSTRAINT uq_rating_likes_user_rating UNIQUE (user_id, rating_id)
);
