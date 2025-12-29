ALTER TABLE users
DROP CONSTRAINT fk_users_photo;

ALTER TABLE users
DROP COLUMN photo_id;

ALTER TABLE photos
ADD COLUMN user_id UUID;

ALTER TABLE photos
ADD CONSTRAINT fk_photos_user
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE photos
ADD CONSTRAINT uq_photos_user UNIQUE (user_id);
