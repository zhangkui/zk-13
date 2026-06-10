<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>余餐登记管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新增登记
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="关键词">
            <el-input v-model="queryParams.keyword" placeholder="菜品名称/宴席类型" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px">
              <el-option label="待领取" :value="1" />
              <el-option label="已领取" :value="2" />
              <el-option label="已过期" :value="3" />
              <el-option label="已销毁" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="queryParams.categoryId" placeholder="全部" clearable style="width: 150px">
              <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryName" :value="item.id" />
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
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="banquetType" label="宴席类型" width="120" />
        <el-table-column prop="banquetDate" label="宴席日期" width="120" />
        <el-table-column prop="quantity" label="数量" width="100">
          <template #default="{ row }">
            {{ row.quantity }}{{ row.quantityUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="produceTime" label="制作时间" width="160" />
        <el-table-column prop="expiryTime" label="过期时间" width="160" />
        <el-table-column prop="foodStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.foodStatus)">
              {{ getStatusText(row.foodStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allergenInfo" label="过敏原" width="150" show-overflow-tooltip />
        <el-table-column prop="registrar" label="登记人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" size="small" @click="handleClaim(row)" v-if="row.foodStatus === 1">领取</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜品名称" prop="foodName">
              <el-input v-model="form.foodName" placeholder="请输入菜品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宴席类型">
              <el-input v-model="form.banquetType" placeholder="如：婚宴、寿宴等" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宴席日期" prop="banquetDate">
              <el-date-picker v-model="form.banquetDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="0" :precision="2" style="width: 70%" />
              <el-input v-model="form.quantityUnit" placeholder="单位" style="width: 28%; margin-left: 2%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宴席地点">
              <el-input v-model="form.banquetLocation" placeholder="请输入宴席地点" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制作时间" prop="produceTime">
              <el-date-picker v-model="form.produceTime" type="datetime" placeholder="选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期时间" prop="expiryTime">
              <el-date-picker v-model="form.expiryTime" type="datetime" placeholder="选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="储存方式">
              <el-input v-model="form.storageMethod" placeholder="如：冷藏、冷冻等" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保存温度">
              <el-input v-model="form.temperature" placeholder="如：0-4℃" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="主要食材">
              <el-input v-model="form.ingredients" placeholder="请输入主要食材" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="过敏原信息">
              <el-input v-model="form.allergenInfo" placeholder="请输入过敏原信息，如：含有鸡蛋、花生等" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜品描述">
              <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入菜品描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登记人" prop="registrar">
              <el-input v-model="form.registrar" placeholder="请输入登记人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input v-model="form.remark" placeholder="请输入备注信息" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="claimDialogVisible" title="领取余餐" width="800px">
      <el-alert
        title="风险提示"
        type="warning"
        :closable="false"
        style="margin-bottom: 20px"
      >
        <p>余餐为宴席剩余食品，存在一定食品安全风险，请务必告知领取人相关风险！</p>
      </el-alert>

      <el-descriptions :column="3" border size="small" style="margin-bottom: 20px">
        <el-descriptions-item label="菜品名称">{{ selectedFood?.foodName }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ selectedFood?.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="可领数量">{{ selectedFood?.quantity }}{{ selectedFood?.quantityUnit }}</el-descriptions-item>
        <el-descriptions-item label="制作时间">{{ selectedFood?.produceTime }}</el-descriptions-item>
        <el-descriptions-item label="过期时间">{{ selectedFood?.expiryTime }}</el-descriptions-item>
        <el-descriptions-item label="过敏原">{{ selectedFood?.allergenInfo || '无' }}</el-descriptions-item>
      </el-descriptions>

      <el-form :model="claimForm" :rules="claimRules" ref="claimFormRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="领取人" prop="recipientId">
              <el-select v-model="claimForm.recipientId" placeholder="请选择领取人" filterable style="width: 100%">
                <el-option v-for="item in recipientList" :key="item.id" :label="`${item.name} (${item.phone})`" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="领取数量" prop="claimQuantity">
              <el-input-number v-model="claimForm.claimQuantity" :min="0.01" :max="selectedFood?.quantity || 0" :precision="2" style="width: 70%" />
              <span style="margin-left: 10px">{{ selectedFood?.quantityUnit }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="领取用途">
              <el-input v-model="claimForm.claimPurpose" placeholder="请输入领取用途" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运输方式">
              <el-input v-model="claimForm.transportMethod" placeholder="如：保温箱配送" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="去向地址">
              <el-input v-model="claimForm.destination" placeholder="请输入详细去向地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计送达时间">
              <el-date-picker v-model="claimForm.expectedArrivalTime" type="datetime" placeholder="选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经办人">
              <el-input v-model="claimForm.handler" placeholder="请输入经办人" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">风险告知确认</el-divider>

        <el-card class="notice-card">
          <template #header>
            <div class="notice-header">
              <el-icon color="#F56C6C"><Warning /></el-icon>
              <span style="margin-left: 8px; font-weight: 600">{{ currentNotice?.noticeTitle }}（{{ currentNotice?.noticeVersion }}）</span>
            </div>
          </template>
          <div class="notice-content" v-html="formatNotice(currentNotice?.noticeContent)"></div>
        </el-card>

        <el-form-item label="签收类型" prop="signType">
          <el-radio-group v-model="claimForm.signType">
            <el-radio label="电子签名">电子签名</el-radio>
            <el-radio label="纸质签字">纸质签字</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="claimForm.isRead">我已仔细阅读上述风险告知书内容</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="claimForm.isUnderstood">我已充分理解所有风险提示内容</el-checkbox>
        </el-form-item>
        <el-form-item prop="isAccepted">
          <el-checkbox v-model="claimForm.isAccepted">我自愿接受全部风险，自行承担食用后的一切后果</el-checkbox>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电子签名">
              <el-input v-model="claimForm.signatureData" type="textarea" :rows="3" placeholder="请输入签名数据(Base64)或签名图片URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签名图片URL">
              <el-input v-model="claimForm.signatureImage" placeholder="请上传并输入签名图片URL" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签收地点">
              <el-input v-model="claimForm.signLocation" placeholder="请输入签收地点" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="见证人">
              <el-input v-model="claimForm.witnessName" placeholder="见证人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="联系电话">
              <el-input v-model="claimForm.witnessPhone" placeholder="见证人电话" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleClaimSubmit">确认领取并签收</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryList } from '@/api/category'
import { getFoodPage, addFood, updateFood, deleteFood } from '@/api/food'
import { getRecipientList } from '@/api/recipient'
import { getCurrentNotice } from '@/api/risk'
import { claimFood } from '@/api/claim'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const categoryList = ref([])
const recipientList = ref([])
const currentNotice = ref(null)

const dialogVisible = ref(false)
const dialogTitle = ref('新增登记')
const formRef = ref()
const isEdit = ref(false)

const claimDialogVisible = ref(false)
const claimFormRef = ref()
const selectedFood = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null,
  categoryId: null
})

const form = reactive({
  id: null,
  foodName: '',
  categoryId: null,
  banquetType: '',
  banquetDate: null,
  banquetLocation: '',
  quantity: null,
  quantityUnit: '份',
  produceTime: null,
  expiryTime: null,
  storageMethod: '',
  temperature: '',
  description: '',
  ingredients: '',
  allergenInfo: '',
  registrar: '',
  remark: ''
})

const claimForm = reactive({
  foodId: null,
  recipientId: null,
  claimQuantity: null,
  quantityUnit: '份',
  claimPurpose: '',
  destination: '',
  transportMethod: '',
  expectedArrivalTime: null,
  handler: '',
  signType: '电子签名',
  signatureData: '',
  signatureImage: '',
  paperSignImage: '',
  signLocation: '',
  isRead: false,
  isUnderstood: false,
  isAccepted: false,
  witnessName: '',
  witnessPhone: '',
  remark: ''
})

const rules = {
  foodName: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  banquetDate: [{ required: true, message: '请选择宴席日期', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  produceTime: [{ required: true, message: '请选择制作时间', trigger: 'change' }],
  expiryTime: [{ required: true, message: '请选择过期时间', trigger: 'change' }],
  registrar: [{ required: true, message: '请输入登记人', trigger: 'blur' }]
}

const claimRules = {
  recipientId: [{ required: true, message: '请选择领取人', trigger: 'change' }],
  claimQuantity: [{ required: true, message: '请输入领取数量', trigger: 'blur' }],
  signType: [{ required: true, message: '请选择签收类型', trigger: 'change' }],
  isAccepted: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('必须接受风险条款才能领取'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const getStatusType = (status) => {
  const types = { 1: 'success', 2: 'warning', 3: 'danger', 4: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 1: '待领取', 2: '已领取', 3: '已过期', 4: '已销毁' }
  return texts[status] || '未知'
}

const formatNotice = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br/>')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getFoodPage(queryParams)
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.status = null
  queryParams.categoryId = null
  queryParams.pageNum = 1
  loadData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    foodName: '',
    categoryId: null,
    banquetType: '',
    banquetDate: null,
    banquetLocation: '',
    quantity: null,
    quantityUnit: '份',
    produceTime: null,
    expiryTime: null,
    storageMethod: '',
    temperature: '',
    description: '',
    ingredients: '',
    allergenInfo: '',
    registrar: '',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增登记'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑登记'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleClaim = async (row) => {
  selectedFood.value = row
  claimForm.foodId = row.id
  claimForm.claimQuantity = row.quantity
  claimForm.quantityUnit = row.quantityUnit
  claimDialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该余餐登记吗？', '提示', {
      type: 'warning'
    })
    await deleteFood(row.id)
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
      await updateFood(form)
      ElMessage.success('更新成功')
    } else {
      await addFood(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleClaimSubmit = async () => {
  await claimFormRef.value.validate()
  try {
    await claimFood(claimForm)
    ElMessage.success('领取登记成功，风险告知已留档')
    claimDialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const loadCategory = async () => {
  categoryList.value = await getCategoryList()
}

const loadRecipient = async () => {
  recipientList.value = await getRecipientList()
}

const loadNotice = async () => {
  currentNotice.value = await getCurrentNotice()
}

onMounted(() => {
  loadData()
  loadCategory()
  loadRecipient()
  loadNotice()
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

.notice-header {
  display: flex;
  align-items: center;
}

.notice-content {
  max-height: 200px;
  overflow-y: auto;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}
</style>
