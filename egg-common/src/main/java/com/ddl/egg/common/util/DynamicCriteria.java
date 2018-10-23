package com.ddl.egg.common.util;

import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public class DynamicCriteria {

    public static void bySearchFilter(Map<String, SearchFilter> filters,Criteria criteria){
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, SearchFilter> entry : filters.entrySet()) {
                SearchFilter filter = entry.getValue();
                switch (filter.operator) {
                    case EQ:
                        criteria.andEqualTo(filter.fieldName, filter.value);
                        break;
                    case NOTEQ:
                    criteria.andNotEqualTo(filter.fieldName, filter.value);
                    break;
                    case LIKE:
                        String value = "%"+filter.value.toString()+"%";
                        criteria.andLike(filter.fieldName, value);
                        break;
                    case NOTLIKE:
                        criteria.andNotLike(filter.fieldName,
                                filter.value.toString());
                        break;
                    case GT:
                        criteria.andGreaterThan(filter.fieldName, filter.value);
                        break;
                    case LT:
                        criteria.andLessThan(filter.fieldName, filter.value);
                        break;
                    case GTE:
                        criteria.andGreaterThanOrEqualTo(filter.fieldName,
                                filter.value);
                        break;
                    case LTE:
                        criteria.andLessThanOrEqualTo(filter.fieldName,
                                filter.value);
                        break;
                    case ISNULL:
                        criteria.andIsNull(filter.fieldName);
                        break;
                    case ISNOTNULL:
                        criteria.andIsNotNull(filter.fieldName);
                        break;
                    case NOTIN:
                        if(filter.value instanceof List){
                            List list = (List)filter.value;
                            criteria.andNotIn(filter.fieldName,list);
                        }
                        break;
                    case IN:
                        if(filter.value instanceof  List){
                            List list = (List)filter.value;
                            criteria.andIn(filter.fieldName,list);
                        }
                        break;
                }
            }
        }
    }
}
