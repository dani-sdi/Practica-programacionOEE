import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { Observable } from 'rxjs/Observable';
import { Usuario } from '../models/usuario';
import { GLOBAL } from './global';
import { Observable } from 'rxjs';

@Injectable()
export class UsuarioService{
    public url: string;

    constructor(public _http: HttpClient){
        this.url = GLOBAL.url;
    }

    register(usuario: Usuario): Observable<any>{
        let params = JSON.stringify(usuario);
        let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8').set('Accept', 'application/json');
        return this._http.post('/usuarios/guardar', params, {headers:headers});
    }

    signup(usuario: Usuario): Observable<any>{
        let params = JSON.stringify(usuario);
        let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8').set('Accept', 'application/json');
        return this._http.post(this.url + '/usuarios/login', params, {headers:headers});
    }

    getIndentity(){
        let identity = JSON.parse(localStorage.getItem('identity') || '');

        if(identity != "undefined"){
            return identity;
        }
        return null;
    }

    getToken(){
        let token = JSON.parse(localStorage.getItem('token') || '');

        if(token != "undefined"){
            return token;
        }
        return null;
    }

    getUsers(): Observable<any>{
        let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
                                        .set('Accept', 'application/json')
                                        .set('Authorization', this.getToken());
        return this._http.get(this.url + '/usuarios/listar', {headers:headers});
    }
}
