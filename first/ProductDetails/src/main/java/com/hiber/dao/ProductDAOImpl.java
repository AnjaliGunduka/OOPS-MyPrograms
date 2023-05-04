package com.hiber.dao;

import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hiber.model.Product;

@Repository
public class ProductDAOImpl implements ProductDAO {
	@Autowired
	SessionFactory factory;

	@Transactional
	@SuppressWarnings({ "unchecked", "finally" })
	public List getAllProducts() {
		List products = null;
		try {

			Session session = factory.getCurrentSession();
			Query q = session.createQuery("from Product");
			products = q.getResultList();

		} catch (Exception e) {
		} finally {
			return products;
		}

	}

	@Transactional
	public boolean addProduct(Product product) {

		Session session = factory.getCurrentSession();
		session.save(product);
		return true;
	}

	@Transactional
	public boolean deleteProduct(int productId) {

		Session session = factory.getCurrentSession();
		Product p = session.get(Product.class, productId);
		session.delete(p);

		return true;
	}

}
