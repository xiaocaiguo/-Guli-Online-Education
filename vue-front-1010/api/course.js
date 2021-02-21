import request from '@/utils/request'
export default {
    getPageList(page,limit,searchObj) {
        return request({
        url: `/eduservice/coursefront/getFrontCourseList/${page}/${limit}`,
        method: 'post',
        data: searchObj
        })
    },
    getAllSubject() {
        return request({
            url: `/eduservice/subject/getAllSubject`,
            method: 'get'
        })
    },
    getById(courseId) {
        return request({
            url: `/eduservice/coursefront/getCourseInfo/${courseId}`,
            method: 'get'
        })
    }
}