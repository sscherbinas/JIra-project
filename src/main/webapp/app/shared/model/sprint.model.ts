import { IStory } from 'app/shared/model/story.model';
import { IProject } from 'app/shared/model/project.model';

export interface ISprint {
  id?: number;
  sprintName?: string;
  sprintCount?: number;
  stories?: IStory[];
  project?: IProject;
}

export class Sprint implements ISprint {
  constructor(
    public id?: number,
    public sprintName?: string,
    public sprintCount?: number,
    public stories?: IStory[],
    public project?: IProject
  ) {}
}
