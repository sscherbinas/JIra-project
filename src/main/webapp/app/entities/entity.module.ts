import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'organization',
        loadChildren: () => import('./organization/organization.module').then(m => m.JiraOrganizationModule)
      },
      {
        path: 'jira-user',
        loadChildren: () => import('./jira-user/jira-user.module').then(m => m.JiraJiraUserModule)
      },
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.JiraProjectModule)
      },
      {
        path: 'sprint',
        loadChildren: () => import('./sprint/sprint.module').then(m => m.JiraSprintModule)
      },
      {
        path: 'story',
        loadChildren: () => import('./story/story.module').then(m => m.JiraStoryModule)
      },
      {
        path: 'test-case',
        loadChildren: () => import('./test-case/test-case.module').then(m => m.JiraTestCaseModule)
      },
      {
        path: 'defect',
        loadChildren: () => import('./defect/defect.module').then(m => m.JiraDefectModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JiraEntityModule {}
