import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../../core/services/authentication/authentication.service';
import { FormPatterns } from '../../../../core/utils/interfaces/generals.interfaces';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';
import { JwtDecodeUser } from '../../../../core/models/authentication/auth.interface';
import { NotificationService } from '../../../../core/services/notification/notification.service';

@Component({
  selector: 'app-form-login',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.css',
})
export class FormLoginComponent {
  validateForm!: FormGroup;
  formPattern = FormPatterns;
  passwordVisible: boolean = false;
  loadingLogin: boolean = false;

  numDocumentTouched: boolean = false;
  passwordTouched: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthenticationService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.validateForm = this.formBuilder.group({
      numDocument: [
        '',
        [
          Validators.required,
          Validators.minLength(7),
          Validators.maxLength(10),
          Validators.pattern(this.formPattern._NUM_IDENTIFICACION_PATTERN),
        ],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(15),
        ],
      ],
    });
  }

  isValidForm() {
    if (this.validateForm.valid) {
      this.signinWithIdentificationAndPassword();
    } else if (this.validateForm.invalid) {
      this.showErrorTips();
    }
  }

  /**
   *   Realiza el inicio de sesion con el numero de documento y la contraseña
   */
  signinWithIdentificationAndPassword() {
    this.loadingLogin = true;
    const body = {
      document: this.validateForm.controls['numDocument'].value,
      password: this.validateForm.controls['password'].value,
    };
  
    this.authService.signinWhithIdentificationAndPassword(body).subscribe(
      (res) => {
        if (res && res.access_token) {
          this.authService.setToken(res.access_token);
          const infoUser: JwtDecodeUser = this.authService.getInformationUserAuth();
          this.router.navigate(['/']);
          const message = "Bienvenido a " + infoUser.business_id;
          //this.notificationService.success(message);
        }
        this.loadingLogin = false;
      },
      (error) => {
        let message = 'Ocurrió un error al iniciar sesión, por favor intente nuevamente';
        if (error?.error?.detail && error.status >= 400 && error.status < 499) {
          message = "Las credenciales proporcionadas son incorrectas.";
        }
        this.loadingLogin = false;
        this.notificationService.error(message, 'Error');
      }
    );
  }
  

  showErrorTips() {
    this.validateForm.controls['numDocument'].markAllAsTouched();
    this.validateForm.controls['numDocument'].updateValueAndValidity();
    this.validateForm.controls['password'].markAllAsTouched();
    this.validateForm.controls['password'].updateValueAndValidity();
  }

  /**
   * Marca como touched los campos del formulario para activar los error tips
   * @param controlName : Es el nombre del control del formulario
   */
  markTouched(controlName: string) {
    if (controlName) {
      this.validateForm.controls[controlName].markAllAsTouched();
      this.validateForm.controls[controlName].updateValueAndValidity();
    }
    if (controlName === 'numDocument') {
      this.numDocumentTouched = false;
    } else if (controlName === 'password') {
      this.passwordTouched = false;
    }
  }
}
