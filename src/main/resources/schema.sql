CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(40),
            login VARCHAR(40),
            email VARCHAR(40),
            birthday TIMESTAMP
          );

CREATE TABLE IF NOT EXISTS ratings (
            rating_id INTEGER PRIMARY KEY,
            rating_name VARCHAR(40)
          );

CREATE TABLE IF NOT EXISTS films (
            id INTEGER PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(40),
            description VARCHAR(255),
            duration INTEGER,
            release_date TIMESTAMP,
            mpa_id INTEGER REFERENCES ratings(rating_id)
          );

CREATE TABLE IF NOT EXISTS friends (
            row_id INTEGER PRIMARY KEY AUTO_INCREMENT,
            user_id INTEGER REFERENCES users(id),
            friend_id INTEGER,
            friendshipStatus VARCHAR(40)
          );

CREATE TABLE IF NOT EXISTS genres (
            genre_id INTEGER PRIMARY KEY,
            genre_name VARCHAR(40)
          );

CREATE TABLE IF NOT EXISTS genresForFilms (
            row_id INTEGER PRIMARY KEY AUTO_INCREMENT,
            film_id INTEGER REFERENCES films(id),
            genre_id INTEGER REFERENCES genres(genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
            row_id INTEGER PRIMARY KEY AUTO_INCREMENT,
            user_id INTEGER REFERENCES users(id),
            film_id INTEGER REFERENCES films(id)
);














