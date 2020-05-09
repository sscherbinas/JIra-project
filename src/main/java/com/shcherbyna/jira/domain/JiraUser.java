package com.shcherbyna.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A JiraUser.
 */
@Entity
@Table(name = "jira_user")
public class JiraUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Size(max = 100)
    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @ManyToMany
    @JoinTable(name = "jira_user_projects",
               joinColumns = @JoinColumn(name = "jira_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "projects_id", referencedColumnName = "id"))
    private Set<Project> projects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("users")
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public JiraUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public JiraUser jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public JiraUser projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public JiraUser addProjects(Project project) {
        this.projects.add(project);
        project.getUsers().add(this);
        return this;
    }

    public JiraUser removeProjects(Project project) {
        this.projects.remove(project);
        project.getUsers().remove(this);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Organization getOrganization() {
        return organization;
    }

    public JiraUser organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JiraUser)) {
            return false;
        }
        return id != null && id.equals(((JiraUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JiraUser{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            "}";
    }
}
