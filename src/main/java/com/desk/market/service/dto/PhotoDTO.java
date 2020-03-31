package com.desk.market.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.desk.market.domain.Photo} entity.
 */
public class PhotoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long photoId;

    
    @Lob
    private byte[] photo;

    private String photoContentType;

    private Long deskId;

    private String deskDeskId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
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

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (photoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), photoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
            "id=" + getId() +
            ", photoId=" + getPhotoId() +
            ", photo='" + getPhoto() + "'" +
            ", deskId=" + getDeskId() +
            ", deskDeskId='" + getDeskDeskId() + "'" +
            "}";
    }
}
