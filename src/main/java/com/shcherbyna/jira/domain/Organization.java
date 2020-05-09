package com.shcherbyna.jira.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "organization_name", length = 100, nullable = false)
    private String organizationName;

    @OneToMany(mappedBy = "organization")
    private Set<JiraUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Organization organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Set<JiraUser> getUsers() {
        return users;
    }

    public Organization users(Set<JiraUser> jiraUsers) {
        this.users = jiraUsers;
        return this;
    }

    public Organization addUsers(JiraUser jiraUser) {
        this.users.add(jiraUser);
        jiraUser.setOrganization(this);
        return this;
    }

    public Organization removeUsers(JiraUser jiraUser) {
        this.users.remove(jiraUser);
        jiraUser.setOrganization(null);
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
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", organizationName='" + getOrganizationName() + "'" +
            "}";
    }
}
