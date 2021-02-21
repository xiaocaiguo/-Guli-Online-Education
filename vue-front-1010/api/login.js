import request from '@/utils/request'
export default {
  //登录
  submitLogin(loginVo) {
    return request({
      url: `/educenter/member/login`,
      method: 'post',
      data: loginVo
    })
  },
  //根据token获取用户信息
  getLoginInfo() {
    return request({
      url: `/educenter/member/getMemberInfo`,
      method: 'get',
     // headers: {'token': cookie.get('guli_token')}
    })
    //headers: {'token': cookie.get('guli_token')} 
  }
}