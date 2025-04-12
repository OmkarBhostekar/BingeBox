-- venues table
CREATE TABLE venues (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  pin_code VARCHAR(20),
  contact_number VARCHAR(20),
  latitude DECIMAL(9, 6),  -- Using DECIMAL for latitude/longitude with appropriate precision
  longitude DECIMAL(9, 6)  -- Using DECIMAL for latitude/longitude with appropriate precision
);

-- auditoriums table
CREATE TABLE auditoriums (
  id UUID PRIMARY KEY,
  venue_id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  capacity INTEGER NOT NULL,
  FOREIGN KEY (venue_id) REFERENCES venues(id)
);

-- seats table
CREATE TABLE seats (
  id UUID PRIMARY KEY,
  auditorium_id UUID NOT NULL,
  seat_number VARCHAR(20) NOT NULL,
  type VARCHAR(50) NOT NULL,
  FOREIGN KEY (auditorium_id) REFERENCES auditoriums(id),
  UNIQUE (auditorium_id, seat_number)  -- Ensuring unique seat numbers within an auditorium
);
