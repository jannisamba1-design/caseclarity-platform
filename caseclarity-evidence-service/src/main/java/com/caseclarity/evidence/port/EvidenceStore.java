package com.caseclarity.evidence.port;

import com.caseclarity.evidence.domain.Evidence;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EvidenceStore {

    Mono<Void> save(Evidence evidence);

    Flux<Evidence> findByTenantAndCase(UUID tenantId, UUID caseId);

    Mono<String> findLatestHash(UUID tenantId, UUID caseId);
}
