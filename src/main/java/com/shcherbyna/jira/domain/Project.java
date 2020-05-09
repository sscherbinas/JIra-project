package com.shcherbyna.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_name", length = 100, nullable = false)
    private String projectName;

    @Size(max = 100)
    @Column(name = "project_manager", length = 100)
    private String projectManager;

    @Column(name = "team_size")
    private Long teamSize;

    @OneToMany(mappedBy = "project")
    private Set<Sprint> sprints = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    @JsonIgnore
    private Set<JiraUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public Project projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public Project projectManager(String projectManager) {
        this.projectManager = projectManager;
        return this;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Long getTeamSize() {
        return teamSize;
    }

    public Project teamSize(Long teamSize) {
        this.teamSize = teamSize;
        return this;
    }

    public void setTeamSize(Long teamSize) {
        this.teamSize = teamSize;
    }

    public Set<Sprint> getSprints() {
        return sprints;
    }

    public Project sprints(Set<Sprint> sprints) {
        this.sprints = sprints;
        return this;
    }

    public Project addSprints(Sprint sprint) {
        this.sprints.add(sprint);
        sprint.setProject(this);
        return this;
    }

    public Project removeSprints(Sprint sprint) {
        this.sprints.remove(sprint);
        sprint.setProject(null);
        return this;
    }

    public void setSprints(Set<Sprint> sprints) {
        this.sprints = sprints;
    }

    public Set<JiraUser> getUsers() {
        return users;
    }

    public Project users(Set<JiraUser> jiraUsers) {
        this.users = jiraUsers;
        return this;
    }

    public Project addUsers(JiraUser jiraUser) {
        this.users.add(jiraUser);
        jiraUser.getProjects().add(this);
        return this;
    }

    public Project removeUsers(JiraUser jiraUser) {
        this.users.remove(jiraUser);
        jiraUser.getProjects().remove(this);
        return this;
    }

    public void setUsers(Set<JiraUser> jiraUsers) {
        this.users = jiraUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", projectManager='" + getProjectManager() + "'" +
            ", teamSize=" + getTeamSize() +
            "}";
    }
}
