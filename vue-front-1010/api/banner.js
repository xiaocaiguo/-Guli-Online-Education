import request from '@/utils/request'
export default {
  getListBanner() {
    return request({
      url: `/educms/bannerFront/getAllBanner`,
      method: 'get'
    })
  }
}