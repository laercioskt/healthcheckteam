package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

}
