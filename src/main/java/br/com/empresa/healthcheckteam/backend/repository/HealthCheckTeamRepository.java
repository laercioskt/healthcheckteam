package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.HealthCheckTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckTeamRepository extends JpaRepository<HealthCheckTeam, Long> {

}
