package com.caseclarity.evidence.adapter.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.Instant;
import java.util.UUID;

@PrimaryKeyClass
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidencePrimaryKey {

    @PrimaryKeyColumn(name = "tenant_id", type = PrimaryKeyType.PARTITIONED)
    private UUID tenantId;

    @PrimaryKeyColumn(name = "case_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    private UUID caseId;

    @PrimaryKeyColumn(name = "uploaded_at", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Instant uploadedAt;

    @PrimaryKeyColumn(name = "evidence_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private UUID evidenceId;
}
