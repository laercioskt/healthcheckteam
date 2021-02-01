package br.com.empresa.healthcheckteam.backend.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id = -1;

    @Version
    private Long version;

    public BaseEntity() {
        this.version = 1L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version != null ? version : 1L;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == -1) {
            return false;
        }
        if (obj instanceof BaseEntity) {
            return id == ((BaseEntity) obj).getId();
        }
        return false;
    }

    public boolean isNew() {
        return getId() == -1;
    }

    @Override
    public int hashCode() {
        if (id == -1) {
            return super.hashCode();
        }
        return Objects.hash(id);
    }

}