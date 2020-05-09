import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJiraUser, JiraUser } from 'app/shared/model/jira-user.model';
import { JiraUserService } from './jira-user.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IProject | IOrganization;

@Component({
  selector: 'jhi-jira-user-update',
  templateUrl: './jira-user-update.component.html'
})
export class JiraUserUpdateComponent implements OnInit {
  isSaving = false;
  projects: IProject[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    userName: [null, [Validators.required, Validators.maxLength(100)]],
    jobTitle: [null, [Validators.maxLength(100)]],
    projects: [],
    organization: []
  });

  constructor(
    protected jiraUserService: JiraUserService,
    protected projectService: ProjectService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jiraUser }) => {
      this.updateForm(jiraUser);

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(jiraUser: IJiraUser): void {
    this.editForm.patchValue({
      id: jiraUser.id,
      userName: jiraUser.userName,
      jobTitle: jiraUser.jobTitle,
      projects: jiraUser.projects,
      organization: jiraUser.organization
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jiraUser = this.createFromForm();
    if (jiraUser.id !== undefined) {
      this.subscribeToSaveResponse(this.jiraUserService.update(jiraUser));
    } else {
      this.subscribeToSaveResponse(this.jiraUserService.create(jiraUser));
    }
  }

  private createFromForm(): IJiraUser {
    return {
      ...new JiraUser(),
      id: this.editForm.get(['id'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      projects: this.editForm.get(['projects'])!.value,
      organization: this.editForm.get(['organization'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJiraUser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IProject[], option: IProject): IProject {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
