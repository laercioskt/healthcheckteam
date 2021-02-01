package br.com.empresa.healthcheckteam.backend.data2;

import br.com.empresa.healthcheckteam.backend.data.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
public class Team extends BaseEntity implements Serializable {

    private String name;

    @ManyToMany(mappedBy = "teams")
    Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<Assessment> assessments = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Set<Assessment> getAssessments() {
        return assessments;
    }

    public static class TeamBuilder {

        private Long id;

        private String name;

        public TeamBuilder() {
        }

        public TeamBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TeamBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public Team build() {
            Team team = new Team();
            if (id != null)
                team.setId(id);
            team.setName(this.name);
            return team;
        }

    }

}