package cz.jlochman.stahovatko.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;

public class HibernateDrugDAO implements DrugDAO {

	private EntityManager em;

	public HibernateDrugDAO() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("DrugPersistenceUnit");
		em = emf.createEntityManager();
	}

	public void persistDrugItem(DrugItem drugItem) {
		em.getTransaction().begin();
		em.persist(drugItem);
		em.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public DrugFile getLastFile(DrugFile newFile) {
		Query query = em.createQuery("FROM DrugFile df WHERE df.fileMD5 = :md5 AND df.fileSize = :size");
		query.setParameter("md5", newFile.getFileMD5() );
		query.setParameter("size", newFile.getFileSize() );
		List<DrugFile> fileList = query.getResultList();
		if (fileList == null || fileList.isEmpty())
			return new DrugFile();
		else return fileList.get(0);
	}



}