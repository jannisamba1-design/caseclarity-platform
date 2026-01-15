package com.caseclarity.evidence.adapter.cassandra;

import com.caseclarity.evidence.domain.Evidence;
import com.caseclarity.evidence.port.EvidenceStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CassandraEvidenceStore implements EvidenceStore {

    private final EvidenceRepository repository;
    private final EvidenceMapper mapper;

    @Override
    public Mono<Void> save(Evidence evidence) {
        return repository.save(mapper.toEntity(evidence)).then();
    }

    @Override
    public Flux<Evidence> findByTenantAndCase(UUID tenantId, UUID caseId) {
        return repository
                .findByTenantAndCase(tenantId, caseId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<String> findLatestHash(UUID tenantId, UUID caseId) {
        return repository.findLatestHash(tenantId, caseId).map(EvidenceEntity::getContentHash);
    }
}
