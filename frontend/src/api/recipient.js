import request from '@/utils/request'

export function getRecipientPage(params) {
  return request({
    url: '/recipient/page',
    method: 'get',
    params
  })
}

export function getRecipientById(id) {
  return request({
    url: `/recipient/${id}`,
    method: 'get'
  })
}

export function addRecipient(data) {
  return request({
    url: '/recipient',
    method: 'post',
    data
  })
}

export function updateRecipient(data) {
  return request({
    url: '/recipient',
    method: 'put',
    data
  })
}

export function updateRecipientStatus(id, status) {
  return request({
    url: `/recipient/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function deleteRecipient(id) {
  return request({
    url: `/recipient/${id}`,
    method: 'delete'
  })
}

export function getRecipientList() {
  return request({
    url: '/recipient/list',
    method: 'get'
  })
}
