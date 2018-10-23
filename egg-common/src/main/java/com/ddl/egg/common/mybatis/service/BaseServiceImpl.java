package com.ddl.egg.common.mybatis.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ddl.egg.common.mybatis.domain.Page;
import com.ddl.egg.common.mybatis.domain.PageRequest;
import com.ddl.egg.common.mybatis.domain.Sort;
import com.ddl.egg.common.mybatis.service.api.BaseService;
import com.ddl.egg.common.util.DynamicCriteria;
import com.ddl.egg.common.util.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 公共基础服务实现类，实现所有基础接口
 */
public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected Mapper<T> mapper;

    protected Class<T> clazz;


    public BaseServiceImpl() {
        clazz = (Class<T>) ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }

    /************************
     * BaseQueryService
     ************************/
    @Override
    public T getEntity(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findList(Map<String, Object> searchParams) {
        return this.findList(searchParams, null);
    }

    @Override
    public List<T> findList(Map<String, Object> searchParams, Sort sort) {
        Example example = new Example(this.clazz);
        Example.Criteria criteria = example.createCriteria();
        Map filters = SearchFilter.parse(searchParams);
        DynamicCriteria.bySearchFilter(filters, criteria);
        if (sort != null) {
            example.setOrderByClause(sort.toString());
        }
        List list = this.mapper.selectByExample(example);
        return list;
    }

    @Override
    public Page<T> findPage(PageRequest pageRequest, Map<String, Object> searchParams) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        DynamicCriteria.bySearchFilter(filters, criteria);
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize(), pageRequest.isCountTotal());
        String orderBy = pageRequest.getSort() == null ? "" : pageRequest.getSort().toString();
        PageHelper.orderBy(orderBy);
        List<T> list = mapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        Page<T> page = new Page<T>(list, pageRequest, pageInfo.getTotal());
        return page;
    }

    public int countAll() {
        return this.mapper.selectCount(null);
    }

    public int count(T t) {
        return this.mapper.selectCount(t);
    }


    public int count(Map<String, Object> searchParams) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        DynamicCriteria.bySearchFilter(filters, criteria);
        int count = this.mapper.selectCountByExample(example);
        return count;
    }

    /************************
     * BaseOperateService
     ************************/
    @Override
    public int create(T entity) {
        return mapper.insertSelective(entity);
    }


    @Override
    public int update(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    /************************
     * BaseDeleteService
     ************************/
    @Override
    public int deleteByPrimaryKey(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }


    /************************
     * BaseCRUDService
     ************************/
    @Override
    public T getByPrimaryKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKey(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int save(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int delete(T entity) {
        if (entity == null) {
            return 0;
        }
        return mapper.delete(entity);
    }

    @Override
    public int delete(Iterable<? extends T> entities) {
        int n = 0;
        for (T entity : entities) {
            int dr = 0;
            if (entity != null) {
                dr = mapper.delete(entity);
            }
            n += dr;
        }
        return n;
    }

    @Override
    public int deleteByPrimaryKey(Object... keys) {
        List<?> list = Arrays.asList(keys);
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        String properyeName = getSinglePrimaryKeyName(clazz);
        if (properyeName != null) {
            criteria.andIn(properyeName, list);
            return mapper.deleteByExample(example);
        }
        return 0;
    }


    /**
     * 获取主键名称
     *
     * @param clazz
     * @return
     */
    public static String getSinglePrimaryKeyName(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String properyeName = null;
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            System.out.print(field.getName());
            if (id != null) {
                properyeName = field.getName();
                break;
            }
        }
        return properyeName;
    }

    static class aa {
        @Id
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /************************
     * PageService
     ************************/
    @Override
    public PageInfo<T> selectPaged(com.ddl.egg.common.dto.request.PageRequest page, Map<String, Object> params) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        Map<String, SearchFilter> filters = SearchFilter.parse(params);
        DynamicCriteria.bySearchFilter(filters, criteria);
        PageHelper.startPage(page.getPage(), page.getRows());
        if (StringUtils.isNotEmpty(page.getSort())) {
            page.setSort(StringUtil.camelhumpToUnderline(page.getSort()));
        }
        if (StringUtils.isNotEmpty(page.getSort()) && StringUtils.isNotEmpty(page.getOrder())) {
            String sort = page.getSort() + " " + page.getOrder();
            PageHelper.orderBy(sort);
        }

        List<T> list = mapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        return pageInfo;
    }

    /************************
     * SearchService
     ************************/
    @Override
    public List<T> query() {
        return mapper.selectAll();
    }

    @Override
    public List<T> query(Map<String, Object> params) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        Map<String, SearchFilter> filters = SearchFilter.parse(params);
        DynamicCriteria.bySearchFilter(filters, criteria);
        List<T> list = mapper.selectByExample(example);
        return list;
    }

    @Override
    public List<T> query(Map<String, Object> params, String orderByClause) {
        Example example = new Example(clazz);
        if (StringUtils.isNotBlank(orderByClause)) {
            example.setOrderByClause(orderByClause);
        }
        Example.Criteria criteria = example.createCriteria();
        Map<String, SearchFilter> filters = SearchFilter.parse(params);
        DynamicCriteria.bySearchFilter(filters, criteria);
        List<T> list = mapper.selectByExample(example);
        return list;
    }


}
