ALTER TABLE photos
    RENAME COLUMN url TO file_name;

ALTER TABLE photos
    DROP COLUMN description;

ALTER TABLE photos
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();