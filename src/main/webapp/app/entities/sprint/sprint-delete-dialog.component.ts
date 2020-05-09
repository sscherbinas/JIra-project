import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';

@Component({
  templateUrl: './sprint-delete-dialog.component.html'
})
export class SprintDeleteDialogComponent {
  sprint?: ISprint;

  constructor(protected sprintService: SprintService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sprintService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sprintListModification');
      this.activeModal.close();
    });
  }
}
