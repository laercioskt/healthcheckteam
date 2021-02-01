package br.com.empresa.healthcheckteam.backend.data2;

import br.com.empresa.healthcheckteam.backend.data.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
public class Member extends BaseEntity implements Serializable {

    private String username;
    private String password;
    private String name;

    @ManyToMany
    @JoinTable(name = "member_team",
            joinColumns = {@JoinColumn(name = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")})
    private final Set<Team> teams = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public static class MemberBuilder {

        private Long id;

        private String username;
        private String password;
        private String name;
        private Team team;

        public MemberBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder withTeam(Team team) {
            this.team = team;
            return this;
        }

        public Member build() {
            Member member = new Member();
            if (id != null) {
                member.setId(id);
            }
            member.setUsername(this.username);
            member.setPassword(this.password);
            member.setName(this.name);
            member.addTeam(team);
            return member;
        }
    }
}