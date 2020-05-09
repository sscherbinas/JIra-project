import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITestCase } from 'app/shared/model/test-case.model';
import { TestCaseService } from './test-case.service';
import { TestCaseDeleteDialogComponent } from './test-case-delete-dialog.component';

@Component({
  selector: 'jhi-test-case',
  templateUrl: './test-case.component.html'
})
export class TestCaseComponent implements OnInit, OnDestroy {
  testCases?: ITestCase[];
  eventSubscriber?: Subscription;

  constructor(protected testCaseService: TestCaseService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.testCaseService.query().subscribe((res: HttpResponse<ITestCase[]>) => (this.testCases = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTestCases();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITestCase): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTestCases(): void {
    this.eventSubscriber = this.eventManager.subscribe('testCaseListModification', () => this.loadAll());
  }

  delete(testCase: ITestCase): void {
    const modalRef = this.modalService.open(TestCaseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.testCase = testCase;
  }
}
