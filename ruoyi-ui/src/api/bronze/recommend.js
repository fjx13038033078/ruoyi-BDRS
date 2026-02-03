import request from '@/utils/request'

/**
 * 获取个性化推荐列表
 * @param {Number} size 推荐数量
 */
export function getRecommendations(size) {
  return request({
    url: '/bronze/recommend/list',
    method: 'get',
    params: { size }
  })
}

/**
 * 获取热门推荐（冷启动推荐）
 * @param {Number} size 推荐数量
 */
export function getHotRecommendations(size) {
  return request({
    url: '/bronze/recommend/hot',
    method: 'get',
    params: { size }
  })
}
