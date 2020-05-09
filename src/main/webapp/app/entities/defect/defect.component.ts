import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDefect } from 'app/shared/model/defect.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DefectService } from './defect.service';
import { DefectDeleteDialogComponent } from './defect-delete-dialog.component';

@Component({
  selector: 'jhi-defect',
  templateUrl: './defect.component.html'
})
export class DefectComponent implements OnInit, OnDestroy {
  defects: IDefect[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected defectService: DefectService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.defects = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.defectService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDefect[]>) => this.paginateDefects(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.defects = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDefects();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDefect): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDefects(): void {
    this.eventSubscriber = this.eventManager.subscribe('defectListModification', () => this.reset());
  }

  delete(defect: IDefect): void {
    const modalRef = this.modalService.open(DefectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.defect = defect;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDefects(data: IDefect[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.defects.push(data[i]);
      }
    }
  }
}
