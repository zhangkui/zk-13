import request from '@/utils/request'

export function getCurrentNotice() {
  return request({
    url: '/risk/notice/current',
    method: 'get'
  })
}

export function getNoticeList() {
  return request({
    url: '/risk/notice/list',
    method: 'get'
  })
}

export function addNotice(data) {
  return request({
    url: '/risk/notice',
    method: 'post',
    data
  })
}

export function updateNotice(data) {
  return request({
    url: '/risk/notice',
    method: 'put',
    data
  })
}

export function setCurrentNotice(id) {
  return request({
    url: `/risk/notice/${id}/current`,
    method: 'put'
  })
}

export function deleteNotice(id) {
  return request({
    url: `/risk/notice/${id}`,
    method: 'delete'
  })
}

export function getArchivePage(params) {
  return request({
    url: '/risk/archive/page',
    method: 'get',
    params
  })
}

export function getArchiveById(id) {
  return request({
    url: `/risk/archive/${id}`,
    method: 'get'
  })
}

export function getArchiveByClaimId(claimRecordId) {
  return request({
    url: `/risk/archive/claim/${claimRecordId}`,
    method: 'get'
  })
}

export function getDashboardStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}
