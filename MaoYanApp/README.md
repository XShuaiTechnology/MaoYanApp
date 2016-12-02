## 用户模块User

### 1.获取 access_token 接口
此接口用来通过微信账号获取 access_token。
如果系统内没有该微信账号，系统会自动创建一个账号，并返回与该账号信息相关联的 access_token ; 如果系统内存在该微信账号，那么系统将直接返回与该账号信息相关的 access_token ;
客户端须先通过微信登录，通过微信登录后获取到的用户 openid、unionid 以及微信的 access_token 来调用此接口以获取骑遇系统的 access_token 。
#### 1.1 Endpoint
`/wx_auth/access_token`
#### 1.2 HTTP请求方式
`GET`
#### 1.3 参数说明
| 参数名        | 是否可选           | 说明  |
| ------------- |:-------------:| -----:|
| openid      | 必选 | 微信 openid |
| unionid      | 必选      |  微信 unionid |
| wx_token | 必选      |    微信返回的 access_token |
#### 1.4 返回结果
1.JSON示例
```json
{
  "uid":"wfefwe234awsf",
  "access_token": "12a1b2c3d4",
  "expires_in": "134",
}
```
2.返回参数说明

| 参数        | 说明  |
| ------------- |:-------------:| -----:|
| uid      | 用户唯一标识id |
| access_token      | 猫眼系统的 access_token |
| expires_in      |  access_token 过期时间 |


### 2.获取用户信息接口
#### 2.1 Endpoint
`/users/{uid}`
#### 2.2 HTTP请求方式
`GET`
#### 2.3 返回结果
1 JSON示例
```json
{
    "uid":"wfefwe234awsf",
    // 这个接口的数据要看后面的需求设计
}
```

### 3.推荐页中的轮播图、编辑推荐和全网热播的返回数据都是一样的，只是接口名字不一样。
#### 3.1 Endpoint
`/editorRecommends`
#### 3.2 HTTP请求方式
`GET`
#### 3.3 返回结果
1 JSON示例
```json
{
    "movieId":"",
    "url":"",
    "imageUrl":"",
    "name":"",
    "rating":"",
    "directors":"",
    "actors":"",
    "pubTime":""，
    "description":""
}
```
2.返回参数说明

| 参数        | 说明  |
| ------------- |:-------------:| -----:|
| movieId      | 电影的唯一标识 |
| url      | 电影的播放地址 |
| imageUrl      | 影片图片 |
| name      |  电影的名字 |
| rating      |  评分 |
| directors      |  导演 |
| actors      |  演员 |
| pubTime      |  上映时间 |
| description      |  电影简介 |

### 4.扫码绑定设备
根据极光生成的设备唯一deviceId，手机扫码获取这deviceId发送到服务器端，
实现手机和设备的绑定。需要带着access_token。
#### 4.1 Endpoint
`/devices?access_token='xxx'`
#### 4.2 HTTP请求方式
`POST`
#### 4.3 请求
1.请求示例
```json
{
    "deviceId":"xxxx"
}
```
2.参数说明
| 参数        | 说明  |
| ------------- |:-------------:| -----:|
| deviceId      | 设备的唯一标识 |
#### 4.4 返回结果(暂时不写)
1 JSON示例


### 5.推送影片到设备
根据绑定的设备，将手机上的影片推送到绑定的设备。






## 播放器基于开源的[ijkplayer](https://github.com/Bilibili/ijkplayer)
界面参考的是JieCaoVideoPlayer，其主要是对ijkplayer的UI封装
https://github.com/lipangit/JieCaoVideoPlayer


## android-zxingLibrary
https://github.com/yipianfengye/android-zxingLibrary
生产二维码是用的封装的第三方zxing库



## 微信登录的流程
1、获得微信的openid，unionId，access_token，然后到自己服务器换取自己
服务器的token，和uid以及用户信息。
2、每次需要验证的请求的时候携带自己服务器的token，以get方式传过去。
用户信息相关的根据uid区分。

https://www.zhihu.com/question/30267006
file:///E:/Work/%E7%8C%AB%E7%9C%BC%E5%BD%B1%E9%99%A2App/%E8%8F%9C%E9%B8%9F%E8%B5%84%E6%96%99/GaoWorkspace/nb_product/API%E6%96%87%E6%A1%A3/qiyu_API.html
token只是一个客户端和服务器端交互的凭证，在数据逻辑上区分每个用户还是需要uid来区分。

uid是不会变的，这个是用户的唯一身份标识。access_token 的话，2.0方式会有一个有效期，
过了有效期，如果用户没有手工取消授权，再一次交换后，
会通过uid重新生成一个access_token（但是当然不会自动更新了），
你数据库更新一下就行了，另外你也可以通过申请高级权限延长access_token的有效期间。
另外2.0就是这样的方案，access_token不会像1.0那样固定不变，更靠谱就是申请高级权限延长，
但是其实延长不延长没什么关系，过期后更新就行，不会影响用户使用的。