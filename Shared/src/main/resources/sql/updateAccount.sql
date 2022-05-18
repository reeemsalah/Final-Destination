CREATE or replace PROCEDURE  edit_Account(
currentEmail VARCHAR(20),
newEmail VARCHAR(20),
newPassword VARCHAR(45),
newFirstName VARCHAR(20),
newLastName VARCHAR(20),
newPhoto varchar(45))
language plpgsql
as $$
BEGIN
UPDATE users SET
email=CASE WHEN newEmail IS not NULL THEN newEmail ELSE email END,
password= CASE WHEN newPassword IS NOT NULL THEN newPassword ELSE password END,
first_name=CASE WHEN newFirstName IS NOT NULL THEN newFirstName ELSE first_name END,
last_name=CASE WHEN newLastName IS NOT NULL THEN newLastName ELSE last_name END,
profile_photo=CASE WHEN newPhoto IS NOT NULL THEN newPhoto ELSE profile_photo END
WHERE email=currentEmail;
END; $$