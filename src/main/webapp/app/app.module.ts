import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { JiraSharedModule } from 'app/shared/shared.module';
import { JiraCoreModule } from 'app/core/core.module';
import { JiraAppRoutingModule } from './app-routing.module';
import { JiraHomeModule } from './home/home.module';
import { JiraEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    JiraSharedModule,
    JiraCoreModule,
    JiraHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    JiraEntityModule,
    JiraAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class JiraAppModule {}
