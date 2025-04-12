package com.bingebox.venueservice.mappers;

import com.bingebox.commons.venue.service.AuditoriumResponse;
import com.bingebox.venueservice.model.Auditorium;
import com.bingebox.venueservice.mappers.VenueMapper;

public class AuditoriumMapper {

    public static AuditoriumResponse toResponse(Auditorium auditorium) {
        return AuditoriumResponse.newBuilder()
                .setId(auditorium.getId().toString())
                .setName(auditorium.getName())
                .setCapacity(auditorium.getCapacity())
                .setVenue(VenueMapper.toResponse(auditorium.getVenue()))
                .build();
    }
}
