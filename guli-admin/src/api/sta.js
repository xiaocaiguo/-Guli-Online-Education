import request from '@/utils/request'

export default{

    createStatistics(day){
    return request({
      url: `/staservice/statistics-daily/countRegister/${day}`,
      method: 'post'
    })
  },
  
  showChart(searchObj){
    return request({
      url: `/staservice/statistics-daily/showData/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
      method: 'get'
    })
  }

}
