<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="banner.title"/>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="banner.sort" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="路由跳转路径">
        <el-input v-model="banner.linkUrl"/>
      </el-form-item>
      <!-- 封面-->
      <el-form-item label="封面">

          <el-upload
            class="avatar-uploader"
            :action="BASE_API+'/eduoss/fileoss/uploadoss'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="banner.imageUrl" :src="banner.imageUrl" class="avatar" width="100%" height="100%">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import bannerApi from '@/api/banner'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

const defaultForm = {
  id:'',
  title: '',
  sort: 0,
  linkUrl: '',
  imageUrl: 'https://edu-xiao.oss-cn-guangzhou.aliyuncs.com/2021/02/20/128b9f7711104e8ab7ae1d91f02e78bc297acd3b-b592-4cfb-a446-a28310369675.jpg'
}

export default {
  components: { ImageCropper, PanThumb },
  data(){
      return {
          banner:{
              id:'',
              title: '',
              sort: 0,
              imageUrl: '',
              linkUrl: ''
          },
          saveBtnDisabled:false,//只能点击一次，防止多次提交
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
    handleAvatarSuccess(res, file) {
      this.imagecropperShow=false
      this.banner.imageUrl = res.data.url
      console.log(this.banner.imageUrl)
      // this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },

    init(){
        if (this.$route.params && this.$route.params.id) {
            const id = this.$route.params.id
            this.getInfo(id)
        }else {
            // 使用对象拓展运算符，拷贝对象，而不是引用，
            // 否则新增一条记录后，defaultForm就变成了之前新增的banner的值
            this.banner = { ...defaultForm }
        }
    },
    saveOrUpdate(){
        //判断修改还是添加
        if (this.banner.id) {
            this.updateBannerInfo()
        }else{
            this.saveBanner()
            console.log(this.banner)
        }
    },
    saveBanner(){
        bannerApi.addBanner(this.banner)
            .then(response => {
                this.$message({
                    type: 'success',
                    message: '添加成功'
                })
                //回到列表页面，路由跳转
                this.$router.push({path:'/cms/banner/list'})
            })
    },
    getInfo(id){
        bannerApi.getBanner(id)
            .then(response => {
                this.banner = response.data.item
            })

    },
    updateBannerInfo(){
        bannerApi.updateBanner(this.banner)
            .then(response => {
                this.$message({
                    type: 'success',
                    message: '修改成功'
                })
                //回到列表页面，路由跳转
                this.$router.push({path:'/cms/banner/list'})
            })
    }
  }
}
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    /* width: 178px;
    height: 178px; */
    display: block;
  }
</style>