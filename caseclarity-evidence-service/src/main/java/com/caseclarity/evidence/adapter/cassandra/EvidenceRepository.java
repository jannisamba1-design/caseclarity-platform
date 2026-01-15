package com.caseclarity.evidence.adapter.cassandra;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EvidenceRepository
        extends ReactiveCassandraRepository<EvidenceEntity, EvidencePrimaryKey> {

    @Query("""
        SELECT * FROM evidence_by_tenant
        WHERE tenant_id = :tenantId
          AND case_id = :caseId
    """)
    Flux<EvidenceEntity> findByTenantAndCase(UUID tenantId, UUID caseId);

    @Query("""
        SELECT content_hash FROM evidence_by_tenant
        WHERE tenant_id = :tenantId
          AND case_id = :caseId
        LIMIT 1
    """)
    Mono<EvidenceEntity> findLatestHash(UUID tenantId, UUID caseId);
}
