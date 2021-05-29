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
 * @author maral
 */
public class EmpresasDAO {

    SessionFactory sf = null;
    Session sesion = null;
    List<Empresas> ListaEmpresas = null;
    List<String> ListaEmpresasNombre = null;
    Transaction tx = null;

    public List<Empresas> getEmpresas(int IdEmpresa) {

        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "from empresas e WHERE e.idEmpresa=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", IdEmpresa);
            ListaEmpresas = query.list();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ListaEmpresas;
    }

    public List<Empresas> getEmpresasCIF(String CIF) {
        Empresas em=null;
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            String consultaHQL = "from Empresas e WHERE e.CIF!=:param1";
            Query query = sesion.createQuery(consultaHQL).setParameter("param1", CIF);
            ListaEmpresas = query.list();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ListaEmpresas;
    }

    public void add(Empresas em){
        List<Empresas> Em2= getEmpresasCIF(em.getCif());
        if(Em2==null){
            tx=sesion.beginTransaction();
            sesion.saveOrUpdate(em);
            tx.commit();
        }
    }
    public void editarEmpresas2021(List<Empresas> ListaEmpresas) {
        try {
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            for (Empresas le : ListaEmpresas) {
                Transaction t = sesion.beginTransaction();
                Empresas emp = (Empresas) sesion.load(Empresas.class, le.getIdEmpresa());
                emp.setNombre(emp.getNombre() + "2021");
                sesion.saveOrUpdate(emp);
                t.commit();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}