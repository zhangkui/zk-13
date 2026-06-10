import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '数据仪表盘' }
  },
  {
    path: '/food-category',
    name: 'FoodCategory',
    component: () => import('@/views/FoodCategory.vue'),
    meta: { title: '余餐分类管理' }
  },
  {
    path: '/leftover-food',
    name: 'LeftoverFood',
    component: () => import('@/views/LeftoverFood.vue'),
    meta: { title: '余餐登记管理' }
  },
  {
    path: '/recipient',
    name: 'Recipient',
    component: () => import('@/views/Recipient.vue'),
    meta: { title: '领取人管理' }
  },
  {
    path: '/claim-record',
    name: 'ClaimRecord',
    component: () => import('@/views/ClaimRecord.vue'),
    meta: { title: '领取记录追踪' }
  },
  {
    path: '/risk-notice',
    name: 'RiskNotice',
    component: () => import('@/views/RiskNotice.vue'),
    meta: { title: '风险告知管理' }
  },
  {
    path: '/sign-archive',
    name: 'SignArchive',
    component: () => import('@/views/SignArchive.vue'),
    meta: { title: '签收留档查询' }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 宴席余餐再分配系统` : '宴席余餐再分配登记与风险留痕系统'
  next()
})

export default router
