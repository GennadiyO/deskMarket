package com.desk.market.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.desk.market.domain.enumeration.DeskType;

/**
 * A DTO for the {@link com.desk.market.domain.Desk} entity.
 */
public class DeskDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long deskId;

    private DeskType type;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Instant creationDate;

    private Instant modificationDate;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public DeskType getType() {
        return type;
    }

    public void setType(DeskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeskDTO deskDTO = (DeskDTO) o;
        if (deskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeskDTO{" +
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
