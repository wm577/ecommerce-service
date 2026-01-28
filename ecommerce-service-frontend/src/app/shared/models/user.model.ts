import {Userrole} from './user-role.model';

export interface User {
  id: number;
  email: string;
  name: string;
  userRole: Userrole;
}
