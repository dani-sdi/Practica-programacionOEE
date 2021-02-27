import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute, Params} from '@angular/router';
import { Usuario } from '../../models/usuario';
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { UsuarioService } from '../../servicios/usuario.service';

@Component({
    selector: 'register',
    templateUrl: './register.component.html',
    providers: [UsuarioService]
})
export class RegisterComponent implements OnInit{
    public title:string;
    public usuario: Usuario;
    public registerForm: FormGroup;
    public status: string;

    constructor(
        private _route: ActivatedRoute,
        private _router: Router,
        private fb: FormBuilder,
        private _usuarioService: UsuarioService,
    ){
        this.status = "";
        this.title = "Regitro"
        this.usuario = new Usuario(
            "",
            "",
            "",
            0,
            ""
            );

        this.registerForm = this.fb.group({
            nombre: new FormControl(this.usuario.nombre,[
                Validators.required
            ]),
            email: new FormControl(this.usuario.email,[
                Validators.required,
                Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
            ]),
            edad: new FormControl(this.usuario.edad,[
                Validators.required
            ]),
            pass: new FormControl(this.usuario.pass,[
                Validators.required
            ])   
        });
       
    }

    ngOnInit(){
        console.log('Componente register cargado');

    }

    onSubmit(){
        this._usuarioService.register(this.usuario).subscribe(
            response => {
                this.status = "success";
            },
            err => {
                console.log(err);
                this.status = "error";
            }
        )
    }

}