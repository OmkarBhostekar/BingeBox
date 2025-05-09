package com.bingebox.eventservice.config;

import com.bingebox.commons.venue.service.AuditoriumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class EventServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    // Method to retrieve the first instance of the venue-service
    private ServiceInstance getVenueServiceInstance() {
        // Find the venue service instance from Eureka
        ServiceInstance instance = discoveryClient.getInstances("venue-service").getFirst();

        if (instance == null) {
            throw new RuntimeException("No instances of venue-service found");
        }

        return instance;
    }

    // Method to create the gRPC channel using the instance details
    private ManagedChannel createGrpcChannel(ServiceInstance instance) {
        // Get the gRPC port from the instance metadata
        String grpcPort = instance.getMetadata().getOrDefault("grpc.port", "9090");
        
        return ManagedChannelBuilder
                .forAddress(instance.getHost(), Integer.parseInt(grpcPort))
                .usePlaintext()
                .build();
    }

    // Bean for AuditoriumServiceBlockingStub
    @Bean
    @Lazy
    public AuditoriumServiceGrpc.AuditoriumServiceBlockingStub auditoriumServiceBlockingStub() {
        // Get the venue service instance
        ServiceInstance instance = getVenueServiceInstance();

        // Return the blocking stub for gRPC
        return AuditoriumServiceGrpc.newBlockingStub(createGrpcChannel(instance));

    }
}
