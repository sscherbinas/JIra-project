import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';
import { SprintDeleteDialogComponent } from './sprint-delete-dialog.component';

@Component({
  selector: 'jhi-sprint',
  templateUrl: './sprint.component.html'
})
export class SprintComponent implements OnInit, OnDestroy {
  sprints?: ISprint[];
  eventSubscriber?: Subscription;

  constructor(protected sprintService: SprintService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.sprintService.query().subscribe((res: HttpResponse<ISprint[]>) => (this.sprints = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSprints();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISprint): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSprints(): void {
    this.eventSubscriber = this.eventManager.subscribe('sprintListModification', () => this.loadAll());
  }

  delete(sprint: ISprint): void {
    const modalRef = this.modalService.open(SprintDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sprint = sprint;
  }
}
