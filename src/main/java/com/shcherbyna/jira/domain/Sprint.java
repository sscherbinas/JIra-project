package com.shcherbyna.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sprint.
 */
@Entity
@Table(name = "sprint")
public class Sprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "sprint_name", length = 100, nullable = false)
    private String sprintName;

    @Column(name = "sprint_count")
    private Integer sprintCount;

    @OneToMany(mappedBy = "sprints")
    private Set<Story> stories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sprints")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSprintName() {
        return sprintName;
    }

    public Sprint sprintName(String sprintName) {
        this.sprintName = sprintName;
        return this;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public Integer getSprintCount() {
        return sprintCount;
    }

    public Sprint sprintCount(Integer sprintCount) {
        this.sprintCount = sprintCount;
        return this;
    }

    public void setSprintCount(Integer sprintCount) {
        this.sprintCount = sprintCount;
    }

    public Set<Story> getStories() {
        return stories;
    }

    public Sprint stories(Set<Story> stories) {
        this.stories = stories;
        return this;
    }

    public Sprint addStories(Story story) {
        this.stories.add(story);
        story.setSprints(this);
        return this;
    }

    public Sprint removeStories(Story story) {
        this.stories.remove(story);
        story.setSprints(null);
        return this;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public Project getProject() {
        return project;
    }

    public Sprint project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sprint)) {
            return false;
        }
        return id != null && id.equals(((Sprint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sprint{" +
            "id=" + getId() +
            ", sprintName='" + getSprintName() + "'" +
            ", sprintCount=" + getSprintCount() +
            "}";
    }
}
