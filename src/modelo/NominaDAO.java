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
 * @author carlos
 */
public class NominaDAO {

    SessionFactory sf = null;
    Session sesion = null;
    List<Nomina> ListaNomina = null;
   
    public void eliminarNominas(int idTrabajador){
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            
            Transaction t = sesion.beginTransaction();
            
            String consultaHQL = "DELETE Nomina n WHERE n.trabajadorbbdd.idTrabajador=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", idTrabajador);
            query.executeUpdate();
            
            t.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
