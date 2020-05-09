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
 * Criteria class for the {@link com.shcherbyna.jira.domain.Project} entity. This class is used
 * in {@link com.shcherbyna.jira.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter projectName;

    private StringFilter projectManager;

    private LongFilter teamSize;

    private LongFilter sprintsId;

    private LongFilter usersId;

    public ProjectCriteria() {
    }

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.projectName = other.projectName == null ? null : other.projectName.copy();
        this.projectManager = other.projectManager == null ? null : other.projectManager.copy();
        this.teamSize = other.teamSize == null ? null : other.teamSize.copy();
        this.sprintsId = other.sprintsId == null ? null : other.sprintsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
    }

    public StringFilter getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(StringFilter projectManager) {
        this.projectManager = projectManager;
    }

    public LongFilter getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(LongFilter teamSize) {
        this.teamSize = teamSize;
    }

    public LongFilter getSprintsId() {
        return sprintsId;
    }

    public void setSprintsId(LongFilter sprintsId) {
        this.sprintsId = sprintsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(projectManager, that.projectManager) &&
            Objects.equals(teamSize, that.teamSize) &&
            Objects.equals(sprintsId, that.sprintsId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        projectName,
        projectManager,
        teamSize,
        sprintsId,
        usersId
        );
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (projectName != null ? "projectName=" + projectName + ", " : "") +
                (projectManager != null ? "projectManager=" + projectManager + ", " : "") +
                (teamSize != null ? "teamSize=" + teamSize + ", " : "") +
                (sprintsId != null ? "sprintsId=" + sprintsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
