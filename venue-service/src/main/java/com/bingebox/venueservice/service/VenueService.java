package com.bingebox.venueservice.service;

import com.bingebox.commons.exceptions.VenueNotFoundException;
import com.bingebox.commons.venue.service.*;
import com.bingebox.venueservice.mappers.VenueMapper;
import com.bingebox.venueservice.model.Venue;
import com.bingebox.venueservice.repository.VenueRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class VenueService extends VenueServiceGrpc.VenueServiceImplBase {

    private final VenueRepository venueRepository;

    @Override
    public void createVenue(CreateVenueRequest request, StreamObserver<VenueResponse> responseObserver) {
        var venue = VenueMapper.toEntity(request);
        var savedVenue = venueRepository.save(venue);
        sendResponse(responseObserver, VenueMapper.toResponse(savedVenue));
    }

    @Override
    public void getVenue(GetVenueRequest request, StreamObserver<VenueResponse> responseObserver) {
        Venue venue = findVenueOrThrow(request.getId());
        sendResponse(responseObserver, VenueMapper.toResponse(venue));
    }

    @Override
    public void updateVenue(UpdateVenueRequest request, StreamObserver<VenueResponse> responseObserver) {
        Venue venue = findVenueOrThrow(request.getId());

        // Apply updates only if present
        if (request.hasName()) venue.setName(request.getName());
        if (request.hasAddress()) venue.setAddress(request.getAddress());
        if (request.hasPinCode()) venue.setPinCode(request.getPinCode());
        if (request.hasContactNumber()) venue.setContactNumber(request.getContactNumber());
        if (request.hasLatitude()) venue.setLatitude(request.getLatitude());
        if (request.hasLongitude()) venue.setLongitude(request.getLongitude());

        var updatedVenue = venueRepository.save(venue);
        sendResponse(responseObserver, VenueMapper.toResponse(updatedVenue));
    }

    @Override
    public void deleteVenue(DeleteVenueRequest request, StreamObserver<DeleteVenueResponse> responseObserver) {
        Venue venue = findVenueOrThrow(request.getId());
        venueRepository.delete(venue);

        var response = DeleteVenueResponse.newBuilder()
                .setMessage("Venue deleted successfully")
                .build();
        sendResponse(responseObserver, response);
    }


    private <T> void sendResponse(StreamObserver<T> observer, T response) {
        observer.onNext(response);
        observer.onCompleted();
    }

    private Venue findVenueOrThrow(String id) {
        UUID venueId;
        try {
            venueId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new VenueNotFoundException("Invalid UUID: " + id);
        }

        return venueRepository.findById(venueId).orElseThrow(
                () -> new VenueNotFoundException("Venue not found for id: " + id));
    }
}
