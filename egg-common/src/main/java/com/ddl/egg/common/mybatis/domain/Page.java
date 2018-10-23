/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ddl.egg.common.mybatis.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Basic Page
 * <p>
 * @Reference org.springframework.data.domain.Page
 *
 * @author Oliver Gierke
 */
public class Page<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 867755909294344406L;

    private List<T> content = new ArrayList<T>();
    private long total;
    private PageRequest pageRequest;


    /**
     * 构造函数
     *
     */
    public Page() {

    }

    /**
     * 构造函数
     *
     * @param content     必须不为空
     * @param pageRequest 分页请求，可以为空
     * @param total       数据总量
     */
    public Page(List<T> content, PageRequest pageRequest, long total) {
        if (null == content) {
            throw new IllegalArgumentException("Content must not be null!");
        }
        this.content.addAll(content);
        this.total = total;
        this.pageRequest = pageRequest;
    }

    /**
     * 构造函数
     *
     * @param content 必须不为空
     */
    public Page(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }


    /**
     * 获取当前页码
     *
     * @return
     */
    public int getPageNo() {
        return pageRequest == null ? 1 : pageRequest.getPageNo();
    }

    /**
     * 获取每页记录条数
     *
     * @return
     */
    public int getPageSize() {
        return pageRequest == null ? 0 : pageRequest.getPageSize();
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotalPages() {
        return getPageSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getPageSize());
    }

    /**
     * 获取总数据条数
     *
     * @return
     */
    public long getTotalElements() {
        return total;
    }

    /**
     * 是否有上一页
     *
     * @return
     */
    public boolean hasPreviousPage() {
        return getPageNo() > 0;
    }

    /**
     * 是否是首页
     *
     * @return
     */
    public boolean isFirstPage() {
        return !hasPreviousPage();
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean hasNextPage() {
        return (getPageNo() + 1) * getPageSize() < total;
    }

    /**
     * 是否是最后一页
     *
     * @return
     */
    public boolean isLastPage() {
        return !hasNextPage();
    }

    /**
     * 获取当前页数据Iterator
     *
     * @return
     */
    public Iterator<T> iterator() {
        return content.iterator();
    }

    /**
     * 获取当前页数据List
     *
     * @return
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /**
     * 是否有数据
     *
     * @return
     */
    public boolean hasContent() {
        return !content.isEmpty();
    }

    /**
     * 获取页面排序规则
     *
     * @return
     */
    public Sort getSort() {
        return pageRequest == null ? null : pageRequest.getSort();
    }

    @Override
    public String toString() {

        String contentType = "UNKNOWN";

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getPageNo(), getTotalPages(), contentType);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Page<?>)) {
            return false;
        }

        Page<?> that = (Page<?>) obj;

        boolean totalEqual = this.total == that.total;
        boolean contentEqual = this.content.equals(that.content);
        boolean pageRequestEqual = this.pageRequest == null ? that.pageRequest == null : this.pageRequest.equals(that.pageRequest);

        return totalEqual && contentEqual && pageRequestEqual;
    }

    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + (int) (total ^ total >>> 32);
        result = 31 * result + (pageRequest == null ? 0 : pageRequest.hashCode());
        result = 31 * result + content.hashCode();

        return result;
    }
}
