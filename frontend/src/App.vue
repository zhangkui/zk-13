<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <div class="header-left">
        <el-icon size="32" color="#fff"><Food /></el-icon>
        <h1 class="title">宴席余餐再分配登记与风险留痕系统</h1>
      </div>
      <div class="header-right">
        <span class="user-info">管理员</span>
      </div>
    </el-header>
    <el-container>
      <el-aside width="220px" class="app-aside">
        <el-menu
          :default-active="$route.path"
          router
          background-color="#001529"
          text-color="#fff"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/food-category">
            <el-icon><Folder /></el-icon>
            <span>余餐分类管理</span>
          </el-menu-item>
          <el-menu-item index="/leftover-food">
            <el-icon><Dish /></el-icon>
            <span>余餐登记管理</span>
          </el-menu-item>
          <el-menu-item index="/recipient">
            <el-icon><User /></el-icon>
            <span>领取人管理</span>
          </el-menu-item>
          <el-menu-item index="/claim-record">
            <el-icon><Tickets /></el-icon>
            <span>领取记录追踪</span>
          </el-menu-item>
          <el-menu-item index="/risk-notice">
            <el-icon><Warning /></el-icon>
            <span>风险告知管理</span>
          </el-menu-item>
          <el-menu-item index="/sign-archive">
            <el-icon><Document /></el-icon>
            <span>签收留档查询</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  width: 100%;
}

.app-container {
  height: 100%;
}

.app-header {
  background: linear-gradient(90deg, #001529 0%, #0050b3 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  height: 70px !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.title {
  font-size: 22px;
  font-weight: 600;
  margin: 0;
  letter-spacing: 2px;
}

.user-info {
  font-size: 14px;
  color: rgba(255,255,255,0.85);
}

.app-aside {
  background-color: #001529;
  height: calc(100vh - 70px);
  overflow-y: auto;
}

.app-aside .el-menu {
  border-right: none;
  height: 100%;
}

.app-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
  height: calc(100vh - 70px);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
