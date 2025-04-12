package com.bingebox.venueservice.mappers;

import com.bingebox.commons.venue.service.VenueResponse;
import com.bingebox.venueservice.model.Venue;

public class VenueMapper {

    public static Venue toEntity(com.bingebox.commons.venue.service.CreateVenueRequest venueDto) {
        return Venue.builder()
                .name(venueDto.getName())
                .address(venueDto.getAddress())
                .pinCode(venueDto.getPinCode())
                .contactNumber(venueDto.getContactNumber())
                .latitude(venueDto.getLatitude())
                .longitude(venueDto.getLongitude())
                .build();
    }

    public static VenueResponse toResponse(Venue venue) {
        return VenueResponse.newBuilder()
                .setId(venue.getId().toString())
                .setName(venue.getName())
                .setAddress(venue.getAddress())
                .setPinCode(venue.getPinCode())
                .setContactNumber(venue.getContactNumber())
                .setLatitude(venue.getLatitude())
                .setLongitude(venue.getLongitude())
                .build();
    }

}
