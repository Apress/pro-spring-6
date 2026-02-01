package com.apress.prospring6.seven.crud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by iuliana.cosmina on 4/23/17.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	// Starting with version 5.x Hibernate will create a new table to maintain Sequence,
	// It now selects the GenerationType.TABLE which uses a database table to generate primary keys.
	// This approach requires a lot of database queries and pessimistic locks to generate unique values.
	//@GeneratedValue(strategy = GenerationType.AUTO) // <= this requires a table called 'hibernate_sequence'
	@GenericGenerator(name = "native_generator", strategy = "native")
	@GeneratedValue(generator = "native_generator")
	@Column(updatable = false)
	protected Long id;

	@Version
	@Column(name = "VERSION")
	private int version;

	/**
	 * Returns the entity identifier. This identifier is unique per entity. It is used by persistence frameworks used in a project,
	 * and although is public, it should not be used by application code.
	 * This identifier is mapped by ORM (Object Relational Mapper) to the database primary key of the Person record to which
	 * the entity instance is mapped.
	 *
	 * @return the unique entity identifier
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the entity identifier. This identifier is unique per entity.  Is is used by persistence frameworks
	 * and although is public, it should never be set by application code.
	 *
	 * @param id the unique entity identifier
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AbstractEntity that = (AbstractEntity) o;
		return Objects.equals(id, that.id);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
