package com.bingebox.venueservice.service;

import com.bingebox.commons.venue.service.*;
import com.bingebox.venueservice.mappers.AuditoriumMapper;
import com.bingebox.venueservice.model.Auditorium;
import com.bingebox.venueservice.model.Venue;
import com.bingebox.venueservice.repository.AuditoriumRepository;
import com.bingebox.venueservice.repository.VenueRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class AuditoriumService extends AuditoriumServiceGrpc.AuditoriumServiceImplBase {

    private final AuditoriumRepository auditoriumRepository;
    private final VenueRepository venueRepository;

    @Override
    public void createAuditorium(CreateAuditoriumRequest request, StreamObserver<AuditoriumResponse> responseObserver) {
        Venue venue = getVenueOrThrow(request.getVenueId());

        Auditorium auditorium = Auditorium.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .venue(venue)
                .build();

        Auditorium saved = auditoriumRepository.save(auditorium);
        sendResponse(responseObserver, AuditoriumMapper.toResponse(saved));
    }

    @Override
    public void getAuditorium(GetAuditoriumRequest request, StreamObserver<AuditoriumResponse> responseObserver) {
        Auditorium auditorium = getAuditoriumOrThrow(request.getAuditoriumId());
        sendResponse(responseObserver, AuditoriumMapper.toResponse(auditorium));
    }

    @Override
    public void updateAuditorium(UpdateAuditoriumRequest request, StreamObserver<AuditoriumResponse> responseObserver) {
        Auditorium auditorium = getAuditoriumOrThrow(request.getAuditoriumId());

        if (request.hasName()) {
            auditorium.setName(request.getName());
        }

        if (request.hasCapacity()) {
            auditorium.setCapacity(request.getCapacity());
        }

        Auditorium updated = auditoriumRepository.save(auditorium);
        sendResponse(responseObserver, AuditoriumMapper.toResponse(updated));
    }

    @Override
    public void deleteAuditorium(DeleteAuditoriumRequest request, StreamObserver<DeleteAuditoriumResponse> responseObserver) {
        Auditorium auditorium = getAuditoriumOrThrow(request.getAuditoriumId());
        auditoriumRepository.delete(auditorium);

        var response = DeleteAuditoriumResponse.newBuilder()
                .setMessage("Auditorium deleted successfully").build();
        sendResponse(responseObserver, response);
    }

    private Venue getVenueOrThrow(String id) {
        try {
            return venueRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found for id: " + id));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID: " + id, e);
        }
    }

    private Auditorium getAuditoriumOrThrow(String id) {
        try {
            return auditoriumRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new IllegalArgumentException("Auditorium not found for id: " + id));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID: " + id, e);
        }
    }

    private <T> void sendResponse(StreamObserver<T> observer, T response) {
        observer.onNext(response);
        observer.onCompleted();
    }
}
