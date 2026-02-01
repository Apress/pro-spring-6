/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.ten.entities;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by iuliana.cosmina on 04/08/2022
 */
@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity<U> implements Serializable {
	@Serial
	private static final long serialVersionUID = 8L;

	@CreatedDate
	@Column(name = "CREATED_DATE")
	protected LocalDateTime createdDate;

	@CreatedBy
	@Column(name = "CREATED_BY")
	protected String createdBy;

	@LastModifiedBy
	@Column(name = "LAST_MODIFIED_BY")
	protected String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_DATE")
	protected LocalDateTime lastModifiedDate;

	public Optional<String> getCreatedBy() {
		return Optional.of(createdBy);
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Optional<LocalDateTime> getCreatedDate() {
		return Optional.of(createdDate);
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Optional<String> getLastModifiedBy() {
		return Optional.of(lastModifiedBy);
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Optional<LocalDateTime> getLastModifiedDate() {
		return Optional.of(lastModifiedDate);
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
