import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITestCase, TestCase } from 'app/shared/model/test-case.model';
import { TestCaseService } from './test-case.service';
import { IStory } from 'app/shared/model/story.model';
import { StoryService } from 'app/entities/story/story.service';

@Component({
  selector: 'jhi-test-case-update',
  templateUrl: './test-case-update.component.html'
})
export class TestCaseUpdateComponent implements OnInit {
  isSaving = false;
  stories: IStory[] = [];

  editForm = this.fb.group({
    id: [],
    testCaseName: [null, [Validators.required, Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(100)]],
    story: []
  });

  constructor(
    protected testCaseService: TestCaseService,
    protected storyService: StoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCase }) => {
      this.updateForm(testCase);

      this.storyService.query().subscribe((res: HttpResponse<IStory[]>) => (this.stories = res.body || []));
    });
  }

  updateForm(testCase: ITestCase): void {
    this.editForm.patchValue({
      id: testCase.id,
      testCaseName: testCase.testCaseName,
      description: testCase.description,
      story: testCase.story
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCase = this.createFromForm();
    if (testCase.id !== undefined) {
      this.subscribeToSaveResponse(this.testCaseService.update(testCase));
    } else {
      this.subscribeToSaveResponse(this.testCaseService.create(testCase));
    }
  }

  private createFromForm(): ITestCase {
    return {
      ...new TestCase(),
      id: this.editForm.get(['id'])!.value,
      testCaseName: this.editForm.get(['testCaseName'])!.value,
      description: this.editForm.get(['description'])!.value,
      story: this.editForm.get(['story'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCase>>): void {
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
