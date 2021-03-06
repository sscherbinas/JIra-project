import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraSharedModule } from 'app/shared/shared.module';
import { StoryComponent } from './story.component';
import { StoryDetailComponent } from './story-detail.component';
import { StoryUpdateComponent } from './story-update.component';
import { StoryDeleteDialogComponent } from './story-delete-dialog.component';
import { storyRoute } from './story.route';

@NgModule({
  imports: [JiraSharedModule, RouterModule.forChild(storyRoute)],
  declarations: [StoryComponent, StoryDetailComponent, StoryUpdateComponent, StoryDeleteDialogComponent],
  entryComponents: [StoryDeleteDialogComponent]
})
export class JiraStoryModule {}
