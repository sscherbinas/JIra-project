import { IProject } from 'app/shared/model/project.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IJiraUser {
  id?: number;
  userName?: string;
  jobTitle?: string;
  projects?: IProject[];
  organization?: IOrganization;
}

export class JiraUser implements IJiraUser {
  constructor(
    public id?: number,
    public userName?: string,
    public jobTitle?: string,
    public projects?: IProject[],
    public organization?: IOrganization
  ) {}
}
