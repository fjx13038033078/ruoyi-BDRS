import request from '@/utils/request'

// 查询数字展览列表
export function listExhibition(query) {
  return request({
    url: '/bronze/exhibition/list',
    method: 'get',
    params: query
  })
}

// 查询数字展览详细
export function getExhibition(id) {
  return request({
    url: '/bronze/exhibition/' + id,
    method: 'get'
  })
}

// 新增数字展览
export function addExhibition(data) {
  return request({
    url: '/bronze/exhibition',
    method: 'post',
    data: data
  })
}

// 修改数字展览
export function updateExhibition(data) {
  return request({
    url: '/bronze/exhibition',
    method: 'put',
    data: data
  })
}

// 删除数字展览
export function delExhibition(id) {
  return request({
    url: '/bronze/exhibition/' + id,
    method: 'delete'
  })
}

// 导出数字展览
export function exportExhibition(query) {
  return request({
    url: '/bronze/exhibition/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}
