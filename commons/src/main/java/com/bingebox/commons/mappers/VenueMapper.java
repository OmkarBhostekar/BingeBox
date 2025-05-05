package com.bingebox.commons.mappers;

import com.bingebox.commons.dto.venue.VenueResponseDTO;
import com.bingebox.commons.venue.service.VenueResponse;

public class VenueMapper {

    public static VenueResponseDTO toVenueResponseDTO(VenueResponse venueResponse) {
        return new VenueResponseDTO(
            venueResponse.getId(),
            venueResponse.getName(),
            venueResponse.getAddress(),
            venueResponse.getPinCode(),
            venueResponse.getContactNumber(),
            venueResponse.getLatitude(),
            venueResponse.getLongitude()
        );
    }
}
