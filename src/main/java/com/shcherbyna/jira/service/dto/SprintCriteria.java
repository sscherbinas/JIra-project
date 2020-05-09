package com.shcherbyna.jira.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.shcherbyna.jira.domain.Sprint} entity. This class is used
 * in {@link com.shcherbyna.jira.web.rest.SprintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sprints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SprintCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sprintName;

    private IntegerFilter sprintCount;

    private LongFilter storiesId;

    private LongFilter projectId;

    public SprintCriteria() {
    }

    public SprintCriteria(SprintCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sprintName = other.sprintName == null ? null : other.sprintName.copy();
        this.sprintCount = other.sprintCount == null ? null : other.sprintCount.copy();
        this.storiesId = other.storiesId == null ? null : other.storiesId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public SprintCriteria copy() {
        return new SprintCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSprintName() {
        return sprintName;
    }

    public void setSprintName(StringFilter sprintName) {
        this.sprintName = sprintName;
    }

    public IntegerFilter getSprintCount() {
        return sprintCount;
    }

    public void setSprintCount(IntegerFilter sprintCount) {
        this.sprintCount = sprintCount;
    }

    public LongFilter getStoriesId() {
        return storiesId;
    }

    public void setStoriesId(LongFilter storiesId) {
        this.storiesId = storiesId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SprintCriteria that = (SprintCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sprintName, that.sprintName) &&
            Objects.equals(sprintCount, that.sprintCount) &&
            Objects.equals(storiesId, that.storiesId) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sprintName,
        sprintCount,
        storiesId,
        projectId
        );
    }

    @Override
    public String toString() {
        return "SprintCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sprintName != null ? "sprintName=" + sprintName + ", " : "") +
                (sprintCount != null ? "sprintCount=" + sprintCount + ", " : "") +
                (storiesId != null ? "storiesId=" + storiesId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
