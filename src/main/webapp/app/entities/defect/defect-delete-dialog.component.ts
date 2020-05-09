import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDefect } from 'app/shared/model/defect.model';
import { DefectService } from './defect.service';

@Component({
  templateUrl: './defect-delete-dialog.component.html'
})
export class DefectDeleteDialogComponent {
  defect?: IDefect;

  constructor(protected defectService: DefectService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.defectService.delete(id).subscribe(() => {
      this.eventManager.broadcast('defectListModification');
      this.activeModal.close();
    });
  }
}
