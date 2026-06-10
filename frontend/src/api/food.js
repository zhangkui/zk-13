import request from '@/utils/request'

export function getFoodPage(params) {
  return request({
    url: '/food/page',
    method: 'get',
    params
  })
}

export function getFoodById(id) {
  return request({
    url: `/food/${id}`,
    method: 'get'
  })
}

export function addFood(data) {
  return request({
    url: '/food',
    method: 'post',
    data
  })
}

export function updateFood(data) {
  return request({
    url: '/food',
    method: 'put',
    data
  })
}

export function updateFoodStatus(id, status) {
  return request({
    url: `/food/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function deleteFood(id) {
  return request({
    url: `/food/${id}`,
    method: 'delete'
  })
}

export function getAvailableFood() {
  return request({
    url: '/food/available',
    method: 'get'
  })
}
