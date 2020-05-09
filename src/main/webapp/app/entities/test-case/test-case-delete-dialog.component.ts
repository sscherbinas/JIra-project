import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITestCase } from 'app/shared/model/test-case.model';
import { TestCaseService } from './test-case.service';

@Component({
  templateUrl: './test-case-delete-dialog.component.html'
})
export class TestCaseDeleteDialogComponent {
  testCase?: ITestCase;

  constructor(protected testCaseService: TestCaseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCaseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('testCaseListModification');
      this.activeModal.close();
    });
  }
}
