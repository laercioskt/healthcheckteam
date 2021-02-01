package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
