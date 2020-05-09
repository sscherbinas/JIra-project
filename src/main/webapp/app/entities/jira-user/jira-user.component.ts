import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJiraUser } from 'app/shared/model/jira-user.model';
import { JiraUserService } from './jira-user.service';
import { JiraUserDeleteDialogComponent } from './jira-user-delete-dialog.component';

@Component({
  selector: 'jhi-jira-user',
  templateUrl: './jira-user.component.html'
})
export class JiraUserComponent implements OnInit, OnDestroy {
  jiraUsers?: IJiraUser[];
  eventSubscriber?: Subscription;

  constructor(protected jiraUserService: JiraUserService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.jiraUserService.query().subscribe((res: HttpResponse<IJiraUser[]>) => (this.jiraUsers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJiraUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJiraUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJiraUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('jiraUserListModification', () => this.loadAll());
  }

  delete(jiraUser: IJiraUser): void {
    const modalRef = this.modalService.open(JiraUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jiraUser = jiraUser;
  }
}
