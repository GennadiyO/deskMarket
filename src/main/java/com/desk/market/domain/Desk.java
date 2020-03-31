package com.desk.market.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.desk.market.domain.enumeration.DeskType;

/**
 * A Desk.
 */
@Entity
@Table(name = "desk")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Desk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "desk_id", nullable = false, unique = true)
    private Long deskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeskType type;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "modification_date")
    private Instant modificationDate;

    @OneToMany(mappedBy = "desk")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "desk")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Price> prices = new HashSet<>();

    @OneToMany(mappedBy = "desk")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DeskProperty> properties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeskId() {
        return deskId;
    }

    public Desk deskId(Long deskId) {
        this.deskId = deskId;
        return this;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public DeskType getType() {
        return type;
    }

    public Desk type(DeskType type) {
        this.type = type;
        return this;
    }

    public void setType(DeskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Desk name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Desk description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Desk creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModificationDate() {
        return modificationDate;
    }

    public Desk modificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
        return this;
    }

    public void setModificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public Desk photos(Set<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Desk addPhotos(Photo photo) {
        this.photos.add(photo);
        photo.setDesk(this);
        return this;
    }

    public Desk removePhotos(Photo photo) {
        this.photos.remove(photo);
        photo.setDesk(null);
        return this;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public Desk prices(Set<Price> prices) {
        this.prices = prices;
        return this;
    }

    public Desk addPrices(Price price) {
        this.prices.add(price);
        price.setDesk(this);
        return this;
    }

    public Desk removePrices(Price price) {
        this.prices.remove(price);
        price.setDesk(null);
        return this;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    public Set<DeskProperty> getProperties() {
        return properties;
    }

    public Desk properties(Set<DeskProperty> deskProperties) {
        this.properties = deskProperties;
        return this;
    }

    public Desk addProperties(DeskProperty deskProperty) {
        this.properties.add(deskProperty);
        deskProperty.setDesk(this);
        return this;
    }

    public Desk removeProperties(DeskProperty deskProperty) {
        this.properties.remove(deskProperty);
        deskProperty.setDesk(null);
        return this;
    }

    public void setProperties(Set<DeskProperty> deskProperties) {
        this.properties = deskProperties;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Desk)) {
            return false;
        }
        return id != null && id.equals(((Desk) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Desk{" +
            "id=" + getId() +
            ", deskId=" + getDeskId() +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            "}";
    }
}
