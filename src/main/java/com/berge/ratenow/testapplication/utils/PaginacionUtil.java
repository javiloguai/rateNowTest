package com.berge.ratenow.testapplication.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


public class PaginacionUtil {

    public final static String listKey = "lista";
    public final static String totalKey = "total";

    public static Pageable getPageable(Integer page, Integer size, String sortDir, String sort) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        CustomPageRequest pageRequest = CustomPageRequest.of(page, size);
        if (sort != null) {
            //pageRequest = new CustomPageRequest(page, size, sortDir, sort);//
            pageRequest.setSortDirString(sortDir);
            pageRequest.setSortString(sort);
        }
        return pageRequest;
    }
    
    public static Pageable getPageableAndSortable(Integer page, Integer size, String sortDir, String sort) {
    	 if (page == null) {
             page = 0;
         }
         if (size == null) {
             size = 10;
         }
         if(sort != null) {
        	 return  PageRequest.of(page, size, (sortDir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending()));
         }else {
        	 return getPageable(page, size, sortDir, sort);
         }
    }

    public static Object readyForQuery(TypedQuery typedQuery, Pageable pageable) {
        if (pageable != null) {
            int startIndex = pageable.getPageSize() * (pageable.getPageNumber() - 1);
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        return typedQuery;
    }

    public static int[] subListIndex(Pageable pageable, int total){
        int[] indexs = new int[] {0, 0};
        if (pageable != null) {
            int startIndex = pageable.getPageSize() * (pageable.getPageNumber() - 1);
            indexs[0] = startIndex;
            indexs[1] = startIndex + pageable.getPageSize();
        }
        if (indexs[1] > total) {
            indexs[1] = total;
            if (total == 1) {
                indexs[1] = 1;
            }
        }
        if (indexs[1] == -1) {
            indexs[1] = 0;
        }
        return indexs;
    }
    
    public static Object readyForQuery(Query query, Pageable pageable) {
        if (pageable != null) {
            int startIndex = pageable.getPageSize() * (pageable.getPageNumber() - 1);
            query.setFirstResult(startIndex);
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }

    public static CriteriaQuery addOrder(Pageable _pageable, CriteriaQuery criteriaQuery, Root root, CriteriaBuilder builder) {
        if (_pageable != null) {
            CustomPageRequest pageable = (CustomPageRequest) _pageable;
            if (pageable.getSortString() != null && !pageable.getSortString().equals("")) {
                String[] sorts = pageable.getSortString().split(",");
                String[] orders = pageable.getSortDirString().split(",");
                List<Order> criteriaOrders = new ArrayList<>();
                if (sorts.length == orders.length) {
                    String order;
                    for (int i = 0; i < orders.length; i++) {
                        order = orders[i];
                        criteriaOrders.add(order.toLowerCase().equals("asc") ? builder.asc(root.get(sorts[i].trim())) : builder.desc(root.get(sorts[i].trim())));
                    }
                } else {
                    for (String sort : sorts) {
                        criteriaOrders.add(orders[0].toLowerCase().equals("asc") ? builder.asc(root.get(sort.trim())) : builder.desc(root.get(sort.trim())));
                    }
                }
                criteriaQuery = criteriaQuery.orderBy(criteriaOrders);
            }
        }
        return criteriaQuery;
    }


}

