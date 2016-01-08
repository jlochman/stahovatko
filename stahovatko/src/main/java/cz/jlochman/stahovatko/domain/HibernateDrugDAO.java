package cz.jlochman.stahovatko.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class HibernateDrugDAO implements DrugDAO {

	private EntityManager em;
	
	public HibernateDrugDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("DrugPersistenceUnit");
	    em = emf.createEntityManager();
	}
	
	public DrugItem getDrugByCode(String code) {
		/*
		Query query = em.createQuery("FROM DrugItem AS di WHERE di.code = :code");
		query.setParameter("code", code);
		
		try {
			return (DrugItem) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		*/
		return null;
	}

	public void persistDrugItem(DrugItem drugItem) {
		em.getTransaction().begin();
		em.persist(drugItem);
		em.getTransaction().commit();
	}

	public List<DrugItem> getAllDrugItems() {
		/*
		Query query = em.createQuery("FROM DrugItem");
	    return (List<DrugItem>) query.getResultList();
	    */	
		return null;
	}


}
