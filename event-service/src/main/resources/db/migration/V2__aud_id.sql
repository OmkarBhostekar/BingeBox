ALTER TABLE event_shows
  ADD COLUMN auditorium_id_new VARCHAR(255);

UPDATE event_shows
  SET auditorium_id_new = CAST(auditorium_id AS VARCHAR);

ALTER TABLE event_shows
  DROP COLUMN auditorium_id;

ALTER TABLE event_shows
  RENAME COLUMN auditorium_id_new TO auditorium_id;
