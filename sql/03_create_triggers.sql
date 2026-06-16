

-- 1. Триггер проверки корректности длительности трека
CREATE OR REPLACE FUNCTION fn_validate_track_duration()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.duration_seconds <= 0 OR NEW.duration_seconds > 36000 THEN
        RAISE EXCEPTION 'Некорректная длительность трека "%": % секунд. Допустимый диапазон: 1 - 36000.', NEW.title, NEW.duration_seconds;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_validate_track_duration ON tracks;
CREATE TRIGGER trg_validate_track_duration
BEFORE INSERT OR UPDATE ON tracks
FOR EACH ROW
EXECUTE FUNCTION fn_validate_track_duration();


-- 2. Триггер запрета удаления исполнителя, если у него есть альбомы
CREATE OR REPLACE FUNCTION fn_prevent_delete_artist_with_albums()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM albums WHERE artist_id = OLD.artist_id) THEN
        RAISE EXCEPTION 'Невозможно удалить исполнителя "%", так как в базе данных уже привязаны его альбомы.', OLD.name;
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_delete_artist_with_albums ON artists;
CREATE TRIGGER trg_prevent_delete_artist_with_albums
BEFORE DELETE ON artists
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_delete_artist_with_albums();


-- 3. Триггер запрета удаления альбома, если в нем уже есть треки
CREATE OR REPLACE FUNCTION fn_prevent_delete_album_with_tracks()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM tracks WHERE album_id = OLD.album_id) THEN
        RAISE EXCEPTION 'Невозможно удалить альбом "%", так как в нем уже добавлены музыкальные треки.', OLD.title;
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_delete_album_with_tracks ON albums;
CREATE TRIGGER trg_prevent_delete_album_with_tracks
BEFORE DELETE ON albums
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_delete_album_with_tracks();


-- 4. Триггер проверки диапазона оценок в отзывах (от 1 до 10)
CREATE OR REPLACE FUNCTION fn_validate_review_rating()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.rating < 1 OR NEW.rating > 10 THEN
        RAISE EXCEPTION 'Некорректная оценка: %. Значение рейтинга должно находиться в диапазоне от 1 до 10.', NEW.rating;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_validate_review_rating ON reviews;
CREATE TRIGGER trg_validate_review_rating
BEFORE INSERT OR UPDATE ON reviews
FOR EACH ROW
EXECUTE FUNCTION fn_validate_review_rating();

-- 5. Триггер запрета на добавление трека в плейлист дважды
CREATE OR REPLACE FUNCTION fn_prevent_duplicate_playlist_track()
RETURNS TRIGGER AS
$$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM playlist_tracks
        WHERE playlist_id = NEW.playlist_id
          AND track_id = NEW.track_id
    ) THEN
        RAISE EXCEPTION
        'Трек уже присутствует в данном плейлисте.';
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_duplicate_playlist_track
ON playlist_tracks;

CREATE TRIGGER trg_prevent_duplicate_playlist_track
BEFORE INSERT
ON playlist_tracks
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_duplicate_playlist_track();