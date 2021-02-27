import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute, Params} from '@angular/router';
import { Usuario } from '../../models/usuario';
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { UsuarioService } from '../../servicios/usuario.service';

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    providers: [UsuarioService]
})
export class LoginComponent implements OnInit{
    public title:string;
    public usuario: Usuario;
    public loginForm: FormGroup;
    public status: string;
    public mensajeError: string;
    public identity: Usuario;
    public token: string;

    constructor(
        private _route: ActivatedRoute,
        private _router: Router,
        private fb: FormBuilder,
        private _usuarioService: UsuarioService,
    ){
        this.token = "";
        this.status = "";
        this.mensajeError = "";
        this.title = "Iniciar sesión"
        this.usuario = new Usuario(
            "",
            "",
            "",
            0,
            ""
            );

        this.loginForm = this.fb.group({
            email: new FormControl(this.usuario.email,[
                Validators.required
            ]),
            pass: new FormControl(this.usuario.pass,[
                Validators.required
            ])   
        });
        this.identity = this.usuario;
    }

    ngOnInit(){
        
    }

    onSubmit(){
        this._usuarioService.signup(this.usuario).subscribe(
            response => {
                this.status = "success";
                this.identity = this.usuario;
                this.token = response.token;
                
                localStorage.setItem('identity', JSON.stringify( this.usuario));
                localStorage.setItem('token', JSON.stringify( this.token));

                this._router.navigate(['/listaUsuarios']);
            },
            err => {
                this.status = "error";
                this.mensajeError = err.error.message;  
            }
        );
    }
}