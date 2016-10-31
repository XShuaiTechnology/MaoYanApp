package com.mao.movie.model;

/**
 * Created by GaoMatrix on 2016/10/31.
 */
public class WxUserInfo {
    // unionid=oGfXpwD9TnQStXhPeR-0aY1rQ48Q
    // province=山东
    // gender=1
    // screen_name=Gao
    // openid=oCDCXwzpEnl_gbQKTQutfptDRdoI
    // language=zh_CN
    // profile_image_url=http://wx.qlogo.cn/mmopen/Q3auHgzwzM42gaUERJgrialk5Dg8JHqYsTlZ4Tof70Oibx0MxLNhISxzNmobyF19G6bzE4XxQoIORAmiaoZu3iacuw/0
    // country=中国
    // city=日照
    private String unionid;
    private String province;
    private int gender;
    private String screen_name;
    private String openid;
    private String language;
    private String profile_image_url;
    private String country;
    private String city;

    @Override
    public String toString() {
        return "WxUserInfo{" +
                "unionid='" + unionid + '\'' +
                ", province='" + province + '\'' +
                ", gender=" + gender +
                ", screen_name='" + screen_name + '\'' +
                ", openid='" + openid + '\'' +
                ", language='" + language + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
