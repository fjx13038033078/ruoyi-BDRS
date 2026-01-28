import request from '@/utils/request'

// 查询青铜器藏品列表
export function listWare(query) {
  return request({
    url: '/bronze/ware/list',
    method: 'get',
    params: query
  })
}

// 查询青铜器藏品详细
export function getWare(id) {
  return request({
    url: '/bronze/ware/' + id,
    method: 'get'
  })
}

// 新增青铜器藏品
export function addWare(data) {
  return request({
    url: '/bronze/ware',
    method: 'post',
    data: data
  })
}

// 修改青铜器藏品
export function updateWare(data) {
  return request({
    url: '/bronze/ware',
    method: 'put',
    data: data
  })
}

// 删除青铜器藏品
export function delWare(id) {
  return request({
    url: '/bronze/ware/' + id,
    method: 'delete'
  })
}

// 导出青铜器藏品
export function exportWare(query) {
  return request({
    url: '/bronze/ware/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 查询热门青铜器列表
export function listHotWare(limit) {
  return request({
    url: '/bronze/ware/hot/' + limit,
    method: 'get'
  })
}

// 根据展览ID查询藏品列表
export function listWareByExhibition(exhibitionId) {
  return request({
    url: '/bronze/ware/exhibition/' + exhibitionId,
    method: 'get'
  })
}
