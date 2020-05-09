import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDefect, Defect } from 'app/shared/model/defect.model';
import { DefectService } from './defect.service';
import { IStory } from 'app/shared/model/story.model';
import { StoryService } from 'app/entities/story/story.service';

@Component({
  selector: 'jhi-defect-update',
  templateUrl: './defect-update.component.html'
})
export class DefectUpdateComponent implements OnInit {
  isSaving = false;
  stories: IStory[] = [];

  editForm = this.fb.group({
    id: [],
    defectName: [null, [Validators.required, Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(100)]],
    story: []
  });

  constructor(
    protected defectService: DefectService,
    protected storyService: StoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ defect }) => {
      this.updateForm(defect);

      this.storyService.query().subscribe((res: HttpResponse<IStory[]>) => (this.stories = res.body || []));
    });
  }

  updateForm(defect: IDefect): void {
    this.editForm.patchValue({
      id: defect.id,
      defectName: defect.defectName,
      description: defect.description,
      story: defect.story
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const defect = this.createFromForm();
    if (defect.id !== undefined) {
      this.subscribeToSaveResponse(this.defectService.update(defect));
    } else {
      this.subscribeToSaveResponse(this.defectService.create(defect));
    }
  }

  private createFromForm(): IDefect {
    return {
      ...new Defect(),
      id: this.editForm.get(['id'])!.value,
      defectName: this.editForm.get(['defectName'])!.value,
      description: this.editForm.get(['description'])!.value,
      story: this.editForm.get(['story'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDefect>>): void {
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

  trackById(index: number, item: IStory): any {
    return item.id;
  }
}
