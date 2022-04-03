 
 
package com.berge.ratenow.testapplication.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;

import com.berge.ratenow.testapplication.utils.PaginacionUtil;

/**
 * @author jruizh
 *
 * @param <Entidad>
 */
public abstract class GenericDAO<Entidad> {

	protected CriteriaBuilder builder;
	protected CriteriaQuery<Entidad> query;
	protected Root<Entidad> r;
	protected Predicate predicate;
	protected CriteriaQuery<Long> cq;

	public void inicializar(Class<Entidad> clase) {
		builder = getEntityManager().getCriteriaBuilder();
		query = builder.createQuery(clase);
		r = query.from(clase);
		predicate = builder.conjunction();
		cq = builder.createQuery(Long.class);
	}

	@SuppressWarnings("All")
	public Map<String, Object> list(Pageable pageable, Class<Entidad> clase) {
		cq.select(builder.count(cq.from(clase)));
		getEntityManager().createQuery(cq);
		cq.where(predicate);
		Long count = getEntityManager().createQuery(cq).getSingleResult();

		query = PaginacionUtil.addOrder(pageable, query, r, builder);
		query.where(predicate);
		TypedQuery<Entidad> typedQuery = (TypedQuery<Entidad>) PaginacionUtil
				.readyForQuery(getEntityManager().createQuery(query), pageable);
		List<Entidad> lista = typedQuery.getResultList();

		Map<String, Object> map = new HashMap<>();
		map.put(PaginacionUtil.listKey, lista);
		map.put(PaginacionUtil.totalKey, count);
		return map;
	}

	public abstract EntityManager getEntityManager();

	public abstract Entidad getNewInstance();
}
