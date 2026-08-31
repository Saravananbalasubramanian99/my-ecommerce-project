import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {
  }

  register(userData: any): Observable<any> {

    return this.http.post(           // which sends the http request to Spring Boot.
      `${this.apiUrl}/register`,
      userData                       // JSON body
    );

  }

  login(credentials: any): Observable<any> {

    return this.http.post(
      `${this.apiUrl}/login`,
      credentials
    );

  }

  getRole(): string | null {

    return localStorage.getItem('role');

  }

  isAdmin(): boolean {

    return this.getRole() === 'ADMIN';

  }

  isUser(): boolean {

    return this.getRole() === 'USER';

  }

}