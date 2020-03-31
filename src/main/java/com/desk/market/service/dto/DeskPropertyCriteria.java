package com.desk.market.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.desk.market.domain.enumeration.DeskPropertyType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.desk.market.domain.DeskProperty} entity. This class is used
 * in {@link com.desk.market.web.rest.DeskPropertyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /desk-properties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeskPropertyCriteria implements Serializable, Criteria {
    /**
     * Class for filtering DeskPropertyType
     */
    public static class DeskPropertyTypeFilter extends Filter<DeskPropertyType> {

        public DeskPropertyTypeFilter() {
        }

        public DeskPropertyTypeFilter(DeskPropertyTypeFilter filter) {
            super(filter);
        }

        @Override
        public DeskPropertyTypeFilter copy() {
            return new DeskPropertyTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter deskPropertyId;

    private DeskPropertyTypeFilter type;

    private StringFilter value;

    private LongFilter deskId;

    public DeskPropertyCriteria() {
    }

    public DeskPropertyCriteria(DeskPropertyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deskPropertyId = other.deskPropertyId == null ? null : other.deskPropertyId.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.deskId = other.deskId == null ? null : other.deskId.copy();
    }

    @Override
    public DeskPropertyCriteria copy() {
        return new DeskPropertyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getDeskPropertyId() {
        return deskPropertyId;
    }

    public void setDeskPropertyId(LongFilter deskPropertyId) {
        this.deskPropertyId = deskPropertyId;
    }

    public DeskPropertyTypeFilter getType() {
        return type;
    }

    public void setType(DeskPropertyTypeFilter type) {
        this.type = type;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public LongFilter getDeskId() {
        return deskId;
    }

    public void setDeskId(LongFilter deskId) {
        this.deskId = deskId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeskPropertyCriteria that = (DeskPropertyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(deskPropertyId, that.deskPropertyId) &&
            Objects.equals(type, that.type) &&
            Objects.equals(value, that.value) &&
            Objects.equals(deskId, that.deskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        deskPropertyId,
        type,
        value,
        deskId
        );
    }

    @Override
    public String toString() {
        return "DeskPropertyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (deskPropertyId != null ? "deskPropertyId=" + deskPropertyId + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (deskId != null ? "deskId=" + deskId + ", " : "") +
            "}";
    }

}
