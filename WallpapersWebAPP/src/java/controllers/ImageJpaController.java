/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import objects.Image;

/**
 *
 * @author Pacho
 */
public class ImageJpaController implements Serializable {

    public ImageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Image image) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(image);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Image image) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            image = em.merge(image);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = image.getImgId();
                if (findImage(id) == null) {
                    throw new NonexistentEntityException("The image with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Image image;
            try {
                image = em.getReference(Image.class, id);
                image.getImgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The image with id " + id + " no longer exists.", enfe);
            }
            em.remove(image);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Image> findImageEntities() {
        return findImageEntities(true, -1, -1);
    }

    public List<Image> findImageEntities(int maxResults, int firstResult) {
        return findImageEntities(false, maxResults, firstResult);
    }

    private List<Image> findImageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Image.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Image findImage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Image.class, id);
        } finally {
            em.close();
        }
    }

    public int getImageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Image> rt = cq.from(Image.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Boolean existsByUrl(String link) {

        try {

            return !getEntityManager().createNamedQuery("Image.findByImgLink").setParameter("imgLink", link).getResultList().isEmpty();
        } finally {
            getEntityManager().close();
        }
    }

    public List<Image> getRandom() {
        try {

            return getEntityManager().createNativeQuery(
                    "select img_link from image order by RAND() limit 15").getResultList();
        } finally {
            getEntityManager().close();
        }
    }
    
    

}
