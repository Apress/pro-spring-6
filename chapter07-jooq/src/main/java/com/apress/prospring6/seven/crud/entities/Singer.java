package com.apress.prospring6.seven.crud.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serial;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by iuliana.cosmina on 4/22/17.
 */
@Entity
@Table(name = "SINGER")
@NamedQueries({
		@NamedQuery(name=Singer.FIND_SINGER_BY_ID,
				query="select distinct s from Singer s " +
						"left join fetch s.albums a " +
						"left join fetch s.instruments i " +
						"where s.id = :id"),
		@NamedQuery(name=Singer.FIND_ALL_WITH_ALBUM,
				query="select distinct s from Singer s " +
						"left join fetch s.albums a " +
						"left join fetch s.instruments i")
})
public class Singer extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 2L;

	public static final String FIND_SINGER_BY_ID = "Singer.findById";
	public static final String FIND_ALL_WITH_ALBUM = "Singer.findAllWithAlbum";

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "BIRTH_DATE")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "singer", cascade=CascadeType.ALL,
			orphanRemoval=true)
	private Set<Album> albums = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "SINGER_INSTRUMENT",
			joinColumns = @JoinColumn(name = "SINGER_ID"),
			inverseJoinColumns = @JoinColumn(name = "INSTRUMENT_ID"))
	private Set<Instrument> instruments = new HashSet<>();


	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}


	public Set<Album> getAlbums() {
		return albums;
	}

	public Set<Instrument> getInstruments() {
		return instruments;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean addAlbum(Album album) {
		album.setSinger(this);
		return albums.add(album);
	}

	public void removeAlbum(Album album) {
		albums.remove(album);
	}

	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public void setInstruments(Set<Instrument> instruments) {
		this.instruments = instruments;
	}

	public boolean addInstrument(Instrument instrument) {
		return instruments.add(instrument);
	}

	public String toString() {
		return "Singer - Id: " + id + ", First name: " + firstName
				+ ", Last name: " + lastName + ", Birthday: " + birthDate;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		Singer singer = (Singer) o;
		if (!Objects.equals(firstName, singer.firstName))
			return false;
		if (!Objects.equals(lastName, singer.lastName))
			return false;
		return Objects.equals(birthDate, singer.birthDate);
	}

	@Override public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		return result;
	}
}
