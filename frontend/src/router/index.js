import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios';

const getUserAuthStatus  = async  () => {
  /*try {
    const response = await axios.get("/users/validateToken", {
      withCredentials: true
    });
    const isAdmin = response.data.isAdmin;
    return {
      isAuthenticated: response.status === 200,
      isAdmin: isAdmin
    };*/
    return {
      isAuthenticated: true,
      isAdmin: true,
    };
  /*} catch (error) {
    return {
      isAuthenticated: false,
      isAdmin: false
    };
  }*/
}

const routes = [
  {
    path: '/',
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
  const userAuthStatus = await getUserAuthStatus();

  if (requiresAuth) {
    if (!userAuthStatus.isAuthenticated) {
      next({ name: 'Login' });
    } else {
      if (userAuthStatus.isAdmin || to.path === '/account') {
        next();
      } else {
        next({ name: 'Account' });
      }
    }
  } else {
    next();
  }
});

export default router
