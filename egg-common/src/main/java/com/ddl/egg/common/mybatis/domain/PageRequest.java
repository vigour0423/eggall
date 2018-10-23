package com.ddl.egg.common.mybatis.domain;


import java.io.Serializable;

/**
 * 分页请求 PageRequest
 * <p>
 *
 * @author Oliver Gierke
 * @Reference org.springframework.data.domain.PageRequest
 */
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 8280485938848398236L;

    private int page;
    private int size;
    private Sort sort;
    private boolean countTotal = true;

    public PageRequest() {

    }

    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code domain} will return the first
     * domain.
     *
     * @param size
     * @param page
     */
    public PageRequest(int page, int size) {
        this(page, size, null);
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page
     * @param size
     * @param direction
     * @param properties
     */
    public PageRequest(int page, int size, Sort.Direction direction, String... properties) {

        this(page, size, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page
     * @param size
     * @param sort
     */
    public PageRequest(int page, int size, Sort sort) {

        if (1 > page) {
            throw new IllegalArgumentException("Page index must not be less than one!");
        }

        if (0 >= size) {
            throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
        }

        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public int getPageSize() {

        return size;
    }

    public int getPageNo() {

        return page;
    }


    public int getOffset() {

        return page * size;
    }


    public Sort getSort() {

        return sort;
    }

    /**
     * Checks if is count total.
     *
     * @return true, if is count total
     */
    public boolean isCountTotal() {
        return countTotal;
    }

    /**
     * Sets the count total.
     *
     * @param countTotal the new count total
     */
    public void setCountTotal(boolean countTotal) {
        this.countTotal = countTotal;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageRequest)) {
            return false;
        }

        PageRequest that = (PageRequest) obj;

        boolean pageEqual = this.page == that.page;
        boolean sizeEqual = this.size == that.size;

        boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);

        return pageEqual && sizeEqual && sortEqual;
    }


    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (null == sort ? 0 : sort.hashCode());

        return result;
    }
}
