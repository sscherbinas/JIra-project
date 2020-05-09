import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraSharedModule } from 'app/shared/shared.module';
import { DefectComponent } from './defect.component';
import { DefectDetailComponent } from './defect-detail.component';
import { DefectUpdateComponent } from './defect-update.component';
import { DefectDeleteDialogComponent } from './defect-delete-dialog.component';
import { defectRoute } from './defect.route';

@NgModule({
  imports: [JiraSharedModule, RouterModule.forChild(defectRoute)],
  declarations: [DefectComponent, DefectDetailComponent, DefectUpdateComponent, DefectDeleteDialogComponent],
  entryComponents: [DefectDeleteDialogComponent]
})
export class JiraDefectModule {}
