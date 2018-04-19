package com.syh.springboot.jdbc.demo.model;

import java.util.Date;

public class DbTag {

    private Long id;

    private String tagName;

    private Long versions;

    private Date createTime;

    public DbTag(String tagName, Long versions, Date createTime) {
        this.tagName = tagName;
        this.versions = versions;
        this.createTime = createTime;
    }

    public DbTag(Long id, String tagName, Long versions, Date createTime) {
        this.id = id;
        this.tagName = tagName;
        this.versions = versions;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public Long getVersions() {
        return versions;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setVersions(Long versions) {
        this.versions = versions;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DbTags{"
                + "id=" + id
                + ", tagName='" + tagName + '\''
                + ", versions='" + versions + '\''
                + ", createTime=" + createTime
                + '}';
    }
}
