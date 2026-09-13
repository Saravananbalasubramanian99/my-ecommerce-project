import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-oauth-success',
  standalone: true,
  templateUrl: './oauth-success.html',
  styleUrl: './oauth-success.css'
})
export class OauthSuccess implements OnInit {

  constructor(private router: Router) {}

  ngOnInit(): void {

    const hash = window.location.hash;

    if (!hash) {
      console.log('No OAuth token found');
      this.router.navigate(['/login']);
      return;
    }

    const params = new URLSearchParams(hash.substring(1));

    const token = params.get('token');
    const userId = params.get('userId');
    const firstName = params.get('firstName');
    const email = params.get('email');
    const role = params.get('role');

    if (!token || !userId) {
      console.log('OAuth login information is incomplete');
      this.router.navigate(['/login']);
      return;
    }

    // Store the same information as normal login
    localStorage.setItem('token', token);
    localStorage.setItem('userId', userId);

    if (firstName) {
      localStorage.setItem('firstName', firstName);
    }

    if (email) {
      localStorage.setItem('email', email);
    }

    if (role) {
      localStorage.setItem('role', role);
    }

    console.log('Google login successful');

    // Go to home page
    this.router.navigate(['/']);
  }
}