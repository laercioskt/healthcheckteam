package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
