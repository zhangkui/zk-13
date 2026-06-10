<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>领取人管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新增领取人
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="关键词">
            <el-input v-model="queryParams.keyword" placeholder="姓名/手机号" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="queryParams.recipientType" placeholder="全部" clearable style="width: 150px">
              <el-option label="个人" value="个人" />
              <el-option label="机构" value="机构" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px">
              <el-option label="待审核" :value="0" />
              <el-option label="正常" :value="1" />
              <el-option label="黑名单" :value="2" />
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
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.recipientType === '个人' ? 'primary' : 'success'" size="small">
              {{ row.recipientType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名/机构名称" min-width="150" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="idCard" label="身份证号" width="180" v-if="false" />
        <el-table-column prop="address" label="联系地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="totalClaimed" label="累计领取" width="100" />
        <el-table-column prop="lastClaimTime" label="最后领取" width="160" />
        <el-table-column prop="recipientStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.recipientStatus)">
              {{ getStatusText(row.recipientStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-dropdown @command="(cmd) => handleStatusChange(row, cmd)" v-if="row.recipientStatus !== 2">
              <el-button type="danger" size="small">
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="2">加入黑名单</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button type="success" size="small" @click="handleStatusChange(row, 1)" v-if="row.recipientStatus === 2">
              解除黑名单
            </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型" prop="recipientType">
              <el-radio-group v-model="form.recipientType">
                <el-radio label="个人">个人</el-radio>
                <el-radio label="机构">机构</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.recipientStatus">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">待审核</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="form.recipientType === '个人' ? '姓名' : '机构名称'" prop="name">
              <el-input v-model="form.name" :placeholder="form.recipientType === '个人' ? '请输入姓名' : '请输入机构名称'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.recipientType === '个人'">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.recipientType === '机构'">
            <el-form-item label="机构代码">
              <el-input v-model="form.orgCode" placeholder="请输入机构代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱">
              <el-input v-model="form.email" placeholder="请输入电子邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系地址">
              <el-input v-model="form.address" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急联系人">
              <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急联系电话">
              <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
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

    <el-dialog v-model="viewDialogVisible" title="领取人详情" width="700px">
      <el-descriptions :column="2" border v-if="viewData">
        <el-descriptions-item label="类型">
          <el-tag :type="viewData.recipientType === '个人' ? 'primary' : 'success'">
            {{ viewData.recipientType }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.recipientStatus)">
            {{ getStatusText(viewData.recipientStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item :label="viewData.recipientType === '个人' ? '姓名' : '机构名称'">
          {{ viewData.name }}
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewData.phone }}</el-descriptions-item>
        <el-descriptions-item label="身份证号" v-if="viewData.recipientType === '个人'">
          {{ viewData.idCard }}
        </el-descriptions-item>
        <el-descriptions-item label="机构代码" v-if="viewData.recipientType === '机构'">
          {{ viewData.orgCode }}
        </el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{ viewData.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系地址" :span="2">{{ viewData.address }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ viewData.emergencyContact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系电话">{{ viewData.emergencyPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="累计领取次数">{{ viewData.totalClaimed || 0 }}</el-descriptions-item>
        <el-descriptions-item label="最后领取时间">{{ viewData.lastClaimTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRecipientPage, addRecipient, updateRecipient, updateRecipientStatus, deleteRecipient } from '@/api/recipient'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增领取人')
const viewDialogVisible = ref(false)
const formRef = ref()
const isEdit = ref(false)
const viewData = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  recipientType: null,
  status: null
})

const form = reactive({
  id: null,
  recipientType: '个人',
  name: '',
  idCard: '',
  orgCode: '',
  phone: '',
  email: '',
  address: '',
  emergencyContact: '',
  emergencyPhone: '',
  idCardFront: '',
  idCardBack: '',
  orgCertificate: '',
  recipientStatus: 1,
  remark: ''
})

const rules = {
  recipientType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待审核', 1: '正常', 2: '黑名单' }
  return texts[status] || '未知'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getRecipientPage(queryParams)
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.recipientType = null
  queryParams.status = null
  queryParams.pageNum = 1
  loadData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    recipientType: '个人',
    name: '',
    idCard: '',
    orgCode: '',
    phone: '',
    email: '',
    address: '',
    emergencyContact: '',
    emergencyPhone: '',
    idCardFront: '',
    idCardBack: '',
    orgCertificate: '',
    recipientStatus: 1,
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增领取人'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑领取人'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  viewData.value = row
  viewDialogVisible.value = true
}

const handleStatusChange = async (row, status) => {
  const msg = status === 2 ? '确定要将该领取人加入黑名单吗？' : '确定要解除该领取人的黑名单吗？'
  try {
    await ElMessageBox.confirm(msg, '提示', { type: 'warning' })
    await updateRecipientStatus(row.id, status)
    ElMessage.success('操作成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await updateRecipient(form)
      ElMessage.success('更新成功')
    } else {
      await addRecipient(form)
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

.search-bar {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
