<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>签收留档查询</span>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="关键词">
            <el-input v-model="queryParams.keyword" placeholder="领取人/菜品名称" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px">
              <el-option label="正常" :value="1" />
              <el-option label="已作废" :value="2" />
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
        <el-table-column prop="claimRecordId" label="领取记录ID" width="120" />
        <el-table-column prop="recipientName" label="领取人" width="120" />
        <el-table-column prop="foodName" label="菜品名称" min-width="150" />
        <el-table-column prop="noticeVersion" label="告知书版本" width="120" />
        <el-table-column prop="signType" label="签收类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.signType === '电子签名' ? 'primary' : 'success'">{{ row.signType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险确认" width="180">
          <template #default="{ row }">
            <div class="risk-flags">
              <el-tag :type="row.isRead ? 'success' : 'danger'" size="small">已阅读</el-tag>
              <el-tag :type="row.isUnderstood ? 'success' : 'danger'" size="small">已理解</el-tag>
              <el-tag :type="row.isAccepted ? 'success' : 'danger'" size="small">已接受</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="signTime" label="签收时间" width="160" />
        <el-table-column prop="signLocation" label="签收地点" width="150" show-overflow-tooltip />
        <el-table-column prop="witnessName" label="见证人" width="100" />
        <el-table-column prop="archiveStatus" label="存档状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.archiveStatus === 1 ? 'success' : 'info'">
              {{ row.archiveStatus === 1 ? '正常' : '已作废' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">详情</el-button>
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

    <el-dialog v-model="detailDialogVisible" title="签收档案详情" width="900px">
      <div v-if="detailData">
        <el-steps :active="3" finish-status="success" style="margin-bottom: 30px">
          <el-step title="阅读告知书" :description="detailData.isRead ? '已完成' : '未完成'" />
          <el-step title="理解风险" :description="detailData.isUnderstood ? '已完成' : '未完成'" />
          <el-step title="接受条款" :description="detailData.isAccepted ? '已完成' : '未完成'" />
          <el-step title="签字确认" :description="detailData.signTime" />
        </el-steps>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="档案编号">{{ detailData.id }}</el-descriptions-item>
          <el-descriptions-item label="关联领取记录">{{ detailData.claimRecordId }}</el-descriptions-item>
          <el-descriptions-item label="领取人">{{ detailData.recipientName }}</el-descriptions-item>
          <el-descriptions-item label="菜品名称">{{ detailData.foodName }}</el-descriptions-item>
          <el-descriptions-item label="风险告知书版本">{{ detailData.noticeVersion }}</el-descriptions-item>
          <el-descriptions-item label="签收类型">
            <el-tag :type="detailData.signType === '电子签名' ? 'primary' : 'success'">
              {{ detailData.signType }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="签收时间">{{ detailData.signTime }}</el-descriptions-item>
          <el-descriptions-item label="签收地点">{{ detailData.signLocation || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签收IP">{{ detailData.signIp || '-' }}</el-descriptions-item>
          <el-descriptions-item label="存档状态">
            <el-tag :type="detailData.archiveStatus === 1 ? 'success' : 'info'">
              {{ detailData.archiveStatus === 1 ? '正常' : '已作废' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="见证人">{{ detailData.witnessName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="见证人电话">{{ detailData.witnessPhone || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">风险告知书内容（存档快照）</el-divider>
        <el-card class="notice-card">
          <div class="notice-content" v-html="formatNotice(detailData.noticeContentSnapshot)"></div>
        </el-card>

        <el-divider content-position="left" v-if="detailData.signatureData || detailData.signatureImage || detailData.paperSignImage">
          签名凭证
        </el-divider>
        <el-row :gutter="20" v-if="detailData.signatureData || detailData.signatureImage || detailData.paperSignImage">
          <el-col :span="8" v-if="detailData.signatureImage">
            <el-card>
              <template #header>电子签名图片</template>
              <el-image :src="detailData.signatureImage" style="width: 100%; height: 150px" fit="contain" />
            </el-card>
          </el-col>
          <el-col :span="8" v-if="detailData.paperSignImage">
            <el-card>
              <template #header>纸质签字照片</template>
              <el-image :src="detailData.paperSignImage" style="width: 100%; height: 150px" fit="contain" />
            </el-card>
          </el-col>
          <el-col :span="8" v-if="detailData.signatureData && !detailData.signatureImage">
            <el-card>
              <template #header>签名数据</template>
              <el-input :value="detailData.signatureData.substring(0, 100) + '...'" type="textarea" :rows="6" readonly />
            </el-card>
          </el-col>
        </el-row>

        <el-divider content-position="left" v-if="detailData.remark">备注</el-divider>
        <el-alert v-if="detailData.remark" :title="detailData.remark" type="info" :closable="false" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getArchivePage, getArchiveById } from '@/api/risk'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const detailDialogVisible = ref(false)
const detailData = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null
})

const formatNotice = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br/>')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getArchivePage(queryParams)
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

const handleView = async (row) => {
  try {
    detailData.value = await getArchiveById(row.id)
    detailDialogVisible.value = true
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

.risk-flags {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.notice-card {
  margin-bottom: 20px;
}

.notice-content {
  max-height: 300px;
  overflow-y: auto;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}
</style>
