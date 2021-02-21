import request from '@/utils/request'
export default {
    getPageList(page,limit) {
    return request({
      url: `/eduservice/teacherfront/pageTeacherList/${page}/${limit}`,
      method: 'get'
    })
    },
    getById(teacherId) {
        return request({
            url: `/eduservice/teacherfront/getTeacherFrontInfo/${teacherId}`,
            method: 'get'
        })
    }
}