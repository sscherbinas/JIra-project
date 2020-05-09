import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraSharedModule } from 'app/shared/shared.module';
import { SprintComponent } from './sprint.component';
import { SprintDetailComponent } from './sprint-detail.component';
import { SprintUpdateComponent } from './sprint-update.component';
import { SprintDeleteDialogComponent } from './sprint-delete-dialog.component';
import { sprintRoute } from './sprint.route';

@NgModule({
  imports: [JiraSharedModule, RouterModule.forChild(sprintRoute)],
  declarations: [SprintComponent, SprintDetailComponent, SprintUpdateComponent, SprintDeleteDialogComponent],
  entryComponents: [SprintDeleteDialogComponent]
})
export class JiraSprintModule {}
