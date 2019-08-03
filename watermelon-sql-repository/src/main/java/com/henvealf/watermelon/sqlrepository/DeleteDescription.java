package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-29
 */
public class DeleteDescription {

    private String mapperName;
    private JSONObject param;

    public static DeleteDescription create(String mapperName, JSONObject param) {
        DeleteDescription deleteDescription = new DeleteDescription();
        deleteDescription.setMapperName(mapperName);
        deleteDescription.setParam(param);
        return deleteDescription;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }
}
