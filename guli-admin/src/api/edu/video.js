import request from '@/utils/request'

export default{

  addVideo(video){
    return request({
      url: `/eduservice/video/addVideo`,
      method: 'post',
      data: video
    })
  },

  getVideo(videoId){
    return request({
      url: `/eduservice/video/getVideo/${videoId}`,
      method: 'get'
    })
  },

  updateVideo(video){
    return request({
      url: `/eduservice/video/updateVideo`,
      method: 'post',
      data:video
    })
  },
//删除小节
  deleteVideo(videoId){
    return request({
      url: `/eduservice/video/${videoId}`,
      method: 'delete'
    })
  },
  //删除阿里云视频
  deleteAlyVideo(videoId){
    return request({
      url: `/eduvod/video/removeAlyVideo/${videoId}`,
      method: 'delete'
    })
  }

}
