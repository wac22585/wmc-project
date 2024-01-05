import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios';

const isAuthenticated = async  () => {
  try {
    const token = localStorage.getItem('authToken');
    if(token == null) return false;
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
  },
  {
    path: '/:catchAll(.*)',
    name: 'notFound',
    component: () => import('@/views/LoginView.vue'),
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach(async (to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth) {
    const authStatus = await isAuthenticated();
    if (!authStatus) {
      next({ name: 'Login' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
