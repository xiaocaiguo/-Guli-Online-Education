import request from '@/utils/request'

export default{

  addCourseInfo(courseInfoVo){
    return request({
      url: `/eduservice/course/addCourseInfo`,
      method: 'post',
      data: courseInfoVo
    })
  },
  getListTeacher(){
    return request({
      url: `/eduservice/teacher/findAll`,
      method: 'get'
    })
  },
  getCourseInfo(courseId){
    return request({
      url: `/eduservice/course/getCourseInfo/${courseId}`,
      method: 'get'
    })
  },
  updateCourseInfo(courseInfoVo){
    return request({
      url: `/eduservice/course/updateCourseInfo`,
      method: 'post',
      data: courseInfoVo
    })
  },
  getPublishCourseInfo(courseId){
    return request({
      url: `/eduservice/course/getPublishCourseInfo/${courseId}`,
      method: 'get'
    })
  },
  publishCourse(courseId){
    return request({
      url: `/eduservice/course/publishCourse/${courseId}`,
      method: 'get'
    })
  },
  getCourseList(current,limit,courseQuery){
    return request({
      url: `/eduservice/course/pageCourseCondition/${current}/${limit}`,
      method: 'post',
      data: courseQuery
    })
  },
  deleteCourse(courseId){
    return request({
      url: `/eduservice/course/${courseId}`,
      method: 'delete'
    })
  },

}
