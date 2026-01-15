package com.caseclarity.evidence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@EnableReactiveCassandraRepositories(
        basePackages = "com.caseclarity.evidence.adapter.cassandra"
)
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "caseclarity";
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected String getContactPoints() {
        return "cassandra";
    }

    @Override
    protected int getPort() {
        return 9042;
    }
}
