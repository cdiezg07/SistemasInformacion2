/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author carlos
 */
public class TrabajadorbbddDAO {

    SessionFactory sf = null;
    Session sesion = null;
    Transaction tx = null;
    Trabajadorbbdd tbd1;
    List<Trabajadorbbdd> ListaIdTrabajadoresToDelete = null;

    public Trabajadorbbdd buscarTrabajador(String dni) {

        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();

            //Con esta consulta estaria trayendo todas los datos de todas las tabas de la base de datos
            String consultaHQL = "FROM Trabajadorbbdd n WHERE n.nifnie=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", dni);
            List<Trabajadorbbdd> ListaResultado = query.list();
            
            for (Trabajadorbbdd tbd : ListaResultado) {
                tbd1 = tbd;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return tbd1;
    }
    public void add(Trabajadorbbdd tbd3){
        Trabajadorbbdd tbd2=buscarTrabajador(tbd3.getNifnie());
        if(tbd2==null){
            
            tx=sesion.beginTransaction();
            sesion.saveOrUpdate(tbd3);
            tx.commit();
        }else if(!tbd3.getFechaAlta().equals(tbd2.getFechaAlta()) && !tbd3.getNombre().equals(tbd2.getNombre())){
            
            tx=sesion.beginTransaction();
            sesion.saveOrUpdate(tbd3);
            tx.commit();
        }        
    }
    public List<Trabajadorbbdd> getTrabajadoresToDelete(int idEmpresa) {
        
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "FROM Trabajadorbbdd t WHERE t.empresas.idEmpresa!=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", idEmpresa);
            ListaIdTrabajadoresToDelete = query.list();

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ListaIdTrabajadoresToDelete; 
    }
    public void eliminarTrabajador(int idEmpresa){
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            
            Transaction t = sesion.beginTransaction();
            
            String consultaHQL = "DELETE FROM Trabajadorbbdd WHERE empresas.idEmpresa!=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", idEmpresa);
            query.executeUpdate();
            
            t.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
