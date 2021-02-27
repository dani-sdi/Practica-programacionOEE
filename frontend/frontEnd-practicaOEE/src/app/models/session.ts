import { Usuario } from "./usuario";

export class Session {
    constructor(
    public token: string,
    public user: Usuario){};
  }