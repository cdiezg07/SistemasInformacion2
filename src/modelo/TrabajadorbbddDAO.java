/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.*;
import modelo.Categorias;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.Trabajadorbbdd;
import org.hibernate.Hibernate;
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

}
