package com.desk.market.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.desk.market.domain.enumeration.DeskPropertyType;

/**
 * A DeskProperty.
 */
@Entity
@Table(name = "desk_property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DeskProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "desk_property_id", nullable = false, unique = true)
    private Long deskPropertyId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DeskPropertyType type;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("properties")
    private Desk desk;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeskPropertyId() {
        return deskPropertyId;
    }

    public DeskProperty deskPropertyId(Long deskPropertyId) {
        this.deskPropertyId = deskPropertyId;
        return this;
    }

    public void setDeskPropertyId(Long deskPropertyId) {
        this.deskPropertyId = deskPropertyId;
    }

    public DeskPropertyType getType() {
        return type;
    }

    public DeskProperty type(DeskPropertyType type) {
        this.type = type;
        return this;
    }

    public void setType(DeskPropertyType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public DeskProperty value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Desk getDesk() {
        return desk;
    }

    public DeskProperty desk(Desk desk) {
        this.desk = desk;
        return this;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeskProperty)) {
            return false;
        }
        return id != null && id.equals(((DeskProperty) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DeskProperty{" +
            "id=" + getId() +
            ", deskPropertyId=" + getDeskPropertyId() +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
