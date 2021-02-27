import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute, Params} from '@angular/router';
import { Usuario } from '../../models/usuario';
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { UsuarioService } from '../../servicios/usuario.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'listaUsuarios',
    templateUrl: './listaUsuarios.component.html',
    providers: [UsuarioService]
})
export class ListaUsuarios implements OnInit{
    public title:string;
    public usuarios: Usuario[];

    constructor(
        private _route: ActivatedRoute,
        private _router: Router,
        private _usuarioService: UsuarioService,
    ){
        this.usuarios = [];
        this.title = "Lista de usuarios"
    }

    ngOnInit(){
        this._usuarioService.getUsers().subscribe(
            response => {
              console.log(response);
              this.usuarios = response;
            },
            err => {
                console.log(err);
            }
        );
    }
} 