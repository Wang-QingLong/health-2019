//添加请求拦截器
axios.interceptors.request.use(function (config) {
//在发送请求之前将登录保存的token添加到请求头里面
    config.headers.token = localStorage.getItem("token");
    return config;
}, function (error) {
//对请求错误做些啥
    return Promise.reject(error);
});