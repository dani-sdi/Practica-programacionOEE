import { Component, OnInit, DoCheck } from '@angular/core';
import { UsuarioService } from './servicios/usuario.service';
import { Router, ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [UsuarioService]
})
export class AppComponent implements OnInit, DoCheck {
  public identity:any;

  title = 'frontEnd-practicaOEE';

  constructor(
    private _userService:UsuarioService,
    private _route: ActivatedRoute,
    private _router: Router
  ){
  }
  ngDoCheck(): void {
    try {
      this.identity = this._userService.getIndentity();
    } catch (error) {
      this.identity = "";
    }
  }

  ngOnInit(){
    this.identity = this._userService.getIndentity();
  }


  logout(){
    localStorage.removeItem('identity');
    localStorage.removeItem('token');
    this._router.navigate(['/login']);
  }
}
