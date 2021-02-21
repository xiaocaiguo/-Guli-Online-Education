<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像 -->
      <el-form-item label="讲师头像">

          <!-- 头衔缩略图 -->
          <pan-thumb :image="teacher.avatar"/>
          <!-- 文件上传按钮 -->
          <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
          </el-button>

          <!--
            v-show：是否显示上传组件
            :key：类似于id，如果一个页面多个图片上传控件，可以做区分
            :url：后台上传的url地址
            @close：关闭上传组件
            @crop-upload-success：上传成功后的回调 -->
          <image-cropper
                        v-show="imagecropperShow"
                        :width="300"
                        :height="300"
                        :key="imagecropperKey"
                        :url="BASE_API+'/eduoss/fileoss/uploadoss'"
                        field="file"
                        @close="close"
                        @crop-upload-success="cropSuccess"/>

      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import teacher from '@/api/edu/teacher.js'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
  components: { ImageCropper, PanThumb },
  data(){
      return {
          teacher:{
              name: '',
              sort: 0,
              level: 1,
              career: '',
              intro: '',
              avatar: 'https://edu-xiao.oss-cn-guangzhou.aliyuncs.com/2021/01/21/b3a110a951e844fa88443087a4859a55file.png'
          },
          saveBtnDisabled:false,//只能点击一次，防止多次提交

          imagecropperShow:false,//上传组件是否显示
          imagecropperKey:0,//上传组件key值
          BASE_API:process.env.BASE_API//获取dev.env.js里面的地址

      }
  },
  watch: {
      $route(to, from) {//监听路由的变化，改变时执行
      console.log('watch $route')
      this.init()
      }
  },
  created(){
      this.init()
  },
  methods:{
    close(){//关闭上传弹窗
      this.imagecropperShow=false
      //上传组件初始化
      this.imagecropperKey=this.imagecropperKey+1
    },
    //上传成功后执行
    cropSuccess(data){
      this.imagecropperShow=false
      this.teacher.avatar = data.url
      //上传组件初始化
      this.imagecropperKey=this.imagecropperKey+1
    },
    init(){
        if (this.$route.params && this.$route.params.id) {
            const id = this.$route.params.id
            this.getInfo(id)
        }else {
            // 使用对象拓展运算符，拷贝对象，而不是引用，
            // 否则新增一条记录后，defaultForm就变成了之前新增的teacher的值
            this.teacher = { ...defaultForm }
        }
    },
    saveOrUpdate(){
        //判断修改还是添加
        if (this.teacher.id) {
            this.updateTeacherInfo()
        }else{
            this.saveTeacher()
        }
    },
    saveTeacher(){
        teacher.addTeacher(this.teacher)
            .then(response => {
                this.$message({
                    type: 'success',
                    message: '添加成功'
                })
                //回到列表页面，路由跳转
                this.$router.push({path:'/teacher/table'})
            })
    },
    getInfo(id){
        teacher.getTeacherInfo(id)
            .then(response => {
                this.teacher = response.data.teacher
            })

    },
    updateTeacherInfo(){
        teacher.updateTeacher(this.teacher)
            .then(response => {
                this.$message({
                    type: 'success',
                    message: '修改成功'
                })
                //回到列表页面，路由跳转
                this.$router.push({path:'/teacher/table'})
            })
    }
  }
}
</script>