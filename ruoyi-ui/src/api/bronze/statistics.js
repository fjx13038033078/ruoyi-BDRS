import request from '@/utils/request'

/**
 * 获取首页统计数据
 */
export function getDashboardData() {
  return request({
    url: '/bronze/statistics/dashboard',
    method: 'get'
  })
}
