import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';

import { Auth } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {

  constructor(private router: Router, private authService: Auth) {
  }

  isAdmin(): boolean {

    return this.authService.isAdmin();

  }

  logout(): void {

    localStorage.removeItem('token');

    this.router.navigate(['/login']);

  }

}