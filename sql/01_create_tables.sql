CREATE TABLE subscriptions
(
	subscription_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	price NUMERIC(8,2) NOT NULL,
	duration_days INTEGER NOT NULL,
	CONSTRAINT chk_subscription_price
		CHECK (price >= 0),
	CONSTRAINT chk_subscription_duration
		CHECK (duration_days > 0),
	CONSTRAINT uq_subscription_name
		UNIQUE(name)
);

CREATE TABLE users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    subscription_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_users_subscription
        FOREIGN KEY (subscription_id)
        REFERENCES subscriptions(subscription_id)
		ON DELETE SET NULL
);

CREATE TABLE labels
(
    label_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    foundation_year INTEGER,
    CONSTRAINT uq_labels_name
        UNIQUE(name),
    CONSTRAINT chk_labels_foundation_year
        CHECK (
            foundation_year IS NULL OR
            foundation_year BETWEEN 1900 AND EXTRACT(YEAR FROM CURRENT_DATE)
        )
);

CREATE TABLE artists
(
    artist_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    country VARCHAR(100),
    label_id BIGINT,
    CONSTRAINT fk_artists_label
        FOREIGN KEY (label_id)
        REFERENCES labels(label_id)
        ON DELETE SET NULL
);

CREATE TABLE genres
(
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE albums
(
    album_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    release_date DATE,
    release_type VARCHAR(20) NOT NULL,
    artist_id BIGINT NOT NULL,
    CONSTRAINT chk_album_release_type
        CHECK (release_type IN ('album', 'ep', 'single')),
    CONSTRAINT fk_albums_artist
        FOREIGN KEY (artist_id)
        REFERENCES artists(artist_id)
        ON DELETE RESTRICT
);

CREATE TABLE tracks
(
    track_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    duration_seconds INTEGER NOT NULL,
    play_count BIGINT NOT NULL DEFAULT 0,
    album_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    CONSTRAINT chk_track_duration
        CHECK (duration_seconds > 0),
    CONSTRAINT chk_track_play_count
        CHECK (play_count >= 0),
    CONSTRAINT fk_tracks_album
        FOREIGN KEY (album_id)
        REFERENCES albums(album_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_tracks_genre
        FOREIGN KEY (genre_id)
        REFERENCES genres(genre_id)
        ON DELETE RESTRICT
);

CREATE TABLE reviews
(
    review_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rating INTEGER NOT NULL,
    review_date TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id BIGINT NOT NULL,
    album_id BIGINT NOT NULL,
    CONSTRAINT chk_review_rating
        CHECK (rating BETWEEN 1 AND 10),
    CONSTRAINT uq_reviews_user_album
        UNIQUE(user_id, album_id),
    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_reviews_album
        FOREIGN KEY (album_id)
        REFERENCES albums(album_id)
        ON DELETE CASCADE
);

CREATE TABLE playlists
(
    playlist_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_playlists_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE playlist_tracks
(
    playlist_id BIGINT NOT NULL,
    track_id BIGINT NOT NULL,
    added_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_playlist_tracks
        PRIMARY KEY (playlist_id, track_id),
    CONSTRAINT fk_playlist_tracks_playlist
        FOREIGN KEY (playlist_id)
        REFERENCES playlists(playlist_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_playlist_tracks_track
        FOREIGN KEY (track_id)
        REFERENCES tracks(track_id)
        ON DELETE CASCADE
);

CREATE TABLE collections
(
    collection_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE collection_tracks
(
    collection_id BIGINT NOT NULL,
    track_id BIGINT NOT NULL,
    CONSTRAINT pk_collection_tracks
        PRIMARY KEY (collection_id, track_id),
    CONSTRAINT fk_collection_tracks_collection
        FOREIGN KEY (collection_id)
        REFERENCES collections(collection_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_collection_tracks_track
        FOREIGN KEY (track_id)
        REFERENCES tracks(track_id)
        ON DELETE CASCADE
);

