package com.desk.market.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.desk.market.domain.Price} entity.
 */
public class PriceDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long priceId;

    @NotNull
    private Instant creationDate;

    @NotNull
    private BigDecimal price;


    private Long deskId;

    private String deskDeskId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public String getDeskDeskId() {
        return deskDeskId;
    }

    public void setDeskDeskId(String deskDeskId) {
        this.deskDeskId = deskDeskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriceDTO priceDTO = (PriceDTO) o;
        if (priceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), priceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PriceDTO{" +
            "id=" + getId() +
            ", priceId=" + getPriceId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", price=" + getPrice() +
            ", deskId=" + getDeskId() +
            ", deskDeskId='" + getDeskDeskId() + "'" +
            "}";
    }
}
