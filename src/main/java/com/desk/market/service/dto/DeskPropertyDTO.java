package com.desk.market.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.desk.market.domain.enumeration.DeskPropertyType;

/**
 * A DTO for the {@link com.desk.market.domain.DeskProperty} entity.
 */
public class DeskPropertyDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long deskPropertyId;

    @NotNull
    private DeskPropertyType type;

    @NotNull
    private String value;


    private Long deskId;

    private String deskDeskId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeskPropertyId() {
        return deskPropertyId;
    }

    public void setDeskPropertyId(Long deskPropertyId) {
        this.deskPropertyId = deskPropertyId;
    }

    public DeskPropertyType getType() {
        return type;
    }

    public void setType(DeskPropertyType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        DeskPropertyDTO deskPropertyDTO = (DeskPropertyDTO) o;
        if (deskPropertyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deskPropertyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeskPropertyDTO{" +
            "id=" + getId() +
            ", deskPropertyId=" + getDeskPropertyId() +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            ", deskId=" + getDeskId() +
            ", deskDeskId='" + getDeskDeskId() + "'" +
            "}";
    }
}
