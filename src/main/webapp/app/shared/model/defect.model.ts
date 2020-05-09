import { IStory } from 'app/shared/model/story.model';

export interface IDefect {
  id?: number;
  defectName?: string;
  description?: string;
  story?: IStory;
}

export class Defect implements IDefect {
  constructor(public id?: number, public defectName?: string, public description?: string, public story?: IStory) {}
}
