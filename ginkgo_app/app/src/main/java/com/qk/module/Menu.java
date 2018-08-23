package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * 用户权限菜单
 */
public class Menu {

    private String name;

    private String parentName;

    private String id;

    private String parentId;

    private String level;

    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", parentName='" + parentName + '\'' +
                ", id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", level='" + level + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}