package com.caseclarity.evidence.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Evidence {

    UUID tenantId;
    UUID caseId;
    UUID evidenceId;

    String contentHash;
    String previousHash;

    UUID uploadedBy;
    Instant uploadedAt;
}
