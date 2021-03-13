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
import java.util.*;
import modelo.Categorias;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.NominaDAO;
import modelo.Trabajadorbbdd;
import modelo.TrabajadorbbddDAO;

/**
 *
 * @author carlos
 */
public class NominaDAO {

    SessionFactory sf = null;
    Session sesion = null;
    List<Nomina> ListaNomina = null;

    public List<Nomina> getNominas(int idTrabajador) {

        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "Select n From Nomina n WHERE n.trabajadorbbdd.idTrabajador=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", idTrabajador);
            ListaNomina = query.list();

            HibernateUtil.shutdown();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ListaNomina;
    }
}
