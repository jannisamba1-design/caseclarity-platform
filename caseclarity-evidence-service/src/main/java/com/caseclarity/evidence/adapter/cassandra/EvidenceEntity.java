package com.caseclarity.evidence.adapter.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("evidence_by_tenant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceEntity {

    @PrimaryKey
    private EvidencePrimaryKey key;

    @Column("content_hash")
    private String contentHash;

    @Column("previous_hash")
    private String previousHash;

    @Column("uploaded_by")
    private UUID uploadedBy;
}
