CREATE or replace PROCEDURE userRegister(
	username VARCHAR(20),
	email VARCHAR(20),
	password VARCHAR(45),
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	isArtist BOOLEAN
)
AS $$
BEGIN 
INSERT INTO users(username, email,password, first_name, last_name, isArtist)
    VALUES(username, email,password, first_name, last_name, isArtist);
END;
$$ LANGUAGE plpgsql;