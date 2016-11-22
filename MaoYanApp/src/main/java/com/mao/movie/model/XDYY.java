/**
 * Date:2016年7月2日上午10:31:40
 * Copyright (c) 2016, benchone@163.com All Rights Reserved.
 */
package com.mao.movie.model;

public class XDYY {
    private String code;
    private String source;
    private String request_url;
    private String message;
    private Result result;

    public class Result {
        private String files;
        private filelist filelist[];
        private String play_type;
        private String definition;
        private String definitionList;
        private String cost;

        public String getFiles() {
            return files;
        }

        public void setFiles(String files) {
            this.files = files;
        }

        public filelist[] getFilelist() {
            return filelist;
        }

        public void setFilelist(filelist[] filelist) {
            this.filelist = filelist;
        }

        public String getPlay_type() {
            return play_type;
        }

        public void setPlay_type(String play_type) {
            this.play_type = play_type;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getDefinitionList() {
            return definitionList;
        }

        public void setDefinitionList(String definitionList) {
            this.definitionList = definitionList;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

    }

    public class filelist {
        private String url;
        private String ext;
        private String size;
        private String name;
        private String[] files_download;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getFiles_download() {
            return files_download;
        }

        public void setFiles_download(String[] files_download) {
            this.files_download = files_download;
        }

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
