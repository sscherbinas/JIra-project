import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISprint, Sprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

@Component({
  selector: 'jhi-sprint-update',
  templateUrl: './sprint-update.component.html'
})
export class SprintUpdateComponent implements OnInit {
  isSaving = false;
  projects: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    sprintName: [null, [Validators.required, Validators.maxLength(100)]],
    sprintCount: [],
    project: []
  });

  constructor(
    protected sprintService: SprintService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sprint }) => {
      this.updateForm(sprint);

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));
    });
  }

  updateForm(sprint: ISprint): void {
    this.editForm.patchValue({
      id: sprint.id,
      sprintName: sprint.sprintName,
      sprintCount: sprint.sprintCount,
      project: sprint.project
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sprint = this.createFromForm();
    if (sprint.id !== undefined) {
      this.subscribeToSaveResponse(this.sprintService.update(sprint));
    } else {
      this.subscribeToSaveResponse(this.sprintService.create(sprint));
    }
  }

  private createFromForm(): ISprint {
    return {
      ...new Sprint(),
      id: this.editForm.get(['id'])!.value,
      sprintName: this.editForm.get(['sprintName'])!.value,
      sprintCount: this.editForm.get(['sprintCount'])!.value,
      project: this.editForm.get(['project'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISprint>>): void {
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

  trackById(index: number, item: IProject): any {
    return item.id;
  }
}
