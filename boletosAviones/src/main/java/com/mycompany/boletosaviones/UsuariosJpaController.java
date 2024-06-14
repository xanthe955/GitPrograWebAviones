/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.boletosaviones;

import com.mycompany.boletosaviones.exceptions.NonexistentEntityException;
import com.mycompany.boletosaviones.exceptions.RollbackFailureException;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author darkx
 */
public class UsuariosJpaController implements Serializable {

    @Inject
    public UsuariosJpaController() {

    }

    public UsuariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @Resource
    private UserTransaction utx;
    @PersistenceUnit(unitName = "Vuelos_y_Boletos_Persistentes")
    private EntityManagerFactory emf;
    @PersistenceContext(unitName = "Vuelos_y_Boletos_Persistentes")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws RollbackFailureException, Exception {
        if (usuarios.getClientesCollection() == null) {
            usuarios.setClientesCollection(new ArrayList<Clientes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Clientes> attachedClientesCollection = new ArrayList<Clientes>();
            for (Clientes clientesCollectionClientesToAttach : usuarios.getClientesCollection()) {
                clientesCollectionClientesToAttach = em.getReference(clientesCollectionClientesToAttach.getClass(), clientesCollectionClientesToAttach.getId());
                attachedClientesCollection.add(clientesCollectionClientesToAttach);
            }
            usuarios.setClientesCollection(attachedClientesCollection);
            em.persist(usuarios);
            for (Clientes clientesCollectionClientes : usuarios.getClientesCollection()) {
                Usuarios oldUsuarioIdOfClientesCollectionClientes = clientesCollectionClientes.getUsuarioId();
                clientesCollectionClientes.setUsuarioId(usuarios);
                clientesCollectionClientes = em.merge(clientesCollectionClientes);
                if (oldUsuarioIdOfClientesCollectionClientes != null) {
                    oldUsuarioIdOfClientesCollectionClientes.getClientesCollection().remove(clientesCollectionClientes);
                    oldUsuarioIdOfClientesCollectionClientes = em.merge(oldUsuarioIdOfClientesCollectionClientes);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            Collection<Clientes> clientesCollectionOld = persistentUsuarios.getClientesCollection();
            Collection<Clientes> clientesCollectionNew = usuarios.getClientesCollection();
            Collection<Clientes> attachedClientesCollectionNew = new ArrayList<Clientes>();
            for (Clientes clientesCollectionNewClientesToAttach : clientesCollectionNew) {
                clientesCollectionNewClientesToAttach = em.getReference(clientesCollectionNewClientesToAttach.getClass(), clientesCollectionNewClientesToAttach.getId());
                attachedClientesCollectionNew.add(clientesCollectionNewClientesToAttach);
            }
            clientesCollectionNew = attachedClientesCollectionNew;
            usuarios.setClientesCollection(clientesCollectionNew);
            usuarios = em.merge(usuarios);
            for (Clientes clientesCollectionOldClientes : clientesCollectionOld) {
                if (!clientesCollectionNew.contains(clientesCollectionOldClientes)) {
                    clientesCollectionOldClientes.setUsuarioId(null);
                    clientesCollectionOldClientes = em.merge(clientesCollectionOldClientes);
                }
            }
            for (Clientes clientesCollectionNewClientes : clientesCollectionNew) {
                if (!clientesCollectionOld.contains(clientesCollectionNewClientes)) {
                    Usuarios oldUsuarioIdOfClientesCollectionNewClientes = clientesCollectionNewClientes.getUsuarioId();
                    clientesCollectionNewClientes.setUsuarioId(usuarios);
                    clientesCollectionNewClientes = em.merge(clientesCollectionNewClientes);
                    if (oldUsuarioIdOfClientesCollectionNewClientes != null && !oldUsuarioIdOfClientesCollectionNewClientes.equals(usuarios)) {
                        oldUsuarioIdOfClientesCollectionNewClientes.getClientesCollection().remove(clientesCollectionNewClientes);
                        oldUsuarioIdOfClientesCollectionNewClientes = em.merge(oldUsuarioIdOfClientesCollectionNewClientes);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Collection<Clientes> clientesCollection = usuarios.getClientesCollection();
            for (Clientes clientesCollectionClientes : clientesCollection) {
                clientesCollectionClientes.setUsuarioId(null);
                clientesCollectionClientes = em.merge(clientesCollectionClientes);
            }
            em.remove(usuarios);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void getUsuariosData(String nom, String pw) {
        //tenemos que llamar a la funcion de NamedQuery de la clase Usuarios que define la tabla de la base de datos
        //o hacer el SELECT a mano
        EntityManager em = emf.createEntityManager();
        TypedQuery<Usuarios> consultaUserNombrePw = em.createNamedQuery(
                "Usuarios.findByNombreAndPassword", Usuarios.class);
        consultaUserNombrePw.setParameter("nombre", nom);
        consultaUserNombrePw.setParameter("password", pw);
        Usuarios u = consultaUserNombrePw.getSingleResult();

    }

    public Usuarios findUsuariosByNombreAndPassword(String nombre, String password) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuarios> query = em.createNamedQuery("Usuarios.findByNombreAndPassword", Usuarios.class);
            query.setParameter("nombre", nombre);
            query.setParameter("password", password);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public void registerUser(String nombre, String email, String pw, String rol) {
//        EntityManager em = getEntityManager();
        try {
//            em.getTransaction().begin();
            utx.begin();
            System.out.println("lo que tiene em es: " + em);
            Usuarios usuario = new Usuarios();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(pw);
            usuario.setRol(rol);
            em.persist(usuario);
            utx.commit();
//            em.getTransaction().commit();
        } catch (Exception e) {
            try {
                System.out.println("excepción register user:" + e.getMessage());
                utx.rollback();
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
                throw new RuntimeException(e);
            } catch (IllegalStateException ex) {
                Logger.getLogger(UsuariosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(UsuariosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(UsuariosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            em.close();
        }
    }

}
