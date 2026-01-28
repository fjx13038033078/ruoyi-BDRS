import request from '@/utils/request'

// 查询用户互动行为列表
export function listAction(query) {
  return request({
    url: '/bronze/action/list',
    method: 'get',
    params: query
  })
}

// 查询用户互动行为详细
export function getAction(id) {
  return request({
    url: '/bronze/action/' + id,
    method: 'get'
  })
}

// 删除用户互动行为
export function delAction(id) {
  return request({
    url: '/bronze/action/' + id,
    method: 'delete'
  })
}

// 收藏/取消收藏
export function toggleFavorite(bronzeId) {
  return request({
    url: '/bronze/action/favorite/' + bronzeId,
    method: 'post'
  })
}

// 点赞/取消点赞
export function toggleLike(bronzeId) {
  return request({
    url: '/bronze/action/like/' + bronzeId,
    method: 'post'
  })
}

// 评分
export function rateBronze(bronzeId, score) {
  return request({
    url: '/bronze/action/rate/' + bronzeId,
    method: 'post',
    params: { score: score }
  })
}

// 获取我的收藏列表
export function myFavorites() {
  return request({
    url: '/bronze/action/favorites',
    method: 'get'
  })
}

// 检查是否收藏
export function checkFavorite(bronzeId) {
  return request({
    url: '/bronze/action/check/favorite/' + bronzeId,
    method: 'get'
  })
}

// 检查是否点赞
export function checkLike(bronzeId) {
  return request({
    url: '/bronze/action/check/like/' + bronzeId,
    method: 'get'
  })
}

// 获取我的评分
export function getMyScore(bronzeId) {
  return request({
    url: '/bronze/action/score/' + bronzeId,
    method: 'get'
  })
}
