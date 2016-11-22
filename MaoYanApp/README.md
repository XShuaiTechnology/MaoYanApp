### 播放器基于开源的[ijkplayer](https://github.com/Bilibili/ijkplayer)
界面参考的是JieCaoVideoPlayer，其主要是对ijkplayer的UI封装
https://github.com/lipangit/JieCaoVideoPlayer


### android-zxingLibrary
https://github.com/yipianfengye/android-zxingLibrary
生产二维码是用的封装的第三方zxing库



### 微信登录的流程
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