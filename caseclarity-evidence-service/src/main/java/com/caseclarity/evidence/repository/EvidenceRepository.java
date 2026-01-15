//package com.caseclarity.evidence.repository;
//
//import com.caseclarity.evidence.dto.EvidencePrimaryKey;
//import org.springframework.data.cassandra.repository.Query;
//import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@Repository
//public interface EvidenceRepository
//        extends ReactiveCassandraRepository<src.main.java.com.caseclarity.evidence.dto.EvidenceEntity, EvidencePrimaryKey> {
//
//    @Query("""
//        SELECT * FROM evidence_by_tenant
//        WHERE tenant_id = :tenantId
//          AND case_id = :caseId
//    """)
//    Flux<src.main.java.com.caseclarity.evidence.dto.EvidenceEntity> findByTenantAndCase(
//            UUID tenantId,
//            UUID caseId
//    );
//
//    @Query("""
//        SELECT content_hash FROM evidence_by_tenant
//        WHERE tenant_id = :tenantId
//          AND case_id = :caseId
//        LIMIT 1
//    """)
//    Mono<String> findLatestHash(
//            UUID tenantId,
//            UUID caseId
//    );
//}
