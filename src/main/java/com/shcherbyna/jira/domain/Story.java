package com.shcherbyna.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Story.
 */
@Entity
@Table(name = "story")
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "story_name", length = 100, nullable = false)
    private String storyName;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "story")
    private Set<TestCase> testcases = new HashSet<>();

    @OneToMany(mappedBy = "story")
    private Set<Defect> defects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("stories")
    private Sprint sprints;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoryName() {
        return storyName;
    }

    public Story storyName(String storyName) {
        this.storyName = storyName;
        return this;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Story createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public Story description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TestCase> getTestcases() {
        return testcases;
    }

    public Story testcases(Set<TestCase> testCases) {
        this.testcases = testCases;
        return this;
    }

    public Story addTestcases(TestCase testCase) {
        this.testcases.add(testCase);
        testCase.setStory(this);
        return this;
    }

    public Story removeTestcases(TestCase testCase) {
        this.testcases.remove(testCase);
        testCase.setStory(null);
        return this;
    }

    public void setTestcases(Set<TestCase> testCases) {
        this.testcases = testCases;
    }

    public Set<Defect> getDefects() {
        return defects;
    }

    public Story defects(Set<Defect> defects) {
        this.defects = defects;
        return this;
    }

    public Story addDefects(Defect defect) {
        this.defects.add(defect);
        defect.setStory(this);
        return this;
    }

    public Story removeDefects(Defect defect) {
        this.defects.remove(defect);
        defect.setStory(null);
        return this;
    }

    public void setDefects(Set<Defect> defects) {
        this.defects = defects;
    }

    public Sprint getSprints() {
        return sprints;
    }

    public Story sprints(Sprint sprint) {
        this.sprints = sprint;
        return this;
    }

    public void setSprints(Sprint sprint) {
        this.sprints = sprint;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Story)) {
            return false;
        }
        return id != null && id.equals(((Story) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Story{" +
            "id=" + getId() +
            ", storyName='" + getStoryName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
