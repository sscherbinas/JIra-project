package com.shcherbyna.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Defect.
 */
@Entity
@Table(name = "defect")
public class Defect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "defect_name", length = 100, nullable = false)
    private String defectName;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("defects")
    private Story story;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefectName() {
        return defectName;
    }

    public Defect defectName(String defectName) {
        this.defectName = defectName;
        return this;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public String getDescription() {
        return description;
    }

    public Defect description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Story getStory() {
        return story;
    }

    public Defect story(Story story) {
        this.story = story;
        return this;
    }

    public void setStory(Story story) {
        this.story = story;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Defect)) {
            return false;
        }
        return id != null && id.equals(((Defect) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Defect{" +
            "id=" + getId() +
            ", defectName='" + getDefectName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
