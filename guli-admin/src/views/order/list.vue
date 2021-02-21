<template>
  <div class="app-container">
    <!-- 表格 -->
    <el-table
    v-loading="listLoading"
    :data="list"
    element-loading-text="数据加载中"
    border
    fit
    highlight-current-row
    row-class-name="myClassList">

    <el-table-column
        label="序号"
        width="35"
        align="center">
        <template slot-scope="scope">
        {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
    </el-table-column>

    <el-table-column label="订单号" align="center">
        <template slot-scope="scope">
        {{ scope.row.orderNo }}
        </template>
    </el-table-column>
    <el-table-column label="课程id" align="center">
        <template slot-scope="scope">
        {{ scope.row.courseId }}
        </template>
    </el-table-column>
    <el-table-column label="课程名称" align="center" >
        <template slot-scope="scope">
        {{scope.row.courseTitle}}
        </template>
    </el-table-column>
    <el-table-column label="课程封面" width="200" align="center">
        <template slot-scope="scope">
            <div class="pic">
            <img :src="scope.row.courseCover" :alt="scope.row.courseTitle" width="100%">
            </div>
        </template>
    </el-table-column>
    <el-table-column label="讲师名称" align="center" >
        <template slot-scope="scope">
        {{ scope.row.teacherName }}
        </template>
    </el-table-column>
    <el-table-column label="会员id" width="100" align="center" >
        <template slot-scope="scope">
        {{scope.row.memberId}}
        </template>
    </el-table-column>
    <el-table-column label="会员昵称" align="center" >
        <template slot-scope="scope">
        {{ scope.row.nickname }}
        </template>
    </el-table-column>
    <el-table-column label="会员手机" align="center" >
        <template slot-scope="scope">
        {{ scope.row.mobile }}
        </template>
    </el-table-column>
    <el-table-column label="订单金额" align="center" >
        <template slot-scope="scope">
        {{scope.row.totalFee.toFixed(2)}}
        </template>
    </el-table-column>
    <el-table-column label="支付类型" align="center" >
        <template slot-scope="scope">
         {{ scope.row.payType===1?'微信':'支付宝' }}
        </template>
    </el-table-column>
    <el-table-column label="订单状态" align="center" >
        <template slot-scope="scope">
         {{ scope.row.status===1?'已支付':'未支付' }}
        </template>
    </el-table-column>

    
    </el-table>

    <!-- 分页 -->
    <el-pagination
    :current-page="page"
    :page-size="limit"
    :total="total"
    style="padding: 30px 0; text-align: center;"
    layout="total, prev, pager, next, jumper"
    @current-change="init"
    />

  </div>
  
</template>
<script>
import orderApi from '@/api/order'

export default {

  data() {
    return {
      listLoading: true, // 是否显示loading信息
      list: null, // 数据列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 10, // 每页记录数
    }
  },

  created() { // 当页面加载时获取数据
    this.init()
  },

  methods: {
    init(page = 1){
        console.log('加载列表')
      // 当点击分页组件的切换按钮的时候，会传输一个当前页码的参数page
      // 解决分页无效问题
      this.page = page
      this.listLoading = true
      orderApi.pageOrder(this.page, this.limit).then(response => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.list = response.data.items
          this.total = response.data.total
        }
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
.myClassList .info {
    width: 450px;
    overflow: hidden;
}
.myClassList .info .pic {
    width: 150px;
    height: 90px;
    overflow: hidden;
    float: left;
}
.myClassList .info .pic a {
    display: block;
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
}
.myClassList .info .pic img {
    display: block;
    width: 100%;
}
.myClassList td .info .title {
    width: 280px;
    float: right;
    height: 90px;
}
.myClassList td .info .title a {
    display: block;
    height: 48px;
    line-height: 24px;
    overflow: hidden;
    color: #00baf2;
    margin-bottom: 12px;
}
.myClassList td .info .title p {
    line-height: 20px;
    margin-top: 5px;
    color: #818181;
}
</style>