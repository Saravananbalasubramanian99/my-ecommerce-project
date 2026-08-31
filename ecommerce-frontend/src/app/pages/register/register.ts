import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,          //for form controls
    RouterLink                    //for navigation
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  registerForm: FormGroup;

  constructor(private formBuilder: FormBuilder,   //Used to create reactive forms.
              private authService: Auth           //Used to send data to your backend 
  ) {

    this.registerForm = this.formBuilder.group({
      //Angular creates a FormGroup with FormControls and Validators
      firstName: [
        '',
        Validators.required
      ],

      lastName: [
        '',
        Validators.required
      ],

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6)
        ]
      ]

    });
  }

  register(): void {

  console.log('REGISTER BUTTON CLICKED');

  console.log('FORM DATA:');
  console.log(this.registerForm.value);

  console.log('FORM VALID:');
  console.log(this.registerForm.valid);

  if (this.registerForm.invalid) {

    console.log('FORM IS INVALID');

    this.registerForm.markAllAsTouched();

    return;
  }

  console.log('CALLING AUTH SERVICE...');

  this.authService
    .register(this.registerForm.value)
    .subscribe({

      next: (response) => {                     //Angular waits for Spring Boot to respond.

        console.log('REGISTRATION SUCCESSFULL');

        console.log(response);

      },

      error: (error) => {

        console.log('REGISTRATION FAILED');

        console.error(error);

      }

    });
}
}
