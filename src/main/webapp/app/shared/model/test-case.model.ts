import { IStory } from 'app/shared/model/story.model';

export interface ITestCase {
  id?: number;
  testCaseName?: string;
  description?: string;
  story?: IStory;
}

export class TestCase implements ITestCase {
  constructor(public id?: number, public testCaseName?: string, public description?: string, public story?: IStory) {}
}
