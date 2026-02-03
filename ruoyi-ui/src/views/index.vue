<template>
  <div class="dashboard-container">
    <!-- 标题区域 -->
    <div class="dashboard-header">
      <div class="header-content">
        <h1 class="main-title">青铜器数字推荐系统</h1>
        <p class="sub-title">Bronze Ware Digital Recommendation System</p>
      </div>
      <div class="header-decoration"></div>
    </div>

    <!-- 汇总数据卡片 -->
    <el-row :gutter="20" class="summary-row">
      <el-col :span="6">
        <div class="summary-card">
          <div class="summary-icon bronze-icon">
            <i class="el-icon-goods"></i>
          </div>
          <div class="summary-info">
            <span class="summary-value">{{ summary.bronzeCount || 0 }}</span>
            <span class="summary-label">藏品总数</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="summary-card">
          <div class="summary-icon view-icon">
            <i class="el-icon-view"></i>
          </div>
          <div class="summary-info">
            <span class="summary-value">{{ summary.totalViews || 0 }}</span>
            <span class="summary-label">总浏览量</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="summary-card">
          <div class="summary-icon action-icon">
            <i class="el-icon-star-on"></i>
          </div>
          <div class="summary-info">
            <span class="summary-value">{{ summary.actionCount || 0 }}</span>
            <span class="summary-label">互动次数</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="summary-card">
          <div class="summary-icon user-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="summary-info">
            <span class="summary-value">{{ summary.userCount || 0 }}</span>
            <span class="summary-label">活跃用户</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="chart-header">
            <span class="chart-title">藏品朝代分布</span>
          </div>
          <div ref="dynastyChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="chart-header">
            <span class="chart-title">藏品器型分布</span>
          </div>
          <div ref="categoryChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="chart-header">
            <span class="chart-title">用户行为统计</span>
          </div>
          <div ref="actionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="chart-header">
            <span class="chart-title">近7天浏览趋势</span>
          </div>
          <div ref="viewTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/bronze/statistics'

export default {
  name: 'Dashboard',
  dicts: ['bus_dynasty', 'bus_category'],
  data() {
    return {
      summary: {},
      dynastyData: [],
      categoryData: [],
      actionData: {},
      viewTrendData: {},
      charts: []
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(chart => chart.dispose())
  },
  methods: {
    loadData() {
      getDashboardData().then(response => {
        const data = response.data
        this.summary = data.summary || {}
        this.dynastyData = data.dynastyDistribution || []
        this.categoryData = data.categoryDistribution || []
        this.actionData = data.actionStatistics || {}
        this.viewTrendData = data.viewTrend || {}

        this.$nextTick(() => {
          this.initDynastyChart()
          this.initCategoryChart()
          this.initActionChart()
          this.initViewTrendChart()
        })
      })
    },
    getDictLabel(dictType, value) {
      const dict = this.dict.type[dictType]
      if (dict) {
        const item = dict.find(d => d.value === value)
        return item ? item.label : value
      }
      return value
    },
    initDynastyChart() {
      const chartDom = this.$refs.dynastyChart
      const chart = echarts.init(chartDom)
      this.charts.push(chart)

      const data = this.dynastyData.map(item => ({
        name: this.getDictLabel('bus_dynasty', item.name),
        value: item.value
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 20,
          top: 'center'
        },
        color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4'],
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          data: data
        }]
      }
      chart.setOption(option)
    },
    initCategoryChart() {
      const chartDom = this.$refs.categoryChart
      const chart = echarts.init(chartDom)
      this.charts.push(chart)

      const data = this.categoryData.map(item => ({
        name: this.getDictLabel('bus_category', item.name),
        value: item.value
      }))

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(d => d.name),
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'bar',
          data: data.map(d => d.value),
          itemStyle: {
            color: '#5470c6'
          },
          barWidth: '50%'
        }]
      }
      chart.setOption(option)
    },
    initActionChart() {
      const chartDom = this.$refs.actionChart
      const chart = echarts.init(chartDom)
      this.charts.push(chart)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['收藏', '点赞', '评分']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'bar',
          data: [
            { value: this.actionData.favorite || 0, itemStyle: { color: '#fac858' } },
            { value: this.actionData.like || 0, itemStyle: { color: '#ee6666' } },
            { value: this.actionData.rate || 0, itemStyle: { color: '#91cc75' } }
          ],
          barWidth: '40%'
        }]
      }
      chart.setOption(option)
    },
    initViewTrendChart() {
      const chartDom = this.$refs.viewTrendChart
      const chart = echarts.init(chartDom)
      this.charts.push(chart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.viewTrendData.dates || []
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'line',
          data: this.viewTrendData.counts || [],
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: {
            width: 3,
            color: '#5470c6'
          },
          itemStyle: {
            color: '#5470c6'
          },
          areaStyle: {
            color: 'rgba(84, 112, 198, 0.2)'
          }
        }]
      }
      chart.setOption(option)
    },
    handleResize() {
      this.charts.forEach(chart => chart.resize())
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}

// 标题区域
.dashboard-header {
  background-color: #fff;
  padding: 30px 40px;
  margin-bottom: 20px;
  border: 1px solid #e8e8e8;
  position: relative;
  overflow: hidden;

  .header-content {
    position: relative;
    z-index: 1;
  }

  .main-title {
    font-size: 32px;
    font-weight: 600;
    color: #1a1a1a;
    margin: 0 0 8px 0;
    letter-spacing: 4px;
  }

  .sub-title {
    font-size: 14px;
    color: #8c8c8c;
    margin: 0;
    letter-spacing: 1px;
    text-transform: uppercase;
  }

  .header-decoration {
    position: absolute;
    right: 40px;
    top: 50%;
    transform: translateY(-50%);
    width: 120px;
    height: 120px;
    border: 3px solid #d4af37;
    opacity: 0.3;

    &::before {
      content: '';
      position: absolute;
      top: 10px;
      left: 10px;
      right: 10px;
      bottom: 10px;
      border: 2px solid #d4af37;
    }
  }
}

// 汇总卡片
.summary-row {
  margin-bottom: 20px;
}

.summary-card {
  background-color: #fff;
  padding: 24px;
  border: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  gap: 20px;
  transition: box-shadow 0.3s;

  &:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .summary-icon {
    width: 56px;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: center;

    i {
      font-size: 28px;
      color: #fff;
    }

    &.bronze-icon {
      background-color: #5470c6;
    }

    &.view-icon {
      background-color: #91cc75;
    }

    &.action-icon {
      background-color: #fac858;
    }

    &.user-icon {
      background-color: #ee6666;
    }
  }

  .summary-info {
    display: flex;
    flex-direction: column;

    .summary-value {
      font-size: 28px;
      font-weight: 600;
      color: #1a1a1a;
      line-height: 1.2;
    }

    .summary-label {
      font-size: 14px;
      color: #8c8c8c;
      margin-top: 4px;
    }
  }
}

// 图表区域
.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border: 1px solid #e8e8e8;

  ::v-deep .el-card__header {
    padding: 16px 20px;
    border-bottom: 1px solid #e8e8e8;
    background-color: #fafafa;
  }

  ::v-deep .el-card__body {
    padding: 20px;
  }

  .chart-header {
    .chart-title {
      font-size: 16px;
      font-weight: 500;
      color: #1a1a1a;
    }
  }

  .chart-container {
    height: 300px;
  }
}
</style>
