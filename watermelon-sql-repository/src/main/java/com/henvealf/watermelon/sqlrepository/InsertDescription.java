package com.henvealf.watermelon.sqlrepository;

import com.alibaba.fastjson.JSONObject;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-26
 */
public class InsertDescription {
    private String mapperName;
    private JSONObject param;
    private Object data;
    private boolean mutilInsert = false;

    public static InsertDescription create(String mapperName, JSONObject param, Object datas, boolean mutilInsert) {
        InsertDescription description = new InsertDescription();
        description.setMapperName(mapperName);
        description.setParam(param);
        description.setData(datas);
        description.setMutilInsert(mutilInsert);
        return description;
    }

    public static InsertDescription create(String mapperName, JSONObject param, Object datas) {
        return create(mapperName, param, datas,false);
    }

    public static InsertDescription create(String mapperName,  Object datas, boolean mutilInsert) {
        InsertDescription description = new InsertDescription();
        description.setMapperName(mapperName);
        description.setData(datas);
        description.setMutilInsert(mutilInsert);
        return description;
    }

    public static InsertDescription create(String mapperName,  Object datas) {
        return create(mapperName, datas,false);
    }

    public static InsertDescription create(String mapperName,  JSONObject param, boolean mutilInsert) {
        InsertDescription description = new InsertDescription();
        description.setMapperName(mapperName);
        description.setParam(param);
        description.setMutilInsert(mutilInsert);
        return description;
    }

    public static InsertDescription create(String mapperName,  JSONObject param) {
        return create(mapperName, param,false);
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

    public Object getData() {
        return data;
    }

    public void setData(Object datas) {
        this.data = datas;
    }

    public boolean isMutilInsert() {
        return mutilInsert;
    }

    public void setMutilInsert(boolean mutilInsert) {
        this.mutilInsert = mutilInsert;
    }
}
