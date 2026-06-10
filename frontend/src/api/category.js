import request from '@/utils/request'

export function getCategoryList() {
  return request({
    url: '/food/categories',
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/food/category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/food/category',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/food/category/${id}`,
    method: 'delete'
  })
}
