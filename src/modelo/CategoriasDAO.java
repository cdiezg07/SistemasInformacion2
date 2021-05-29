/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author maral
 */
public class CategoriasDAO {

    SessionFactory sf = null;
    Session sesion = null;
    List<Categorias> ListaEmpresas = null;
    List<String> ListaEmpresasNombre = null;
    Transaction tx = null;


    public List<Categorias> getCategoriasNombre(String nombre) {
        Categorias em=null;
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "FROM categorias e WHERE e.NombreCategoria=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", nombre);
            ListaEmpresas = query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ListaEmpresas;
    }

    public void add(Categorias em){
        List<Categorias> Em2 = getCategoriasNombre(em.getNombreCategoria());
        
        if(Em2==null){
            
            tx=sesion.beginTransaction();
            sesion.saveOrUpdate(em);
            tx.commit();
        }else{
        System.out.println("123456789"+Em2.toString());
        }
        
    }
}
