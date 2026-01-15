package com.caseclarity.evidence.adapter.cassandra;

import com.caseclarity.evidence.domain.Evidence;
import org.springframework.stereotype.Component;

@Component
public class EvidenceMapper {

    public EvidenceEntity toEntity(Evidence e) {
        return EvidenceEntity.builder()
                .key(
                        EvidencePrimaryKey.builder()
                                .tenantId(e.getTenantId())
                                .caseId(e.getCaseId())
                                .uploadedAt(e.getUploadedAt())
                                .evidenceId(e.getEvidenceId())
                                .build()
                )
                .contentHash(e.getContentHash())
                .previousHash(e.getPreviousHash())
                .uploadedBy(e.getUploadedBy())
                .build();
    }

    public Evidence toDomain(EvidenceEntity entity) {
        return Evidence.builder()
                .tenantId(entity.getKey().getTenantId())
                .caseId(entity.getKey().getCaseId())
                .evidenceId(entity.getKey().getEvidenceId())
                .uploadedAt(entity.getKey().getUploadedAt())
                .contentHash(entity.getContentHash())
                .previousHash(entity.getPreviousHash())
                .uploadedBy(entity.getUploadedBy())
                .build();
    }
}
