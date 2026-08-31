import { Component } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';

import { Router, RouterLink } from '@angular/router';

import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',

  imports: [
    ReactiveFormsModule,
    RouterLink
  ],

  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: Auth,
    private router: Router
  ) {

    this.loginForm = this.formBuilder.group({

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        '',
        Validators.required
      ]

    });

  }


  login(): void {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;
    }


    console.log('LOGIN FORM SUBMITTED');

    console.log(this.loginForm.value);


    this.authService
      .login(this.loginForm.value)
      .subscribe({

        next: (response) => {

          console.log('LOGIN SUCCESSFUL');

          console.log(response);


          localStorage.setItem(
            'token',
            response.token
          );

          localStorage.setItem(
            'role', 
            response.role
          );

          localStorage.setItem(
            'userId',
            response.userId
          );


          localStorage.setItem(
            'firstName',
            response.firstName
          );


          localStorage.setItem(
            'email',
            response.email
          );


          this.router.navigate(['/']);

        },


        error: (error) => {

          console.error('LOGIN FAILED');

          console.error(error);

        }

      });

  }

}