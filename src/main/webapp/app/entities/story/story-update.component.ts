import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStory, Story } from 'app/shared/model/story.model';
import { StoryService } from './story.service';
import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from 'app/entities/sprint/sprint.service';

@Component({
  selector: 'jhi-story-update',
  templateUrl: './story-update.component.html'
})
export class StoryUpdateComponent implements OnInit {
  isSaving = false;
  sprints: ISprint[] = [];

  editForm = this.fb.group({
    id: [],
    storyName: [null, [Validators.required, Validators.maxLength(100)]],
    createdBy: [null, [Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(1000)]],
    sprints: []
  });

  constructor(
    protected storyService: StoryService,
    protected sprintService: SprintService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ story }) => {
      this.updateForm(story);

      this.sprintService.query().subscribe((res: HttpResponse<ISprint[]>) => (this.sprints = res.body || []));
    });
  }

  updateForm(story: IStory): void {
    this.editForm.patchValue({
      id: story.id,
      storyName: story.storyName,
      createdBy: story.createdBy,
      description: story.description,
      sprints: story.sprints
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const story = this.createFromForm();
    if (story.id !== undefined) {
      this.subscribeToSaveResponse(this.storyService.update(story));
    } else {
      this.subscribeToSaveResponse(this.storyService.create(story));
    }
  }

  private createFromForm(): IStory {
    return {
      ...new Story(),
      id: this.editForm.get(['id'])!.value,
      storyName: this.editForm.get(['storyName'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      description: this.editForm.get(['description'])!.value,
      sprints: this.editForm.get(['sprints'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStory>>): void {
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

  trackById(index: number, item: ISprint): any {
    return item.id;
  }
}
