package com.bingebox.commons.mappers;

import com.bingebox.commons.dto.venue.AuditoriumResponseDTO;
import com.bingebox.commons.venue.service.AuditoriumResponse;

public class AuditoriumMapper {
    public static AuditoriumResponseDTO toAuditoriumResponseDTO(
            AuditoriumResponse auditoriumResponse
    ) {
        return new AuditoriumResponseDTO(
                auditoriumResponse.getId(),
                auditoriumResponse.getName(),
                auditoriumResponse.getCapacity(),
                VenueMapper.toVenueResponseDTO(auditoriumResponse.getVenue())
        );
    }
}
