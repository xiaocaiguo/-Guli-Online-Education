import request from '@/utils/request'

export default{

    pageBanner(page,limit){
    return request({
      url: `/educms/bannerAdmin/pageBanner/${page}/${limit}`,
      method: 'get'
    })
  },
  
  getBanner(id){
    return request({
      url: `/educms/bannerAdmin/get/${id}`,
      method: 'get'
    })
  },
  addBanner(banner){
    return request({
      url: `/educms/bannerAdmin/save`,
      method: 'post',
      data: banner
    })
  },

  updateBanner(video){
    return request({
      url: `/educms/bannerAdmin/update`,
      method: 'post',
      data:video
    })
  },
//删除
  deleteBanner(id){
    return request({
      url: `/educms/bannerAdmin/remove/${id}`,
      method: 'delete'
    })
  }

}
