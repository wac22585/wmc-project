import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios';

const isAuthenticated = async  () => {
  try {
    const token = localStorage.getItem('autoToken');
    const response = await axios.get("/users/validateToken", {
      params: {
        authToken: token
      }
    });
    return response.status === 200;
  } catch (error) {
    return false;
  }
}

const routes = [
  {
    path: '',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/add',
    name: 'Add',
    component: () => import('@/views/AddView.vue'),
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/edit/:id',
    name: 'Edit',
    component: () => import('@/views/EditView.vue'),
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/account',
    name: 'Account',
    component: () => import('@/views/AccountView.vue'),
    meta: {
      requiresAuth: true,
    }
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
    next({name: 'Login'});
  } else {
    next();
  }
})

export default router
