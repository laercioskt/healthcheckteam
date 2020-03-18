package br.com.empresa.healthcheckteam.backend.data;

import javax.persistence.Entity;

@Entity
public class HealthCheckTeam extends BaseEntity {

    private String code;

    public String getCode() {
        return code;
    }

}
