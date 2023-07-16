package com.apress.prospring6.eighteen.audit;

import org.hibernate.SessionFactory;
import org.hibernate.stat.*;
import jakarta.annotation.PostConstruct;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 7/9/17.
 * Description: Custom implementation of https://github.com/manuelbernhardt/hibernate-core/blob/master/hibernate-core/src/main/java/org/hibernate/jmx/StatisticsService.java
 * that is not part of Hibernate 6.
 */
@Component
@ManagedResource(description = "JMX managed resource",
		objectName = "jmxDemo:name=ProSpring6SingerApp-hibernate")
public class CustomHibernateStatistics {

	private final SessionFactory sessionFactory;

	public CustomHibernateStatistics(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Statistics stats;

	@PostConstruct
	private void init() {
		stats = sessionFactory.getStatistics();
	}

	@ManagedOperation(description="Get statistics for entity name")
	@ManagedOperationParameter(name = "entityName", description = "Full class name for the entity")
	public EntityStatistics getEntityStatistics(String entityName) {
		return stats.getEntityStatistics(entityName);
	}

	@ManagedOperation(description="Get statistics for role")
	@ManagedOperationParameter(name = "role", description = "Role name")
	public CollectionStatistics getCollectionStatistics(String role) {
		return stats.getCollectionStatistics(role);
	}

	@ManagedOperation(description="Get statistics for query")
	@ManagedOperationParameter(name = "hql", description = "Query name")
	public QueryStatistics getQueryStatistics(String hql) {
		return stats.getQueryStatistics(hql);
	}

	@ManagedAttribute
	public long getEntityDeleteCount() {
		return stats.getEntityDeleteCount();
	}

	@ManagedAttribute
	public long getEntityInsertCount() {
		return stats.getEntityInsertCount();
	}

	@ManagedAttribute
	public long getEntityLoadCount() {
		return stats.getEntityLoadCount();
	}

	@ManagedAttribute
	public long getEntityFetchCount() {
		return stats.getEntityFetchCount();
	}

	@ManagedAttribute
	public long getEntityUpdateCount() {
		return stats.getEntityUpdateCount();
	}

	@ManagedAttribute
	public long getQueryExecutionCount() {
		return stats.getQueryExecutionCount();
	}

	@ManagedAttribute
	public long getQueryCacheHitCount() {
		return stats.getQueryCacheHitCount();
	}

	@ManagedAttribute
	public long getQueryExecutionMaxTime() {
		return stats.getQueryExecutionMaxTime();
	}

	@ManagedAttribute
	public long getQueryCacheMissCount() {
		return stats.getQueryCacheMissCount();
	}

	@ManagedAttribute
	public long getQueryCachePutCount() {
		return stats.getQueryCachePutCount();
	}

	@ManagedAttribute
	public long getFlushCount() {
		return stats.getFlushCount();
	}

	@ManagedAttribute
	public long getConnectCount() {
		return stats.getConnectCount();
	}

	@ManagedAttribute
	public long getSecondLevelCacheHitCount() {
		return stats.getSecondLevelCacheHitCount();
	}

	@ManagedAttribute
	public long getSecondLevelCacheMissCount() {
		return stats.getSecondLevelCacheMissCount();
	}

	@ManagedAttribute
	public long getSecondLevelCachePutCount() {
		return stats.getSecondLevelCachePutCount();
	}

	@ManagedAttribute
	public long getSessionCloseCount() {
		return stats.getSessionCloseCount();
	}

	@ManagedAttribute
	public long getSessionOpenCount() {
		return stats.getSessionOpenCount();
	}

	@ManagedAttribute
	public long getCollectionLoadCount() {
		return stats.getCollectionLoadCount();
	}

	@ManagedAttribute
	public long getCollectionFetchCount() {
		return stats.getCollectionFetchCount();
	}

	@ManagedAttribute
	public long getCollectionUpdateCount() {
		return stats.getCollectionUpdateCount();
	}

	@ManagedAttribute
	public long getCollectionRemoveCount() {
		return stats.getCollectionRemoveCount();
	}

	@ManagedAttribute
	public long getCollectionRecreateCount() {
		return stats.getCollectionRecreateCount();
	}

	@ManagedAttribute
	public long getStartTime() {
		return stats.getStartTime();
	}

	@ManagedAttribute
	public boolean isStatisticsEnabled() {
		return stats.isStatisticsEnabled();
	}

	@ManagedAttribute
	public String[] getEntityNames() {
		return stats.getEntityNames();
	}

	@ManagedAttribute
	public String[] getQueries() {
		return stats.getQueries();
	}

	@ManagedAttribute
	public long getSuccessfulTransactionCount() {
		return stats.getSuccessfulTransactionCount();
	}
	@ManagedAttribute
	public long getTransactionCount() {
		return stats.getTransactionCount();
	}

	@ManagedAttribute
	public long getPrepareStatementCount(){
		return stats.getPrepareStatementCount();
	}

	@ManagedAttribute
	public String getQueryExecutionMaxTimeQueryString() {
		return stats.getQueryExecutionMaxTimeQueryString();
	}

}