/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.facade;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.models.Books;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Category;

/**
 *
 * @author User
 */
public class CategoryFacade implements Serializable {

    public CategoryFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) {
        if (category.getBooksList() == null) {
            category.setBooksList(new ArrayList<Books>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Books> attachedBooksList = new ArrayList<Books>();
            for (Books booksListBooksToAttach : category.getBooksList()) {
                booksListBooksToAttach = em.getReference(booksListBooksToAttach.getClass(), booksListBooksToAttach.getBookId());
                attachedBooksList.add(booksListBooksToAttach);
            }
            category.setBooksList(attachedBooksList);
            em.persist(category);
            for (Books booksListBooks : category.getBooksList()) {
                booksListBooks.getCategoryList().add(category);
                booksListBooks = em.merge(booksListBooks);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getCategoryId());
            List<Books> booksListOld = persistentCategory.getBooksList();
            List<Books> booksListNew = category.getBooksList();
            List<Books> attachedBooksListNew = new ArrayList<Books>();
            for (Books booksListNewBooksToAttach : booksListNew) {
                booksListNewBooksToAttach = em.getReference(booksListNewBooksToAttach.getClass(), booksListNewBooksToAttach.getBookId());
                attachedBooksListNew.add(booksListNewBooksToAttach);
            }
            booksListNew = attachedBooksListNew;
            category.setBooksList(booksListNew);
            category = em.merge(category);
            for (Books booksListOldBooks : booksListOld) {
                if (!booksListNew.contains(booksListOldBooks)) {
                    booksListOldBooks.getCategoryList().remove(category);
                    booksListOldBooks = em.merge(booksListOldBooks);
                }
            }
            for (Books booksListNewBooks : booksListNew) {
                if (!booksListOld.contains(booksListNewBooks)) {
                    booksListNewBooks.getCategoryList().add(category);
                    booksListNewBooks = em.merge(booksListNewBooks);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = category.getCategoryId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getCategoryId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            List<Books> booksList = category.getBooksList();
            for (Books booksListBooks : booksList) {
                booksListBooks.getCategoryList().remove(category);
                booksListBooks = em.merge(booksListBooks);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
