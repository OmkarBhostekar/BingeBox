-- bookings table
CREATE TABLE bookings (
  id UUID PRIMARY KEY,
  user_id VARCHAR NOT NULL,         -- external reference
  event_show_id VARCHAR NOT NULL,   -- external reference
  payment_id VARCHAR,               -- external reference
  booking_status TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- booked_seats table
CREATE TABLE booked_seats (
  id UUID PRIMARY KEY,
  booking_id UUID NOT NULL,
  event_show_id VARCHAR NOT NULL,   -- external reference
  seat_id VARCHAR NOT NULL,         -- external reference
  status TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- foreign key constraint within BookingService
ALTER TABLE booked_seats
ADD CONSTRAINT fk_booked_seats_booking
FOREIGN KEY (booking_id) REFERENCES bookings(id);