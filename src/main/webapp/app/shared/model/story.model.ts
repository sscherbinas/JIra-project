import { ITestCase } from 'app/shared/model/test-case.model';
import { IDefect } from 'app/shared/model/defect.model';
import { ISprint } from 'app/shared/model/sprint.model';

export interface IStory {
  id?: number;
  storyName?: string;
  createdBy?: string;
  description?: string;
  testcases?: ITestCase[];
  defects?: IDefect[];
  sprints?: ISprint;
}

export class Story implements IStory {
  constructor(
    public id?: number,
    public storyName?: string,
    public createdBy?: string,
    public description?: string,
    public testcases?: ITestCase[],
    public defects?: IDefect[],
    public sprints?: ISprint
  ) {}
}
