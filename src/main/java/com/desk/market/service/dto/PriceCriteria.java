package com.desk.market.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.desk.market.domain.Price} entity. This class is used
 * in {@link com.desk.market.web.rest.PriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PriceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter priceId;

    private InstantFilter creationDate;

    private BigDecimalFilter price;

    private LongFilter deskId;

    public PriceCriteria() {
    }

    public PriceCriteria(PriceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.priceId = other.priceId == null ? null : other.priceId.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.deskId = other.deskId == null ? null : other.deskId.copy();
    }

    @Override
    public PriceCriteria copy() {
        return new PriceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getPriceId() {
        return priceId;
    }

    public void setPriceId(LongFilter priceId) {
        this.priceId = priceId;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
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
        final PriceCriteria that = (PriceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(priceId, that.priceId) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(deskId, that.deskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        priceId,
        creationDate,
        price,
        deskId
        );
    }

    @Override
    public String toString() {
        return "PriceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (priceId != null ? "priceId=" + priceId + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (deskId != null ? "deskId=" + deskId + ", " : "") +
            "}";
    }

}
