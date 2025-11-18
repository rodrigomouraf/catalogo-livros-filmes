CREATE TABLE IF NOT EXISTS catalog (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    creator VARCHAR(70),
    release_year INT,
    genre VARCHAR(50),
    synopsis TEXT,
    media_type VARCHAR(50) NOT NULL
);