<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>领取记录追踪</span>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="关键词">
            <el-input v-model="queryParams.keyword" placeholder="菜品/领取人" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px">
              <el-option label="运输中" :value="1" />
              <el-option label="已送达" :value="2" />
              <el-option label="已使用" :value="3" />
              <el-option label="异常" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadData">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="foodName" label="菜品名称" min-width="150" />
        <el-table-column prop="recipientName" label="领取人" width="120" />
        <el-table-column prop="recipientPhone" label="联系电话" width="130" />
        <el-table-column label="领取数量" width="120">
          <template #default="{ row }">
            {{ row.claimQuantity }}{{ row.quantityUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="claimTime" label="领取时间" width="160" />
        <el-table-column prop="claimPurpose" label="领取用途" width="120" show-overflow-tooltip />
        <el-table-column prop="destination" label="去向地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="transportMethod" label="运输方式" width="120" />
        <el-table-column prop="expectedArrivalTime" label="预计送达" width="160" />
        <el-table-column prop="actualArrivalTime" label="实际送达" width="160" />
        <el-table-column prop="usageStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.usageStatus)">
              {{ getStatusText(row.usageStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">详情</el-button>
            <el-button type="success" size="small" @click="handleUpdateStatus(row, 2)" v-if="row.usageStatus === 1">
              确认送达
            </el-button>
            <el-button type="warning" size="small" @click="handleUpdateStatus(row, 3)" v-if="row.usageStatus === 2">
              标记使用
            </el-button>
            <el-button type="danger" size="small" @click="handleUpdateStatus(row, 4)" v-if="row.usageStatus < 4">
              标记异常
            </el-button>
            <el-button type="info" size="small" @click="handleViewArchive(row)">签收记录</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="领取记录详情" width="700px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="菜品名称">{{ detailData.foodName }}</el-descriptions-item>
        <el-descriptions-item label="领取人">{{ detailData.recipientName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.recipientPhone }}</el-descriptions-item>
        <el-descriptions-item label="领取数量">
          {{ detailData.claimQuantity }}{{ detailData.quantityUnit }}
        </el-descriptions-item>
        <el-descriptions-item label="领取时间" :span="2">{{ detailData.claimTime }}</el-descriptions-item>
        <el-descriptions-item label="领取用途">{{ detailData.claimPurpose || '-' }}</el-descriptions-item>
        <el-descriptions-item label="运输方式">{{ detailData.transportMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="去向地址" :span="2">{{ detailData.destination }}</el-descriptions-item>
        <el-descriptions-item label="预计送达时间">{{ detailData.expectedArrivalTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际送达时间">{{ detailData.actualArrivalTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经办人">{{ detailData.handler || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailData.usageStatus)">
            {{ getStatusText(detailData.usageStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="使用反馈" :span="2">{{ detailData.usageFeedback || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="statusDialogVisible" title="更新状态" width="500px">
      <el-form :model="statusForm" label-width="100px">
        <el-form-item label="状态">
          <el-tag :type="getStatusType(statusForm.status)">
            {{ getStatusText(statusForm.status) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="反馈说明" v-if="statusForm.status === 4">
          <el-input v-model="statusForm.feedback" type="textarea" :rows="3" placeholder="请说明异常情况" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStatusUpdate">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="archiveDialogVisible" title="签收留档详情" width="800px">
      <div v-if="archiveData">
        <el-alert
          :title="`风险告知书（版本：${archiveData.noticeVersion}）`"
          type="warning"
          :closable="false"
          style="margin-bottom: 20px"
        />
        <el-card class="notice-card">
          <div class="notice-content" v-html="formatNotice(archiveData.noticeContentSnapshot)"></div>
        </el-card>
        <el-descriptions :column="2" border style="margin-top: 20px">
          <el-descriptions-item label="签收人">{{ archiveData.recipientName }}</el-descriptions-item>
          <el-descriptions-item label="菜品名称">{{ archiveData.foodName }}</el-descriptions-item>
          <el-descriptions-item label="签收类型">{{ archiveData.signType }}</el-descriptions-item>
          <el-descriptions-item label="签收时间">{{ archiveData.signTime }}</el-descriptions-item>
          <el-descriptions-item label="签收地点">{{ archiveData.signLocation || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签收IP">{{ archiveData.signIp || '-' }}</el-descriptions-item>
          <el-descriptions-item label="是否已阅读">
            <el-tag :type="archiveData.isRead ? 'success' : 'danger'">
              {{ archiveData.isRead ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否已理解">
            <el-tag :type="archiveData.isUnderstood ? 'success' : 'danger'">
              {{ archiveData.isUnderstood ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否接受风险" :span="2">
            <el-tag :type="archiveData.isAccepted ? 'success' : 'danger'">
              {{ archiveData.isAccepted ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="见证人">{{ archiveData.witnessName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="见证人电话">{{ archiveData.witnessPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="存档状态" :span="2">
            <el-tag :type="archiveData.archiveStatus === 1 ? 'success' : 'info'">
              {{ archiveData.archiveStatus === 1 ? '正常' : '已作废' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="签名图片" :span="2" v-if="archiveData.signatureImage">
            <el-image :src="archiveData.signatureImage" style="width: 200px; height: 100px" fit="contain" />
          </el-descriptions-item>
          <el-descriptions-item label="纸质签字照片" :span="2" v-if="archiveData.paperSignImage">
            <el-image :src="archiveData.paperSignImage" style="width: 300px; height: 200px" fit="contain" />
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else description="暂无签收记录" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getClaimPage, updateClaimStatus } from '@/api/claim'
import { getArchiveByClaimId } from '@/api/risk'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const detailDialogVisible = ref(false)
const statusDialogVisible = ref(false)
const archiveDialogVisible = ref(false)
const detailData = ref(null)
const archiveData = ref(null)
const currentRecordId = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null,
  foodId: null,
  recipientId: null
})

const statusForm = reactive({
  status: null,
  feedback: ''
})

const getStatusType = (status) => {
  const types = { 1: 'primary', 2: 'success', 3: 'warning', 4: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 1: '运输中', 2: '已送达', 3: '已使用', 4: '异常' }
  return texts[status] || '未知'
}

const formatNotice = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br/>')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getClaimPage(queryParams)
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.status = null
  queryParams.pageNum = 1
  loadData()
}

const handleView = (row) => {
  detailData.value = row
  detailDialogVisible.value = true
}

const handleUpdateStatus = (row, status) => {
  currentRecordId.value = row.id
  statusForm.status = status
  statusForm.feedback = ''
  statusDialogVisible.value = true
}

const submitStatusUpdate = async () => {
  try {
    await updateClaimStatus(currentRecordId.value, statusForm.status, statusForm.feedback)
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleViewArchive = async (row) => {
  try {
    archiveData.value = await getArchiveByClaimId(row.id)
    archiveDialogVisible.value = true
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.notice-card {
  margin-bottom: 20px;
}

.notice-content {
  max-height: 200px;
  overflow-y: auto;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}
</style>
