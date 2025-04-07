-- events table
CREATE TABLE events (
  id UUID PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  category VARCHAR(100),
  language VARCHAR(50),
  age_rating VARCHAR(20),
  banner_url TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- event_shows table
CREATE TABLE event_shows (
  id UUID PRIMARY KEY,
  event_id UUID NOT NULL,
  auditorium_id UUID NOT NULL,
  scheduled_time TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (event_id) REFERENCES events(id)
);

-- show_seat_pricing table
CREATE TABLE show_seat_pricing (
  id UUID PRIMARY KEY,
  event_show_id UUID NOT NULL,
  seat_type VARCHAR(50) NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (event_show_id) REFERENCES event_shows(id),
  UNIQUE (event_show_id, seat_type)
);
