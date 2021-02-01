package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data2.Member;
import br.com.empresa.healthcheckteam.backend.data2.Member.MemberBuilder;
import br.com.empresa.healthcheckteam.backend.data2.Team;
import br.com.empresa.healthcheckteam.backend.data2.Team.TeamBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void createASimpleMember() {
        Team teamA = createASimpleTeam();
        Member memberX = new MemberBuilder()
                .withUsername("user")
                .withPassword("password")
                .withName("Member X")
                .withTeam(teamA)
                .build();

        Member savedMember = repository.save(memberX);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getTeams()).containsExactly(teamA);
        assertThat(reloadTeam(teamA.getId()).getMembers()).containsExactly(savedMember);
    }

    private Team reloadTeam(Long teamId) {
        Optional<Team> maybeATeam = teamRepository.findById(teamId);
        assertThat(maybeATeam).isPresent();
        return maybeATeam.get();
    }

    private Team createASimpleTeam() {
        return teamRepository.save(new TeamBuilder().withName("Team A").build());
    }

}