import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraSharedModule } from 'app/shared/shared.module';
import { JiraUserComponent } from './jira-user.component';
import { JiraUserDetailComponent } from './jira-user-detail.component';
import { JiraUserUpdateComponent } from './jira-user-update.component';
import { JiraUserDeleteDialogComponent } from './jira-user-delete-dialog.component';
import { jiraUserRoute } from './jira-user.route';

@NgModule({
  imports: [JiraSharedModule, RouterModule.forChild(jiraUserRoute)],
  declarations: [JiraUserComponent, JiraUserDetailComponent, JiraUserUpdateComponent, JiraUserDeleteDialogComponent],
  entryComponents: [JiraUserDeleteDialogComponent]
})
export class JiraJiraUserModule {}
