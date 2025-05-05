package com.bingebox.eventservice.mappers;

import com.bingebox.commons.mappers.AuditoriumMapper;
import com.bingebox.commons.venue.service.AuditoriumResponse;
import com.bingebox.commons.dto.event.EventShowResponse;
import com.bingebox.eventservice.model.EventShow;

public class EventShowMapper {

    public static EventShowResponse toEventShowResponse(
            AuditoriumResponse auditorium,
            EventShow eventShow
    ) {
        return new EventShowResponse(
                eventShow.getId().toString(),
                EventMapper.mapToResponse(eventShow.getEvent()),
                AuditoriumMapper.toAuditoriumResponseDTO(auditorium),
                eventShow.getScheduledTime().toString()
        );
    }

}
