import request from '@/utils/request'

export function getClaimPage(params) {
  return request({
    url: '/claim/page',
    method: 'get',
    params
  })
}

export function getClaimById(id) {
  return request({
    url: `/claim/${id}`,
    method: 'get'
  })
}

export function claimFood(data) {
  return request({
    url: '/claim',
    method: 'post',
    data
  })
}

export function updateClaimStatus(id, status, feedback) {
  return request({
    url: `/claim/${id}/status`,
    method: 'put',
    params: { status, feedback }
  })
}

export function updateClaim(data) {
  return request({
    url: '/claim',
    method: 'put',
    data
  })
}

export function deleteClaim(id) {
  return request({
    url: `/claim/${id}`,
    method: 'delete'
  })
}
