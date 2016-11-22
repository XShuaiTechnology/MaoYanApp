package com.mao.movie.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GaoMatrix on 2016/11/14.
 */
public class RecommendMovie {

    /**
     * total : 12
     * rows : [{"id":959,"name":"反弹风暴2","url":"http://oauoo9ml6.bkt.clouddn.com/recommend/picture/1478657223879.png","label":"中国 香港 / 剧情 犯罪","intentComponentPackage":"com.xshuai.netplayer","intentComponentClass":"com.xshuai.netplayer.VideoPlayer","intentExtras":["string","name","反弹风暴2","string","type","4","string","movieurl","/apps/plcloud/反贪风暴2.mp4"],"intentData":"/apps/plcloud/反贪风暴2.mp4","apkUrl":"http://7xt472.com1.z0.glb.clouddn.com/XShuaiNetPlayer","apkVersionCode":"0","tuiJianType":"4","description":"重案组与廉政公署两方从针锋相对到携手查案的故事。林德禄节奏把控紧凑，细节也有铺垫：酒后吐出初恋在船上、笔中藏匿罪证U盘、商业大厦调查围捕...陈静对嗜赌如命大哥痛苦回忆。面瘫古天乐复读机般重复着\u201cICAC查案，不方便透露\u201d，张智霖从懒散弱势到雷霆反击演屌爆，蔡少芬是少有亮点之一","status":1,"rating":"5.3","pubDate":"20161114022124","care_sele":1,"top":0,"parentId":"35","recommend":"片姐"}]
     */

    private int total;
    /**
     * id : 959
     * name : 反弹风暴2
     * url : http://oauoo9ml6.bkt.clouddn.com/recommend/picture/1478657223879.png
     * label : 中国 香港 / 剧情 犯罪
     * intentComponentPackage : com.xshuai.netplayer
     * intentComponentClass : com.xshuai.netplayer.VideoPlayer
     * intentExtras : ["string","name","反弹风暴2","string","type","4","string","movieurl","/apps/plcloud/反贪风暴2.mp4"]
     * intentData : /apps/plcloud/反贪风暴2.mp4
     * apkUrl : http://7xt472.com1.z0.glb.clouddn.com/XShuaiNetPlayer
     * apkVersionCode : 0
     * tuiJianType : 4
     * description : 重案组与廉政公署两方从针锋相对到携手查案的故事。林德禄节奏把控紧凑，细节也有铺垫：酒后吐出初恋在船上、笔中藏匿罪证U盘、商业大厦调查围捕...陈静对嗜赌如命大哥痛苦回忆。面瘫古天乐复读机般重复着“ICAC查案，不方便透露”，张智霖从懒散弱势到雷霆反击演屌爆，蔡少芬是少有亮点之一
     * status : 1
     * rating : 5.3
     * pubDate : 20161114022124
     * care_sele : 1
     * top : 0
     * parentId : 35
     * recommend : 片姐
     */

    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable{
        private int id;
        private String name;
        private String url;
        private String label;
        private String intentComponentPackage;
        private String intentComponentClass;
        private String intentData;
        private String apkUrl;
        private String apkVersionCode;
        private String tuiJianType;
        private String description;
        private int status;
        private String rating;
        private String pubDate;
        private int care_sele;
        private int top;
        private String parentId;
        private String recommend;
        private List<String> intentExtras;

        @Override
        public String toString() {
            return "RowsBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", label='" + label + '\'' +
                    ", intentComponentPackage='" + intentComponentPackage + '\'' +
                    ", intentComponentClass='" + intentComponentClass + '\'' +
                    ", intentData='" + intentData + '\'' +
                    ", apkUrl='" + apkUrl + '\'' +
                    ", apkVersionCode='" + apkVersionCode + '\'' +
                    ", tuiJianType='" + tuiJianType + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", rating='" + rating + '\'' +
                    ", pubDate='" + pubDate + '\'' +
                    ", care_sele=" + care_sele +
                    ", top=" + top +
                    ", parentId='" + parentId + '\'' +
                    ", recommend='" + recommend + '\'' +
                    ", intentExtras=" + intentExtras +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIntentComponentPackage() {
            return intentComponentPackage;
        }

        public void setIntentComponentPackage(String intentComponentPackage) {
            this.intentComponentPackage = intentComponentPackage;
        }

        public String getIntentComponentClass() {
            return intentComponentClass;
        }

        public void setIntentComponentClass(String intentComponentClass) {
            this.intentComponentClass = intentComponentClass;
        }

        public String getIntentData() {
            return intentData;
        }

        public void setIntentData(String intentData) {
            this.intentData = intentData;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public String getApkVersionCode() {
            return apkVersionCode;
        }

        public void setApkVersionCode(String apkVersionCode) {
            this.apkVersionCode = apkVersionCode;
        }

        public String getTuiJianType() {
            return tuiJianType;
        }

        public void setTuiJianType(String tuiJianType) {
            this.tuiJianType = tuiJianType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public int getCare_sele() {
            return care_sele;
        }

        public void setCare_sele(int care_sele) {
            this.care_sele = care_sele;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public List<String> getIntentExtras() {
            return intentExtras;
        }

        public void setIntentExtras(List<String> intentExtras) {
            this.intentExtras = intentExtras;
        }

        private String intentExtrasStr;
        public String getIntentExtrasStr() {
            return intentExtrasStr;
        }

        public void setIntentExtrasStr(String intentExtrasStr) {
            this.intentExtrasStr = intentExtrasStr;
        }
    }
}
