package com.henvealf.watermelon.sqlrepository;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-29
 */
public class UpdateDescription {

    private String mapperName;
    private Object data;
    private boolean mutilUpdate = false;

    public static UpdateDescription create(String mapperName, Object param, boolean mutilUpdate) {
        UpdateDescription deleteDescription = new UpdateDescription();
        deleteDescription.setMapperName(mapperName);
        deleteDescription.setData(param);
        deleteDescription.setMutilUpdate(mutilUpdate);
        return deleteDescription;
    }

    public static UpdateDescription create(String mapperName, Object param) {
        return create(mapperName, param, false);
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isMutilUpdate() {
        return mutilUpdate;
    }

    public void setMutilUpdate(boolean mutilUpdate) {
        this.mutilUpdate = mutilUpdate;
    }
}
