package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
