import { Routes } from '@angular/router';

import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Home } from './pages/home/home';
import { Electronics } from './pages/electronics/electronics';
import { HealthBeauty } from './pages/health-beauty/health-beauty';
import { MainLayout } from './layout/main-layout/main-layout';
import { authGuard } from './guards/auth-guard';
import { CreateProduct } from './pages/create-product/create-product';
import { EditProduct } from './pages/edit-product/edit-product';
import { CartComponent } from './cart/cart';

export const routes: Routes = [

  {
    path: 'login',
    component: Login
  },

  {
    path: 'register',
    component: Register
  },

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },

  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [

      {
        path: 'home',
        component: Home
      },

      {
        path: 'electronics',
        component: Electronics
      },

      {
        path: 'health-beauty',
        component: HealthBeauty
      },

      {
        path: 'create-product',
        component: CreateProduct
      },

      {
        path: 'edit-product/:id',
        component: EditProduct
      },

      {
        path: 'cart',
        component: CartComponent
      }

    ]
  },

  {
    path: '**',
    redirectTo: 'home'
  }

];