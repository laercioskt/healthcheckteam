package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.Team;
import br.com.empresa.healthcheckteam.backend.data.Team.TeamBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository repository;

    @Test
    public void createASimpleTeam() {
        Team teamA = new TeamBuilder()
                .withName("Team A")
                .build();
        Team savedTeam = repository.save(teamA);
        assertThat(savedTeam.getId()).isNotNull();
    }

}