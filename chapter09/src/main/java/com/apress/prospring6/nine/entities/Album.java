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
package com.apress.prospring6.nine.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by iuliana.cosmina on 4/21/17.
 */
@Entity
@Table(name = "ALBUM")
@NamedQueries({
		@NamedQuery(name=Album.FIND_ALL, query="select a from Album a where a.singer= :singer")
})
public class Album extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 3L;

	public static final String FIND_ALL = "Album.findAll";

	@Column
	private String title;
	@Column(name = "RELEASE_DATE")
	private LocalDate releaseDate;

	@ManyToOne
	@JoinColumn(name = "SINGER_ID")
	private Singer singer;

	public Singer getSinger() {
		return this.singer;
	}

	public String getTitle() {
		return this.title;
	}

	public LocalDate getReleaseDate() {
		return this.releaseDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSinger(Singer singer) {
		this.singer = singer;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Album album = (Album) o;
		if(this.id != null) {
			return this.id.equals(((Album) o).id);
		}
		return title.equals(album.title) && releaseDate.equals(album.releaseDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, releaseDate);
	}

	@Override
	public String toString() {
		return "Album - Id: " + id + ", Singer id: " + singer.getId()
				+ ", Title: " + title + ", Release Date: " + releaseDate;
	}
}
