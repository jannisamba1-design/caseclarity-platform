package com.caseclarity.evidence.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class StorageService {

    public Mono<Void> store(
            UUID tenantId,
            UUID caseId,
            UUID evidenceId,
            FilePart file
    ) {
        /*
         * Future:
         * s3://caseclarity/{tenantId}/{caseId}/{evidenceId}
         *
         * For now:
         * We simulate successful storage
         */
        return Mono.empty();
    }
}
