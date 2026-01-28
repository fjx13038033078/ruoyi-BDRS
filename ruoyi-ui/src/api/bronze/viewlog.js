import request from '@/utils/request'

// 查询用户浏览记录列表
export function listViewlog(query) {
  return request({
    url: '/bronze/viewlog/list',
    method: 'get',
    params: query
  })
}

// 查询用户浏览记录详细
export function getViewlog(id) {
  return request({
    url: '/bronze/viewlog/' + id,
    method: 'get'
  })
}

// 删除用户浏览记录
export function delViewlog(id) {
  return request({
    url: '/bronze/viewlog/' + id,
    method: 'delete'
  })
}

// 记录浏览
export function recordView(bronzeId) {
  return request({
    url: '/bronze/viewlog/record/' + bronzeId,
    method: 'post'
  })
}

// 获取我的浏览历史
export function myHistory() {
  return request({
    url: '/bronze/viewlog/history',
    method: 'get'
  })
}
