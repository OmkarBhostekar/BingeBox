package com.bingebox.venueservice.service;

import com.bingebox.commons.exceptions.AuditoriumNotFoundException;
import com.bingebox.commons.exceptions.SeatNotFoundException;
import com.bingebox.commons.venue.service.*;
import com.bingebox.venueservice.model.Auditorium;
import com.bingebox.venueservice.model.Seat;
import com.bingebox.venueservice.repository.AuditoriumRepository;
import com.bingebox.venueservice.repository.SeatsRepository;
import com.bingebox.venueservice.mappers.SeatMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class SeatService extends SeatServiceGrpc.SeatServiceImplBase {

    private final SeatsRepository seatsRepository;
    private final AuditoriumRepository auditoriumRepository;

    // Create multiple seats
    @Override
    public void createSeats(CreateSeatsRequest request, StreamObserver<CreateSeatsResponse> responseObserver) {
        Auditorium auditorium = getAuditoriumById(request.getAuditoriumId());
        List<Seat> seats = request.getSeatDetailsList()
                .stream()
                .map(SeatMapper::toEntity)
                .peek(seat -> seat.setAuditorium(auditorium))
                .toList();

        List<Seat> savedSeats = seatsRepository.saveAll(seats);
        CreateSeatsResponse response = CreateSeatsResponse.newBuilder()
                .addAllSeats(SeatMapper.toResponses(savedSeats))
                .build();

        sendResponse(responseObserver, response);
    }

    // Get a seat by ID
    @Override
    public void getSeat(GetSeatRequest request, StreamObserver<SeatResponse> responseObserver) {
        Seat seat = getSeatById(request.getSeatId());
        SeatResponse response = SeatMapper.toResponse(seat);
        sendResponse(responseObserver, response);
    }

    // Update a seat by ID
    @Override
    public void updateSeat(UpdateSeatRequest request, StreamObserver<SeatResponse> responseObserver) {
        Seat seat = getSeatById(request.getSeatId());

        if (request.hasSeatNumber()) seat.setSeatNumber(request.getSeatNumber());
        if (request.hasType()) seat.setSeatType(SeatMapper.seatTypeFromProto(request.getType()));

        Seat updatedSeat = seatsRepository.save(seat);
        SeatResponse response = SeatMapper.toResponse(updatedSeat);
        sendResponse(responseObserver, response);
    }

    // Delete a seat by ID
    @Override
    public void deleteSeat(DeleteSeatRequest request, StreamObserver<DeleteSeatResponse> responseObserver) {
        Seat seat = getSeatById(request.getSeatId());
        seatsRepository.delete(seat);

        DeleteSeatResponse response = DeleteSeatResponse.newBuilder()
                .setMessage("Seat deleted successfully")
                .build();

        sendResponse(responseObserver, response);
    }

    // Get all seats by auditorium ID
    @Override
    public void getAllSeatsByAuditoriumId(GetAllSeatsByAuditoriumIdRequest request, StreamObserver<GetAllSeatsByAuditoriumIdResponse> responseObserver) {
        UUID auditoriumId = UUID.fromString(request.getAuditoriumId());
        List<Seat> seats = seatsRepository.findByAuditorium_Id(auditoriumId);

        GetAllSeatsByAuditoriumIdResponse response = GetAllSeatsByAuditoriumIdResponse.newBuilder()
                .addAllSeats(SeatMapper.toResponses(seats))
                .build();

        sendResponse(responseObserver, response);
    }

    // Utility method to get a seat by ID, throws a custom exception if not found
    private Seat getSeatById(String seatId) {
        UUID seatUUID = UUID.fromString(seatId);
        return seatsRepository.findById(seatUUID)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found for id: " + seatId));
    }

    // Utility method to get an auditorium by ID, throws a custom exception if not found
    private Auditorium getAuditoriumById(String auditoriumId) {
        UUID auditoriumUUID = UUID.fromString(auditoriumId);
        return auditoriumRepository.findById(auditoriumUUID)
                .orElseThrow(() -> new AuditoriumNotFoundException("Auditorium not found for id: " + auditoriumId));
    }

    // Common method to send a response to the observer
    private <T> void sendResponse(StreamObserver<T> observer, T response) {
        observer.onNext(response);
        observer.onCompleted();
    }
}
