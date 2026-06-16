

-- =====================================================================
-- FUNCTIONS (чтение)
-- =====================================================================

CREATE OR REPLACE FUNCTION fn_get_artists()
RETURNS TABLE(artist_id BIGINT, artist_name VARCHAR(100), country VARCHAR(100), label_name VARCHAR(100))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT a.artist_id, a.name, a.country, l.name
	FROM artists a LEFT JOIN labels l ON a.label_id = l.label_id
	ORDER BY a.name;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_artist_tracks(p_artist_id BIGINT)
RETURNS TABLE(track_id BIGINT, track_title VARCHAR(150), album_title VARCHAR(150), duration_seconds INTEGER, play_count BIGINT, genre_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.track_id, t.title, al.title, t.duration_seconds, t.play_count, g.name
	FROM tracks t
	JOIN albums al ON t.album_id = al.album_id
	JOIN genres g ON t.genre_id = g.genre_id
	WHERE al.artist_id = p_artist_id
	ORDER BY t.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_subscriptions()
RETURNS TABLE(subscription_id BIGINT, name VARCHAR(50), price DOUBLE PRECISION, duration_days INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY SELECT s.subscription_id, s.name, s.price::DOUBLE PRECISION, s.duration_days FROM subscriptions s ORDER BY s.subscription_id;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_labels()
RETURNS TABLE(label_id BIGINT, name VARCHAR(100), foundation_year INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY SELECT l.label_id, l.name, l.foundation_year FROM labels l ORDER BY l.name;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_dashboard_stats()
RETURNS TABLE(user_count BIGINT, track_count BIGINT, album_count BIGINT, subscription_count BIGINT, artist_count BIGINT, genre_count BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT
		(SELECT COUNT(*)::BIGINT FROM users),
		(SELECT COUNT(*)::BIGINT FROM tracks),
		(SELECT COUNT(*)::BIGINT FROM albums),
		(SELECT COUNT(*)::BIGINT FROM subscriptions),
		(SELECT COUNT(*)::BIGINT FROM artists),
		(SELECT COUNT(*)::BIGINT FROM genres);
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_users()
RETURNS TABLE(user_id BIGINT, username VARCHAR(50), email VARCHAR(255), subscription_name VARCHAR(50), created_at TIMESTAMP)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT u.user_id, u.username, u.email, s.name, u.created_at
	FROM users u LEFT JOIN subscriptions s ON u.subscription_id = s.subscription_id
	ORDER BY u.username;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_albums()
RETURNS TABLE(album_id BIGINT, album_title VARCHAR(150), artist_name VARCHAR(100), release_date DATE, release_type VARCHAR(20), avg_rating DOUBLE PRECISION, review_count BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT al.album_id, al.title, a.name, al.release_date, al.release_type,
		COALESCE(ROUND(AVG(r.rating)::NUMERIC, 1), 0)::DOUBLE PRECISION, COUNT(r.review_id)
	FROM albums al
	JOIN artists a ON al.artist_id = a.artist_id
	LEFT JOIN reviews r ON al.album_id = r.album_id
	GROUP BY al.album_id, al.title, a.name, al.release_date, al.release_type
	ORDER BY al.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_album_tracks(p_album_id BIGINT)
RETURNS TABLE(track_id BIGINT, track_title VARCHAR(150), duration_seconds INTEGER, play_count BIGINT, genre_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.track_id, t.title, t.duration_seconds, t.play_count, g.name
	FROM tracks t JOIN genres g ON t.genre_id = g.genre_id
	WHERE t.album_id = p_album_id ORDER BY t.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_album_reviews(p_album_id BIGINT)
RETURNS TABLE(review_id BIGINT, username VARCHAR(50), rating INTEGER, review_date TIMESTAMP)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT r.review_id, u.username, r.rating, r.review_date
	FROM reviews r JOIN users u ON r.user_id = u.user_id
	WHERE r.album_id = p_album_id ORDER BY r.review_date DESC;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_tracks()
RETURNS TABLE(track_id BIGINT, track_title VARCHAR(150), artist_name VARCHAR(100), album_title VARCHAR(150), duration_seconds INTEGER, play_count BIGINT, genre_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.track_id, t.title, a.name, al.title, t.duration_seconds, t.play_count, g.name
	FROM tracks t
	JOIN albums al ON t.album_id = al.album_id
	JOIN artists a ON al.artist_id = a.artist_id
	JOIN genres g ON t.genre_id = g.genre_id
	ORDER BY t.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_search_tracks(p_query TEXT)
RETURNS TABLE(track_id BIGINT, track_title VARCHAR(150), artist_name VARCHAR(100), album_title VARCHAR(150), duration_seconds INTEGER, play_count BIGINT, genre_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.track_id, t.title, a.name, al.title, t.duration_seconds, t.play_count, g.name
	FROM tracks t
	JOIN albums al ON t.album_id = al.album_id
	JOIN artists a ON al.artist_id = a.artist_id
	JOIN genres g ON t.genre_id = g.genre_id
	WHERE LOWER(t.title) LIKE '%' || LOWER(p_query) || '%'
	   OR LOWER(al.title) LIKE '%' || LOWER(p_query) || '%'
	   OR LOWER(a.name) LIKE '%' || LOWER(p_query) || '%'
	ORDER BY t.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_genres()
RETURNS TABLE(genre_id BIGINT, genre_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY SELECT g.genre_id, g.name FROM genres g ORDER BY g.name;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_reviews()
RETURNS TABLE(review_id BIGINT, username VARCHAR(50), album_title VARCHAR(150), rating INTEGER, review_date TIMESTAMP)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT r.review_id, u.username, al.title, r.rating, r.review_date
	FROM reviews r
	JOIN users u ON r.user_id = u.user_id
	JOIN albums al ON r.album_id = al.album_id
	ORDER BY r.review_date DESC;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_playlists()
RETURNS TABLE(playlist_id BIGINT, playlist_name VARCHAR(100), username VARCHAR(50), created_at TIMESTAMP, track_count BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT p.playlist_id, p.name, u.username, p.created_at, COUNT(pt.track_id)
	FROM playlists p
	JOIN users u ON p.user_id = u.user_id
	LEFT JOIN playlist_tracks pt ON p.playlist_id = pt.playlist_id
	GROUP BY p.playlist_id, p.name, u.username, p.created_at
	ORDER BY p.name;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_playlist_detail(p_playlist_id BIGINT)
RETURNS TABLE(track_title VARCHAR(150), artist_name VARCHAR(100), duration_seconds INTEGER, added_at TIMESTAMP)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.title, a.name, t.duration_seconds, pt.added_at
	FROM playlist_tracks pt
	JOIN tracks t ON pt.track_id = t.track_id
	JOIN albums al ON t.album_id = al.album_id
	JOIN artists a ON al.artist_id = a.artist_id
	WHERE pt.playlist_id = p_playlist_id
	ORDER BY pt.added_at;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_collections()
RETURNS TABLE(collection_id BIGINT, title VARCHAR(100), description TEXT, track_count BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT c.collection_id, c.title, c.description, COUNT(ct.track_id)
	FROM collections c LEFT JOIN collection_tracks ct ON c.collection_id = ct.collection_id
	GROUP BY c.collection_id, c.title, c.description ORDER BY c.title;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_collection_detail(p_collection_id BIGINT)
RETURNS TABLE(track_title VARCHAR(150), artist_name VARCHAR(100), album_title VARCHAR(150), genre_name VARCHAR(50), duration_seconds INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	RETURN QUERY
	SELECT t.title, a.name, al.title, g.name, t.duration_seconds
	FROM collection_tracks ct
	JOIN tracks t ON ct.track_id = t.track_id
	JOIN albums al ON t.album_id = al.album_id
	JOIN artists a ON al.artist_id = a.artist_id
	JOIN genres g ON t.genre_id = g.genre_id
	WHERE ct.collection_id = p_collection_id ORDER BY t.title;
END;
$$;


-- =====================================================================
-- PROCEDURES (запись)
-- =====================================================================

CREATE OR REPLACE PROCEDURE sp_add_artist(p_name VARCHAR(100), p_country VARCHAR(100), p_label_name VARCHAR(100))
LANGUAGE plpgsql AS $$
DECLARE v_label_id BIGINT;
BEGIN
	SELECT label_id INTO v_label_id FROM labels WHERE LOWER(name) = LOWER(p_label_name);
	IF v_label_id IS NULL THEN RAISE EXCEPTION 'Лейбл "%" не найден.', p_label_name; END IF;
	INSERT INTO artists(name, country, label_id) VALUES (p_name, p_country, v_label_id);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_artist(p_id BIGINT, p_name VARCHAR(100), p_country VARCHAR(100), p_label_name VARCHAR(100))
LANGUAGE plpgsql AS $$
DECLARE v_label_id BIGINT;
BEGIN
	SELECT label_id INTO v_label_id FROM labels WHERE LOWER(name) = LOWER(p_label_name);
	IF v_label_id IS NULL THEN RAISE EXCEPTION 'Лейбл "%" не найден.', p_label_name; END IF;
	UPDATE artists SET name = p_name, country = p_country, label_id = v_label_id WHERE artist_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_artist(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM albums WHERE artist_id = p_id) THEN
		RAISE EXCEPTION 'Невозможно удалить артиста — у него есть альбомы.';
	END IF;
	DELETE FROM artists WHERE artist_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_subscription(p_name VARCHAR(50), p_price DOUBLE PRECISION, p_duration_days INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	INSERT INTO subscriptions(name, price, duration_days) VALUES (p_name, p_price, p_duration_days);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_subscription(p_id BIGINT, p_name VARCHAR(50), p_price DOUBLE PRECISION, p_duration_days INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	UPDATE subscriptions SET name = p_name, price = p_price, duration_days = p_duration_days WHERE subscription_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_subscription(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM users WHERE subscription_id = p_id) THEN
		RAISE EXCEPTION 'Невозможно удалить подписку — она назначена пользователям.';
	END IF;
	DELETE FROM subscriptions WHERE subscription_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_label(p_name VARCHAR(100), p_foundation_year INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM labels WHERE LOWER(name) = LOWER(p_name)) THEN
		RAISE EXCEPTION 'Лейбл "%" уже существует.', p_name;
	END IF;
	INSERT INTO labels(name, foundation_year) VALUES (p_name, p_foundation_year);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_label(p_id BIGINT, p_name VARCHAR(100), p_foundation_year INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
	UPDATE labels SET name = p_name, foundation_year = p_foundation_year WHERE label_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_label(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM artists WHERE label_id = p_id) THEN
		RAISE EXCEPTION 'Невозможно удалить лейбл — к нему привязаны артисты.';
	END IF;
	DELETE FROM labels WHERE label_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_album(p_title VARCHAR(150), p_artist_name VARCHAR(100), p_release_date DATE, p_release_type VARCHAR(20))
LANGUAGE plpgsql AS $$
DECLARE v_artist_id BIGINT;
BEGIN
	SELECT artist_id INTO v_artist_id FROM artists WHERE LOWER(name) = LOWER(p_artist_name);
	IF v_artist_id IS NULL THEN RAISE EXCEPTION 'Исполнитель "%" не найден.', p_artist_name; END IF;
	INSERT INTO albums(title, release_date, release_type, artist_id) VALUES (p_title, p_release_date, p_release_type, v_artist_id);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_album(p_id BIGINT, p_title VARCHAR(150), p_artist_name VARCHAR(100), p_release_date DATE, p_release_type VARCHAR(20))
LANGUAGE plpgsql AS $$
DECLARE v_artist_id BIGINT;
BEGIN
	SELECT artist_id INTO v_artist_id FROM artists WHERE LOWER(name) = LOWER(p_artist_name);
	IF v_artist_id IS NULL THEN RAISE EXCEPTION 'Исполнитель "%" не найден.', p_artist_name; END IF;
	UPDATE albums SET title = p_title, release_date = p_release_date, release_type = p_release_type, artist_id = v_artist_id WHERE album_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_album(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	DELETE FROM albums WHERE album_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_track(p_title VARCHAR(150), p_album_title VARCHAR(150), p_genre_name VARCHAR(50), p_duration_seconds INTEGER)
LANGUAGE plpgsql AS $$
DECLARE v_album_id BIGINT; v_genre_id BIGINT;
BEGIN
	SELECT album_id INTO v_album_id FROM albums WHERE LOWER(title) = LOWER(p_album_title);
	IF v_album_id IS NULL THEN RAISE EXCEPTION 'Альбом "%" не найден.', p_album_title; END IF;
	SELECT genre_id INTO v_genre_id FROM genres WHERE LOWER(name) = LOWER(p_genre_name);
	IF v_genre_id IS NULL THEN RAISE EXCEPTION 'Жанр "%" не найден.', p_genre_name; END IF;
	INSERT INTO tracks(title, duration_seconds, album_id, genre_id) VALUES (p_title, p_duration_seconds, v_album_id, v_genre_id);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_track(p_id BIGINT, p_title VARCHAR(150), p_album_title VARCHAR(150), p_genre_name VARCHAR(50), p_duration_seconds INTEGER)
LANGUAGE plpgsql AS $$
DECLARE v_album_id BIGINT; v_genre_id BIGINT;
BEGIN
	SELECT album_id INTO v_album_id FROM albums WHERE LOWER(title) = LOWER(p_album_title);
	IF v_album_id IS NULL THEN RAISE EXCEPTION 'Альбом "%" не найден.', p_album_title; END IF;
	SELECT genre_id INTO v_genre_id FROM genres WHERE LOWER(name) = LOWER(p_genre_name);
	IF v_genre_id IS NULL THEN RAISE EXCEPTION 'Жанр "%" не найден.', p_genre_name; END IF;
	UPDATE tracks SET title = p_title, album_id = v_album_id, genre_id = v_genre_id, duration_seconds = p_duration_seconds WHERE track_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_track(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	DELETE FROM tracks WHERE track_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_genre(p_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM genres WHERE LOWER(name) = LOWER(p_name)) THEN RAISE EXCEPTION 'Жанр "%" уже существует.', p_name; END IF;
	INSERT INTO genres(name) VALUES (p_name);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_genre(p_id BIGINT, p_name VARCHAR(50))
LANGUAGE plpgsql AS $$
BEGIN
	UPDATE genres SET name = p_name WHERE genre_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_genre(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM tracks WHERE genre_id = p_id) THEN
		RAISE EXCEPTION 'Невозможно удалить жанр — к нему привязаны треки.';
	END IF;
	DELETE FROM genres WHERE genre_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_review(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	DELETE FROM reviews WHERE review_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_collection(p_title VARCHAR(100), p_description TEXT)
LANGUAGE plpgsql AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM collections WHERE LOWER(title) = LOWER(p_title)) THEN RAISE EXCEPTION 'Коллекция "%" уже существует.', p_title; END IF;
	INSERT INTO collections(title, description) VALUES (p_title, p_description);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_collection(p_id BIGINT, p_title VARCHAR(100), p_description TEXT)
LANGUAGE plpgsql AS $$
BEGIN
	UPDATE collections SET title = p_title, description = p_description WHERE collection_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_collection(p_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
	DELETE FROM collections WHERE collection_id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_track_to_collection(p_collection_id BIGINT, p_track_title VARCHAR(150))
LANGUAGE plpgsql AS $$
DECLARE v_track_id BIGINT;
BEGIN
	SELECT track_id INTO v_track_id FROM tracks WHERE LOWER(title) = LOWER(p_track_title);
	IF v_track_id IS NULL THEN RAISE EXCEPTION 'Трек "%" не найден.', p_track_title; END IF;
	IF EXISTS (SELECT 1 FROM collection_tracks WHERE collection_id = p_collection_id AND track_id = v_track_id) THEN
		RAISE EXCEPTION 'Трек "%" уже в коллекции.', p_track_title;
	END IF;
	INSERT INTO collection_tracks(collection_id, track_id) VALUES (p_collection_id, v_track_id);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_remove_track_from_collection(p_collection_id BIGINT, p_track_title VARCHAR(150))
LANGUAGE plpgsql AS $$
DECLARE v_track_id BIGINT;
BEGIN
	SELECT track_id INTO v_track_id FROM tracks WHERE LOWER(title) = LOWER(p_track_title);
	IF v_track_id IS NULL THEN RAISE EXCEPTION 'Трек "%" не найден.', p_track_title; END IF;
	DELETE FROM collection_tracks WHERE collection_id = p_collection_id AND track_id = v_track_id;
END;
$$;
