// Composables
import { createRouter, createWebHistory } from 'vue-router'

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
    beforeEnter: (to, from, next) => {
      const isAuthenticated = true;
      if (isAuthenticated) {
        next();
      } else {
        next({ name: 'Login' });
      }
    },
  },
  {
    path: '/add',
    name: 'Add',
    component: () => import('@/views/AddView.vue'),
  }
];


const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
