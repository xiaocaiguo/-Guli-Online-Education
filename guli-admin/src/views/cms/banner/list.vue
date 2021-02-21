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
        width="70"
        align="center">
        <template slot-scope="scope">
        {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
    </el-table-column>

    <el-table-column label="封面" width="300" align="center">
        <template slot-scope="scope">
            <div class="pic">
            <img :src="scope.row.imageUrl" :alt="scope.row.title" width="100%">
            </div>
        </template>
    </el-table-column>
    <el-table-column label="名称" align="center">
        <template slot-scope="scope">
        {{ scope.row.title }}
        </template>
    </el-table-column>
    <el-table-column label="link_url" align="center">
        <template slot-scope="scope">
        {{ scope.row.linkUrl }}
        </template>
    </el-table-column>
    <el-table-column label="排序" align="center">
        <template slot-scope="scope">
        {{ scope.row.sort }}
        </template>
    </el-table-column>
    <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
        {{ scope.row.gmtCreate.substr(0, 10) }}
        </template>
    </el-table-column>
    <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
        <router-link :to="'/cms/banner/add/'+scope.row.id">
            <el-button type="text" size="mini" icon="el-icon-edit">编辑</el-button>
        </router-link>
        <el-button type="text" size="mini" icon="el-icon-delete" @click="removeBannerById(scope.row.id)">删除</el-button>
        </template>
    </el-table-column>
    </el-table>

    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center;"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="init"
      @size-change="changeSize"
    />

  </div>
  
</template>
<script>
import bannerApi from '@/api/banner'

export default {

  data() {
    return {
    //   listLoading: true, // 是否显示loading信息
      list: null, // 数据列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 5, // 每页记录数
    }
  },

  created() { // 当页面加载时获取数据
    this.init()
  },

  methods: {
      init(page = 1){
          this.page = page
          this.listLoading = true
          bannerApi.pageBanner(this.page,this.limit)
            .then(response => {
                this.list = response.data.items
                console.log(this.list)
                this.total = response.data.total
                this.listLoading = false
            })
      },
      // 当页码发生改变的时候
        changeSize(size) {
          console.log(size)
          this.limit = size
          this.init(1)
        },

    removeBannerById(id){
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              var promise = bannerApi.deleteBanner(id)
              promise.then(()=>{this.init()})
              this.$message({
                  type: 'success',
                  message: '删除成功!'
              });
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