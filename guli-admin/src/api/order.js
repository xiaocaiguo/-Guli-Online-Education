import request from '@/utils/request'

export default{

    pageOrder(page,limit){
    return request({
      url: `/orderservice/order/pageOrder/${page}/${limit}`,
      method: 'get'
    })
  },

}
