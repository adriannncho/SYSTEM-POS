import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FormPatterns } from '../../../../core/utils/interfaces/generals.interfaces';
import { AuthenticationService } from '../../../../core/services/authentication/authentication.service';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';
import { FormLoginComponent } from '../../components/form-login/form-login.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormLoginComponent,
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

}
