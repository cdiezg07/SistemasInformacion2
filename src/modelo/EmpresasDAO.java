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
 * @author maral
 */
public class EmpresasDAO {
    SessionFactory sf = null;
    Session sesion = null;
    List<Empresas> ListaEmpresas = null;
    List<String> ListaEmpresasNombre = null;
    public List<String> getEmpresas() {

        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "select Nombre from Empresas";
            Query query = sesion.createQuery(consultaHQL);
            ListaEmpresasNombre = query.list();
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ListaEmpresasNombre;
    }
     public void editarEmpresas2021(int Id){
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            
            Transaction t = sesion.beginTransaction();
            
            String consultaHQL = "UPDATE Empresas SET Nombre = CONCAT(Nombre, '2021') where (IdEmpresa!=:param1);";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", Id);
            query.executeUpdate();
            
            t.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
