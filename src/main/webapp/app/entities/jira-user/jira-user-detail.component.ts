import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJiraUser } from 'app/shared/model/jira-user.model';

@Component({
  selector: 'jhi-jira-user-detail',
  templateUrl: './jira-user-detail.component.html'
})
export class JiraUserDetailComponent implements OnInit {
  jiraUser: IJiraUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jiraUser }) => (this.jiraUser = jiraUser));
  }

  previousState(): void {
    window.history.back();
  }
}
