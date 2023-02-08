package com.ishlaw.crudservice.repositories;

import com.ishlaw.crudservice.model.ReportResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class GenericCrudService implements CrudService {

    private static final Logger LOG = LogManager.getLogger(GenericCrudService.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    @PostConstruct
    protected void init() {
        LOG.info("Initializing crud service...");
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        LOG.info("Crud service initialized. Sessionfactory properties {}", entityManagerFactory.getProperties());
    }

    @Override
    public <T> T findEntity(Serializable primaryKey, Class<T> clazz) throws HibernateException {

        Session session = null;
        try {
            session = sessionFactory.openSession();
            IdentifierLoadAccess loadAccess = session.byId(clazz);
            return (T) loadAccess.load(primaryKey);
        } catch (Exception e) {
            LOG.error("Failed to find entity {} by id {}. {}", clazz, primaryKey, e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public <T> List<T> fetchWithNamedQuery(String queryName, Map<String, Object> params) {
        return fetchWithNamedQuery(queryName, params, 0, DEFAULT_PAGE_SIZE);
    }

    @Override
    public <T> List<T> fetchWithHibernateQuery(String query, Map<String, Object> params) throws HibernateException {
        return fetchWithHibernateQuery(query, params, 0, DEFAULT_PAGE_SIZE);
    }

    @Override
    public <T> T fetchWithHibernateQuerySingleResult(String query, Map<String, Object> params) throws HibernateException {
        List<T> results = fetchWithHibernateQuery(query, params, 0, 1);
        return results.isEmpty() ? null : results.iterator().next();
    }

    @Override
    public <T> List<T> fetchWithHibernateQuery(String query, Map<String, Object> params, int start, int end) throws HibernateException {
        LOG.debug("Executing Hibernate={}, start={},end={} params=[{}]", query, start, end, params);
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query q = session.createQuery(query);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });
            start = start < 0 ? 0 : start;
            if (end >= start && start >= 0) {
                q.setFirstResult(start);
                q.setMaxResults(end - start);
            }
            return q.<T>list();
        } catch (Exception e) {
            LOG.error("Failed in executing hibernate query {} with params [{}]. {}.", query, params, e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public <T> Object fetchWithHibernateQuerySingleResult(String query, Map<String, Object> params, int start, int end) throws HibernateException {
        LOG.debug("Executing Hibernate={}, start={},end={} params=[{}]", query, start, end, params);

        Session session = null;
        try {
            session = sessionFactory.openSession();

            Query q = session.createQuery(query);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });

            return q.getFirstResult();
        } catch (Exception e) {
            LOG.error("Failed in executing hibernate query {} with params [{}]. {}.", query, params, e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public int executeHibernateQuery(String queryString, Map<String, Object> params) throws HibernateException {
        LOG.debug("Executing Hibernate={}, params=[{}]", queryString, params);
        Transaction tx = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query q = session.createQuery(queryString);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });

            int executeUpdate = q.executeUpdate();
            tx.commit();
            return executeUpdate;
        } catch (Exception e) {
            LOG.error("Failed in executing hibernate query {} with params [{}]. {}.", queryString, params, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public int executeNativeQuery(String queryString, Map<String, Object> params) throws HibernateException {
        LOG.debug("Executing Hibernate={}, params=[{}]", queryString, params);
        Transaction tx = null;

        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query q = session.createNativeQuery(queryString);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });

            int executeUpdate = q.executeUpdate();
            tx.commit();
            return executeUpdate;
        } catch (Exception e) {
            LOG.error("Failed in executing hibernate query {} with params [{}]. {}.", queryString, params, e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * @param <T>
     * @param query
     * @param start
     * @param end
     * @return
     * @throws HibernateException
     */
    @Override
    public <T> List<T> fetchWithNativeQuery(String query, Map<String, Object> params, int start, int end) throws HibernateException {
        LOG.debug("Executing nativeQuery={}, start={},end={} params=[{}]", query, start, end, params);

        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query q = session.createNativeQuery(query);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });
            start = start < 0 ? 0 : start;
            if (end >= start && start >= 0) {
                q.setFirstResult(start);
                q.setMaxResults(end - start);
            }


            return q.<T>list();



        } catch (Exception e) {
            LOG.error("Failed in executing hibernate query {} with params [{}]. {}.", query, e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ReportResponse fetchWithNativeQueryResultMapPageable(String query, String qcount, String mapper, Map<String, Object> params, int pageNumber, int pageSize) throws HibernateException {
        LOG.info("Executing nativeQuery={}, mapper={}, pageNumber={},pageSize={} params=[{}]", query,mapper, pageNumber, pageSize, params);
        try (Session session = sessionFactory.openSession()) {
            //Query to fetch data
            Query q = session.createNativeQuery(query,mapper);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });

            pageNumber += 1;
                /*setFirstResult(int): Sets the offset position in the result set to start pagination
                 setMaxResults(int): Sets the maximum number of entities that should be included in the page
                  */
            q.setFirstResult((pageNumber-1) * pageSize);
            q.setMaxResults(pageSize);
            int count =q.list().size();
            //Query to count data matching the criteria
            Query qcQuery = session.createNativeQuery(qcount);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    qcQuery.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    qcQuery.setParameter(param.getKey(), param.getValue());
                }
            });
            LOG.info("Total count {}",qcQuery.getSingleResult());
            BigDecimal countResults = (BigDecimal) qcQuery.getSingleResult();
            int totalPages = (int) (Math.ceil((double)countResults.intValue() / pageSize));


            ReportResponse p=new ReportResponse();
            ReportResponse.pageProfile pageProf=new ReportResponse.pageProfile();

            p.setProfile(pageProf);
            p.getProfile().setTotalPages(totalPages);
            //for the portal @Sam
            p.getProfile().setPageNumber(pageNumber-1);
            p.getProfile().setPageSize(count);
            p.getProfile().setTotalElements(countResults.intValue());
            p.setEntity(q.list());
            //Check whether is the last page or not @Alaaa!!!
            if(p.getEntity().size() < pageSize){
                p.getProfile().setLast(true);
                p.getProfile().setFirst(false);
            }else{
                p.getProfile().setLast(false);
                p.getProfile().setFirst(true);
            }

            return p;
        } catch (HibernateException e) {
            LOG.error("Failed in executing pagination native query {} with params [{}]. {}.", query, e.getMessage());
            throw e;
        }

    }

    @Override
    public <T> List<T> fetchWithNativeQueryResultMap(String query, String mapper, Map<String, Object> params, int start, int end) throws HibernateException {
        LOG.debug("Executing nativeQuery={}, mapper={}, start={},end={} params=[{}]", query,mapper, start, end, params);
        try (Session session = sessionFactory.openSession()) {
            Query q = session.createNativeQuery(query,mapper);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    q.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    q.setParameter(param.getKey(), param.getValue());
                }
            });
            start = start < 0 ? 0 : start;
            if (end >= start && start >= 0) {
                q.setFirstResult(start);
                q.setMaxResults(end - start);
            }
            return q.<T>list();
        } catch (HibernateException e) {
            LOG.error("Failed in executing native query {} with params [{}]. {}.", query, e.getMessage());
            throw e;
        }

    }


    @Override
    public <T> List<T> fetchWithNamedQuery(String queryName, Map<String, Object> params, int start, int end) {
        LOG.debug("Executing NamedQuery={}, start={},end={} params=[{}]", queryName, start, end, params);

        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query query = session.getNamedQuery(queryName);
            params.entrySet().forEach((param) -> {
                if (param.getValue() instanceof Collection) {
                    query.setParameterList(param.getKey(), (Collection) param.getValue());
                } else {
                    query.setParameter(param.getKey(), param.getValue());
                }
            });
            start = start < 0 ? 0 : start;
            if (end >= start && start >= 0) {
                query.setFirstResult(start);
                query.setMaxResults(end - start);
            }
            return query.<T>list();
        } catch (Exception e) {
            LOG.error("Failed in executing named query {} with params [{}]. {}.", queryName, params, e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public <T> void saveOrUpdate(T entity) throws HibernateException {
        LOG.debug("Perist or merge {}", entity);
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.getTransaction();
            tx.begin();
            session.saveOrUpdate(entity);
            tx.commit();
        } catch (Exception e) {
            LOG.error("Failed to persist or merge {}. {}", entity, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public <T> void saveOrUpdate(List<T> entities) throws HibernateException {
        LOG.debug("Perist or merge {} entities.", entities.size());
        Transaction tx = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.getTransaction();
            tx.begin();
            for (T entity : entities) {
                session.saveOrUpdate(entity);
            }
            tx.commit();
        } catch (Exception e) {
            LOG.error("Failed to persist or merge {} entities. {}", entities.size(), e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public <T> Object save(T entity) throws HibernateException, ConstraintViolationException {
        LOG.debug("Perist or merge {}", entity);
        Transaction tx = null;
        Object savedEntity = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.getTransaction();
            tx.begin();
            savedEntity = session.save(entity);
            tx.commit();
        } catch (ConstraintViolationException e) {
            LOG.error("Constraint validation failed {}. {}", entity, e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } catch (RollbackException e) {
            LOG.error("RollbackException validation failed {}. {}", entity, e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to persist or merge {}. {}", entity, e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return savedEntity;
    }

    @Override
    public <T> void remove(T entity) throws HibernateException {
        LOG.debug("Deleting {}", entity);
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.getTransaction();
            tx.begin();
            session.delete(entity);
            tx.commit();
        } catch (Exception e) {
            LOG.error("Failed to delete {}. {}", entity, e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

