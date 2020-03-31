package com.desk.market.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.desk.market.domain.enumeration.DeskType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.desk.market.domain.Desk} entity. This class is used
 * in {@link com.desk.market.web.rest.DeskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /desks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeskCriteria implements Serializable, Criteria {
    /**
     * Class for filtering DeskType
     */
    public static class DeskTypeFilter extends Filter<DeskType> {

        public DeskTypeFilter() {
        }

        public DeskTypeFilter(DeskTypeFilter filter) {
            super(filter);
        }

        @Override
        public DeskTypeFilter copy() {
            return new DeskTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter deskId;

    private DeskTypeFilter type;

    private StringFilter name;

    private StringFilter description;

    private InstantFilter creationDate;

    private InstantFilter modificationDate;

    private LongFilter photosId;

    private LongFilter pricesId;

    private LongFilter propertiesId;

    public DeskCriteria() {
    }

    public DeskCriteria(DeskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deskId = other.deskId == null ? null : other.deskId.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modificationDate = other.modificationDate == null ? null : other.modificationDate.copy();
        this.photosId = other.photosId == null ? null : other.photosId.copy();
        this.pricesId = other.pricesId == null ? null : other.pricesId.copy();
        this.propertiesId = other.propertiesId == null ? null : other.propertiesId.copy();
    }

    @Override
    public DeskCriteria copy() {
        return new DeskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getDeskId() {
        return deskId;
    }

    public void setDeskId(LongFilter deskId) {
        this.deskId = deskId;
    }

    public DeskTypeFilter getType() {
        return type;
    }

    public void setType(DeskTypeFilter type) {
        this.type = type;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public InstantFilter getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(InstantFilter modificationDate) {
        this.modificationDate = modificationDate;
    }

    public LongFilter getPhotosId() {
        return photosId;
    }

    public void setPhotosId(LongFilter photosId) {
        this.photosId = photosId;
    }

    public LongFilter getPricesId() {
        return pricesId;
    }

    public void setPricesId(LongFilter pricesId) {
        this.pricesId = pricesId;
    }

    public LongFilter getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(LongFilter propertiesId) {
        this.propertiesId = propertiesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeskCriteria that = (DeskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(deskId, that.deskId) &&
            Objects.equals(type, that.type) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modificationDate, that.modificationDate) &&
            Objects.equals(photosId, that.photosId) &&
            Objects.equals(pricesId, that.pricesId) &&
            Objects.equals(propertiesId, that.propertiesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        deskId,
        type,
        name,
        description,
        creationDate,
        modificationDate,
        photosId,
        pricesId,
        propertiesId
        );
    }

    @Override
    public String toString() {
        return "DeskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (deskId != null ? "deskId=" + deskId + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (modificationDate != null ? "modificationDate=" + modificationDate + ", " : "") +
                (photosId != null ? "photosId=" + photosId + ", " : "") +
                (pricesId != null ? "pricesId=" + pricesId + ", " : "") +
                (propertiesId != null ? "propertiesId=" + propertiesId + ", " : "") +
            "}";
    }

}
