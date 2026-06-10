<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item, index) in statCards" :key="index">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: item.color }">
              <el-icon :size="32" color="#fff"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">{{ item.label }}</div>
              <div class="stat-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>余餐状态分布</span>
            </div>
          </template>
          <v-chart class="chart" :option="foodStatusOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>领取状态分布</span>
            </div>
          </template>
          <v-chart class="chart" :option="claimStatusOption" autoresize />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import { getDashboardStats } from '@/api/risk'
import {
  Dish,
  User,
  Tickets,
  Document,
  CircleCheck,
  Van
} from '@element-plus/icons-vue'

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
])

const stats = ref({})

const statCards = ref([
  { label: '余餐登记总数', value: 0, icon: Dish, color: '#409EFF' },
  { label: '待领取数量', value: 0, icon: CircleCheck, color: '#67C23A' },
  { label: '已领取数量', value: 0, icon: Van, color: '#E6A23C' },
  { label: '领取人总数', value: 0, icon: User, color: '#909399' },
  { label: '活跃领取人', value: 0, icon: User, color: '#F56C6C' },
  { label: '领取记录总数', value: 0, icon: Tickets, color: '#8E44AD' },
  { label: '运输中', value: 0, icon: Van, color: '#3498DB' },
  { label: '已送达', value: 0, icon: CircleCheck, color: '#2ECC71' },
  { label: '签收档案总数', value: 0, icon: Document, color: '#1ABC9C' }
])

const foodStatusOption = ref({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [
    {
      name: '余餐状态',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false, position: 'center' },
      emphasis: {
        label: { show: true, fontSize: 20, fontWeight: 'bold' }
      },
      labelLine: { show: false },
      data: [
        { value: 0, name: '待领取', itemStyle: { color: '#67C23A' } },
        { value: 0, name: '已领取', itemStyle: { color: '#E6A23C' } },
        { value: 0, name: '已过期', itemStyle: { color: '#F56C6C' } },
        { value: 0, name: '已销毁', itemStyle: { color: '#909399' } }
      ]
    }
  ]
})

const claimStatusOption = ref({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [
    {
      name: '领取状态',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false, position: 'center' },
      emphasis: {
        label: { show: true, fontSize: 20, fontWeight: 'bold' }
      },
      labelLine: { show: false },
      data: [
        { value: 0, name: '运输中', itemStyle: { color: '#3498DB' } },
        { value: 0, name: '已送达', itemStyle: { color: '#2ECC71' } },
        { value: 0, name: '已使用', itemStyle: { color: '#27AE60' } },
        { value: 0, name: '异常', itemStyle: { color: '#E74C3C' } }
      ]
    }
  ]
})

const loadStats = async () => {
  try {
    stats.value = await getDashboardStats()
    statCards.value[0].value = stats.value.totalFood
    statCards.value[1].value = stats.value.availableFood
    statCards.value[2].value = stats.value.claimedFood
    statCards.value[3].value = stats.value.totalRecipient
    statCards.value[4].value = stats.value.activeRecipient
    statCards.value[5].value = stats.value.totalClaim
    statCards.value[6].value = stats.value.inTransit
    statCards.value[7].value = stats.value.delivered
    statCards.value[8].value = stats.value.totalArchive

    foodStatusOption.value.series[0].data[0].value = stats.value.availableFood
    foodStatusOption.value.series[0].data[1].value = stats.value.claimedFood

    claimStatusOption.value.series[0].data[0].value = stats.value.inTransit
    claimStatusOption.value.series[0].data[1].value = stats.value.delivered
    claimStatusOption.value.series[0].data[2].value = stats.value.used
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.stat-card {
  border-radius: 8px;
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.chart {
  height: 300px;
  width: 100%;
}
</style>
