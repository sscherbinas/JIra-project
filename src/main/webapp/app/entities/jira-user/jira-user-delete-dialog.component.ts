import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJiraUser } from 'app/shared/model/jira-user.model';
import { JiraUserService } from './jira-user.service';

@Component({
  templateUrl: './jira-user-delete-dialog.component.html'
})
export class JiraUserDeleteDialogComponent {
  jiraUser?: IJiraUser;

  constructor(protected jiraUserService: JiraUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jiraUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jiraUserListModification');
      this.activeModal.close();
    });
  }
}
