import { ISprint } from 'app/shared/model/sprint.model';
import { IJiraUser } from 'app/shared/model/jira-user.model';

export interface IProject {
  id?: number;
  projectName?: string;
  projectManager?: string;
  teamSize?: number;
  sprints?: ISprint[];
  users?: IJiraUser[];
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public projectName?: string,
    public projectManager?: string,
    public teamSize?: number,
    public sprints?: ISprint[],
    public users?: IJiraUser[]
  ) {}
}
