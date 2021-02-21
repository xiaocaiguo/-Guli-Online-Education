<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>

    <el-form label-width="120px">

    <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
    </el-form-item>

    <!-- 所属分类：级联下拉列表 -->
    <!-- 一级分类 -->
    <el-form-item label="课程类别">
    <el-select
        v-model="courseInfo.subjectParentId"
        placeholder="一级分类"
        @change="subjectLevelOneChanged">
        <el-option
        v-for="subject in subjectOneList"
        :key="subject.id"
        :label="subject.title"
        :value="subject.id"/>
    </el-select>
    <!-- 二级分类 -->
    <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
    <el-option
        v-for="subject in subjectTwoList"
        :key="subject.id"
        :label="subject.title"
        :value="subject.id"/>
    </el-select>
</el-form-item>

    <!-- 课程讲师 -->
    <el-form-item label="课程讲师">
    <el-select
        v-model="courseInfo.teacherId"
        placeholder="请选择">
        <el-option
        v-for="teacher in teacherList"
        :key="teacher.id"
        :label="teacher.name"
        :value="teacher.id"/>
    </el-select>
    </el-form-item>

    <el-form-item label="总课时">
        <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
    </el-form-item>

        <!-- 课程简介-->
    <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description"/>
    </el-form-item> 

    <!-- 课程封面-->
    <el-form-item label="课程封面">

        <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :action="BASE_API+'/eduoss/fileoss/uploadoss'"
            class="avatar-uploader">
            <img :src="courseInfo.cover" width="50%" height="50%">
        </el-upload>

    </el-form-item>

    <!-- 课程封面
    <el-form-item label="课程封面">

        <el-upload
            class="upload-banner"
            drag
            :action="BASE_API+'/eduoss/fileoss/uploadoss'"
            :auto-upload="true"
            :on-change="handleCrop"
            :before-upload="beforeAvatarUpload"
            :show-file-list="false"
            >
            <el-image v-if="courseInfo.cover" :src="courseInfo.cover" fit="contain" class="avatar"></el-image>
            <div v-else class="upload-box">
                <el-button type="primary" class="select-btn">选择图片</el-button>
            </div>
            </el-upload>

    </el-form-item> -->

    <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
    </el-form-item>

    <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
    </el-form-item>
    </el-form>
  </div>
</template>
<script>
import course from '@/api/edu/course'
import subject from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce'

export default {
    components: { Tinymce},
    data (){
        return {
            saveBtnDisabled:false,
            courseInfo:{
                title: '',
                subjectId: '',
                subjectParentId:'',
                teacherId: '',
                lessonNum: 0,
                description: '',
                cover: '/static/01.jpg',
                price: 0
            },
            BASE_API: process.env.BASE_API,
            teacherList:[],
            subjectOneList:[],
            subjectTwoList:[],
            courseId:'',
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
        init(){
            if (this.$route.params && this.$route.params.id) {
                this.courseId = this.$route.params.id
                // 根据id获取课程基本信息
                this.getInfo();
                this.getTeacherList()
            }else{
                this.getTeacherList()
                this.getSubjectOneList()
            }
        },
        getInfo(){
            course.getCourseInfo(this.courseId)
                .then(response => {
                    this.courseInfo = response.data.courseInfoVo
                    subject.getSubjectList()
                        .then(response => {
                            this.subjectOneList = response.data.list
                            for(var i = 0; i<this.subjectOneList.length;i++){
                                var oneSubject = this.subjectOneList[i]
                                if(oneSubject.id == this.courseInfo.subjectParentId){
                                    this.subjectTwoList = oneSubject.children
                                }
                            }
                        })
                })
        },
        //上传成功
        handleAvatarSuccess(res,file){
            this.courseInfo.cover = res.data.url
        },
        handleCrop (file) {
            // 点击弹出剪裁框
            this.$nextTick(() => {
            if (this.canCropper) {
                this.cropperOption.img = window.URL.createObjectURL(file.raw) // 将图片转化为路径
                this.showCropper = this.canCropper
            }
            })
        },
        //上传之前
        beforeAvatarUpload(file){
            const isJPG = file.type === 'image/jpeg'
            const isLt2M = file.size / 1024 / 1024 < 2

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        },
        subjectLevelOneChanged(value){
            //value是一级分类id
            for (var i=0;i<this.subjectOneList.length;i++){
                var oneSubject = this.subjectOneList[i]
                if(value === oneSubject.id){
                    this.subjectTwoList = oneSubject.children
                    this.courseInfo.subjectId = ''
                }
            }

        },
        getSubjectOneList(){
            subject.getSubjectList()
                .then(response => {
                    this.subjectOneList = response.data.list
                })
        },
        
        getTeacherList(){
            course.getListTeacher()
                .then(response => {
                    this.teacherList = response.data.items
                })
        },
        addCourse(){
            course.addCourseInfo(this.courseInfo)
                .then(response => {
                    this.$message({
                    type: 'success',
                    message: '添加课程信息成功'
                    })
                    this.$router.push({path: '/course/chapter/'+response.data.courseId})
                })
        },
        updateCourse(){
            course.updateCourseInfo(this.courseInfo)
                .then(response => {
                    this.$message({
                    type: 'success',
                    message: '修改课程信息成功'
                    })
                    this.$router.push({path: '/course/chapter/'+this.courseId})
                })
        },

        saveOrUpdate(){
            if(!this.courseInfo.id){
                this.addCourse()
            }else{
                this.updateCourse()
            }
        },
    }
}
</script>
<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>