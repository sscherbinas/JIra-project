import { IJiraUser } from 'app/shared/model/jira-user.model';

export interface IOrganization {
  id?: number;
  organizationName?: string;
  users?: IJiraUser[];
}

export class Organization implements IOrganization {
  constructor(public id?: number, public organizationName?: string, public users?: IJiraUser[]) {}
}
