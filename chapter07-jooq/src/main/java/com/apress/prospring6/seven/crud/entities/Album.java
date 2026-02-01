package com.apress.prospring6.seven.crud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by iuliana.cosmina on 4/21/17.
 */
@Entity
@Table(name = "ALBUM")
public class Album extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 3L;

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
	public String toString() {
		return "Album - Id: " + id + ", Singer id: " + singer.getId()
				+ ", Title: " + title + ", Release Date: " + releaseDate;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		Album album = (Album) o;
		if (!Objects.equals(title, album.title))
			return false;
		return Objects.equals(releaseDate, album.releaseDate);
	}

	@Override public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
		return result;
	}
}
