<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>风险告知管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新增版本
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="noticeTitle" label="标题" min-width="200" />
        <el-table-column prop="noticeVersion" label="版本号" width="120" />
        <el-table-column label="是否当前版本" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.isCurrent === 1" type="success" effect="dark">当前版本</el-tag>
            <el-tag v-else type="info">历史版本</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="expiryDate" label="失效日期" width="120" />
        <el-table-column prop="createBy" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" size="small" @click="handleSetCurrent(row)" v-if="row.isCurrent !== 1">
              设为当前
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)" v-if="row.isCurrent !== 1">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入告知书标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="noticeVersion">
              <el-input v-model="form.noticeVersion" placeholder="如：V1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="form.effectiveDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期">
              <el-date-picker v-model="form.expiryDate" type="date" placeholder="选择日期（可选）" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人">
              <el-input v-model="form.createBy" placeholder="请输入创建人" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="风险条款">
              <el-input v-model="form.riskItems" type="textarea" :rows="3" placeholder='请输入风险条款JSON数组，如：[{"itemCode":"RISK001","itemContent":"风险内容","level":"高"}]' />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="告知内容" prop="noticeContent">
              <el-input v-model="form.noticeContent" type="textarea" :rows="12" placeholder="请输入风险告知书详细内容" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="风险告知书预览" width="800px">
      <div v-if="viewData">
        <div class="preview-header">
          <h2>{{ viewData.noticeTitle }}</h2>
          <div class="preview-meta">
            <span>版本：{{ viewData.noticeVersion }}</span>
            <span v-if="viewData.isCurrent === 1" style="color: #67C23A; font-weight: 600">（当前版本）</span>
          </div>
        </div>
        <el-divider />
        <div class="preview-content" v-html="formatNotice(viewData.noticeContent)"></div>
        <el-divider v-if="viewData.riskItems" />
        <div v-if="viewData.riskItems">
          <h4>风险条款明细</h4>
          <el-table :data="parseRiskItems(viewData.riskItems)" border size="small">
            <el-table-column prop="itemCode" label="条款编码" width="120" />
            <el-table-column prop="itemContent" label="风险内容" min-width="300" />
            <el-table-column prop="level" label="风险等级" width="100">
              <template #default="{ row }">
                <el-tag :type="getRiskLevelType(row.level)">{{ row.level }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoticeList, addNotice, updateNotice, setCurrentNotice, deleteNotice } from '@/api/risk'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增版本')
const viewDialogVisible = ref(false)
const formRef = ref()
const isEdit = ref(false)
const viewData = ref(null)

const form = reactive({
  id: null,
  noticeTitle: '',
  noticeContent: '',
  noticeVersion: '',
  isCurrent: 0,
  effectiveDate: null,
  expiryDate: null,
  riskItems: '',
  createBy: '',
  remark: ''
})

const rules = {
  noticeTitle: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  noticeVersion: [{ required: true, message: '请输入版本号', trigger: 'blur' }],
  noticeContent: [{ required: true, message: '请输入告知内容', trigger: 'blur' }],
  effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }]
}

const formatNotice = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br/>')
}

const parseRiskItems = (items) => {
  try {
    return JSON.parse(items)
  } catch {
    return []
  }
}

const getRiskLevelType = (level) => {
  const types = { '高': 'danger', '中': 'warning', '低': 'info' }
  return types[level] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await getNoticeList()
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    noticeTitle: '',
    noticeContent: '',
    noticeVersion: '',
    isCurrent: 0,
    effectiveDate: null,
    expiryDate: null,
    riskItems: '',
    createBy: '',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增版本'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑版本'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  viewData.value = row
  viewDialogVisible.value = true
}

const handleSetCurrent = async (row) => {
  try {
    await ElMessageBox.confirm('确定要将此版本设为当前生效版本吗？', '提示', { type: 'warning' })
    await setCurrentNotice(row.id)
    ElMessage.success('设置成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该版本吗？', '提示', { type: 'warning' })
    await deleteNotice(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await updateNotice(form)
      ElMessage.success('更新成功')
    } else {
      await addNotice(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
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

.preview-header {
  text-align: center;
  margin-bottom: 20px;
}

.preview-header h2 {
  margin-bottom: 10px;
  color: #303133;
}

.preview-meta {
  color: #909399;
  font-size: 14px;
}

.preview-content {
  line-height: 2;
  color: #606266;
  white-space: pre-wrap;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.preview-content h4 {
  color: #303133;
  margin: 15px 0 10px 0;
}
</style>
