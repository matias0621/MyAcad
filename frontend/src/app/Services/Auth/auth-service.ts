import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient,
    private router: Router
  ) { }

  getToken() {
    return localStorage.getItem('token');
  }

  getDecodedToken() {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwtDecode(token);
    } catch {
      return null;
    }
  }

  getRole() {
    const decoded: any = this.getDecodedToken();
    return decoded ? decoded.role : null;
  }

  login(data: { legajo: string, password: string }) {
    return this.http.post(`${this.apiUrl}/login`, data);
  }

  register(data: any) {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/auth/login']);
  }

  changePassword(data: { currentPassword: string; newPassword: string }) {
    return this.http.post(`${this.apiUrl}/changePassword`, data);
  }
}
