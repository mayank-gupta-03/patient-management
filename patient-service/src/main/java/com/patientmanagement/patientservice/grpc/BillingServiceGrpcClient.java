package com.patientmanagement.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}")
            String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ) {
        log.info("Connecting to billing service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(BillingRequest billingRequest) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(billingRequest.getPatientId())
                .setName(billingRequest.getName())
                .setEmail(billingRequest.getEmail())
                .build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Create billing account response: {}", response);

        return response;
    }
}
