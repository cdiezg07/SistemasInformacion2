/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


/**
 *
 * @author maral
 */
public class bbddDAO {
    SessionFactory sf = null;
    Session sesion = null;
    Transaction tx = null;
    Trabajadorbbdd tbd1;
    List<Trabajadorbbdd> ListaIdTrabajadoresToDelete = null;
    List<Empresas> ListaEmpresas = null;
    List<String> ListaEmpresasNombre = null;
    List<Categorias> ListaCategorias = null;
    List<Trabajadorbbdd> ListaTrabajadores = null;
    List<Nomina> ListaNomina = null;
    List<String> ListaCategoriasNombre = null;
    ArrayList<Categorias> ca= new ArrayList<Categorias>();
    ArrayList<Empresas> em= new ArrayList<Empresas>();
    public List<Categorias> getCategorias() {
        Categorias em=null;
        try {
            String consultaHQL = "FROM Categorias e";
            Query query = sesion.createQuery(consultaHQL);
            ListaCategorias = query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ListaCategorias;
    }
    
    public bbddDAO(ArrayList<Categorias> c,ArrayList<Empresas> e,ArrayList<Trabajadorbbdd> t,ArrayList<Nomina> n){
        sf = HibernateUtil.getSessionFactory();
        sesion = sf.openSession();
        tx = sesion.beginTransaction();
        List<Categorias> aux;
        String consultaHQL = "FROM Categorias e";
        Query query = sesion.createQuery(consultaHQL);
        ListaCategorias = query.list();
        //System.out.println(ListaEmpresas.toString());
        //ArrayList<Categorias> c2 = new ArrayList<Categorias>();
        boolean noExiste=true;
        for(int i=0;i<c.size();i++){
            for(int j=0;j<ListaCategorias.size();j++){
                if(c.get(i).getNombreCategoria().equals(ListaCategorias.get(j).getNombreCategoria())){
                    noExiste=false;
                    ListaCategorias.get(j).setSalarioBaseCategoria(c.get(i).getSalarioBaseCategoria());
                    ListaCategorias.get(j).setComplementoCategoria(c.get(i).getComplementoCategoria());
                    sesion.update(ListaCategorias.get(j));
                }
            }
            if(noExiste){
                noExiste=true;
                sesion.save(c.get(i));
                ListaCategorias.add(c.get(i));
            }
        }
        consultaHQL = "FROM Empresas e";
        query = sesion.createQuery(consultaHQL);
        ListaEmpresas = query.list();
        //System.out.println(ListaEmpresas.toString());
        //ArrayList<Categorias> c2 = new ArrayList<Categorias>();
        noExiste=true;
        for(int i=0;i<e.size();i++){
            for(int j=0;j<ListaEmpresas.size();j++){
                if(e.get(i).getCif().equals(ListaEmpresas.get(j).getCif())){
                    noExiste=false;
                    ListaEmpresas.get(j).setNombre(e.get(i).getNombre());
                    sesion.update(ListaEmpresas.get(j));
                }
            }
            if(noExiste){
                noExiste=true;
                sesion.save(e.get(i));
                ListaEmpresas.add(e.get(i));
            }
        }
        /*consultaHQL = "FROM Empresas e";
        query = sesion.createQuery(consultaHQL);
        ListaEmpresas = query.list();*/
        consultaHQL = "FROM Trabajadorbbdd e";
        query = sesion.createQuery(consultaHQL);
        ListaTrabajadores = query.list();
        //System.out.println(ListaEmpresas.toString());
        //ArrayList<Categorias> c2 = new ArrayList<Categorias>();
        int cap=ListaTrabajadores.size();
        noExiste=true;
        for(int i=0;i<t.size();i++){
            for(int j=0;j<ListaTrabajadores.size();j++){
                if(t.get(i).getNifnie().equals(ListaTrabajadores.get(j).getNifnie()) && t.get(i).getNombre().equals(ListaTrabajadores.get(j).getNombre()) && t.get(i).getFechaAlta().equals(ListaTrabajadores.get(j).getFechaAlta()) && t.get(i).getEmpresas().getCif().equals(ListaTrabajadores.get(j).getEmpresas()
                .getCif())  ){
                    noExiste=false;
                    if(j<cap){
                    ListaTrabajadores.get(j).setApellido1(t.get(i).getApellido1());
                    ListaTrabajadores.get(j).setApellido2(t.get(i).getApellido2());
                    //ListaTrabajadores.get(i).setCategorias(tbd3.getCategorias());
                    ListaTrabajadores.get(j).setCodigoCuenta(t.get(i).getCodigoCuenta());
                    ListaTrabajadores.get(j).setEmail(t.get(i).getEmail());
                    //tbd2.get(i).setEmpresas(tbd3.getEmpresas());
                    ListaTrabajadores.get(j).setIban(t.get(i).getIban());
                    ListaTrabajadores.get(j).setNominas(t.get(i).getNominas());
                    for(int k=0;k<ListaCategorias.size();k++){
                        if(t.get(i).getCategorias().getNombreCategoria().equals(ListaCategorias.get(k).getNombreCategoria())){
                            ListaTrabajadores.get(j).setCategorias(ListaCategorias.get(k));
                        }
                    }
                    boolean auxb=true;
                    for(int k=0;k<ListaEmpresas.size();k++){
                        if(t.get(i).getEmpresas().getCif().equals(ListaEmpresas.get(k).getCif())){
                            auxb=false;
                            ListaTrabajadores.get(j).setEmpresas(ListaEmpresas.get(k));
                        }
                    }
                    if(auxb){
                        sesion.save(t.get(i).getEmpresas());
                        ListaEmpresas.add(t.get(i).getEmpresas());
                    }
                    sesion.update(ListaTrabajadores.get(j));
                    }
                }
            }
            if(noExiste){
                noExiste=true;
                for(int k=0;k<ListaCategorias.size();k++){
                        if(t.get(i).getCategorias().getNombreCategoria().compareTo(ListaCategorias.get(k).getNombreCategoria())==0){
                            //System.out.println("c"+k);
                            t.get(i).setCategorias(ListaCategorias.get(k));
                        }
                    }
                boolean auxb=true;
                    for(int k=0;k<ListaEmpresas.size();k++){
                         //= t.get(i).getEmpresas().getCif().equals(ListaEmpresas.get(k).getCif());
                        if(t.get(i).getEmpresas().getCif().compareTo(ListaEmpresas.get(k).getCif())==0){
                            //System.out.println("e"+k+t.get(i).getEmpresas().getCif()+"/"+ListaEmpresas.get(k).getCif());
                            t.get(i).setEmpresas(ListaEmpresas.get(k));
                            auxb=false;
                        }
                    }
                    if(auxb){
                        sesion.save(t.get(i).getEmpresas());
                        ListaEmpresas.add(t.get(i).getEmpresas());
                    }
                if(t.get(i).getNifnie().length()!=0){
                    sesion.save(t.get(i));
                    ListaTrabajadores.add(t.get(i));
                }
            }
        }
        //System.out.println(n.size());
        consultaHQL = "FROM Trabajadorbbdd e";
        query = sesion.createQuery(consultaHQL);
        ListaTrabajadores = query.list();
        //sesion.getTransaction().commit();
        consultaHQL = "FROM Nomina e";
        query = sesion.createQuery(consultaHQL);
        ListaNomina = query.list();
        noExiste=true;
        for(int i=0;i<n.size();i++){
            for(int j=0;j<ListaNomina.size();j++){
                if(n.get(i).getMes()==ListaNomina.get(j).getMes() &&  
                n.get(i).getAnio()==ListaNomina.get(j).getAnio()
                        && Double.compare(n.get(i).getBrutoAnual(), ListaNomina.get(j).getBrutoAnual())==0 &&
                        Double.compare(n.get(i).getLiquidoNomina(), ListaNomina.get(j).getLiquidoNomina())==0 &&
                        n.get(i).getTrabajadorbbdd().getNifnie().equals(ListaNomina.get(j).getTrabajadorbbdd().getNifnie()) && 
                        n.get(i).getTrabajadorbbdd().getNombre().equals(ListaNomina.get(j).getTrabajadorbbdd().getNombre()) &&
                        n.get(i).getTrabajadorbbdd().getFechaAlta().equals(ListaNomina.get(j).getTrabajadorbbdd().getFechaAlta())){
                    noExiste=false;
                        boolean auxd=false;
                for(int k=0;k<ListaTrabajadores.size();k++){
                      
                    
                    if(n.get(i).getTrabajadorbbdd().getNifnie().equals(ListaTrabajadores.get(k).getNifnie() )){
                            
                            if(n.get(i).getTrabajadorbbdd().getNombre().equals(ListaTrabajadores.get(k).getNombre())){
                                
                                
                                if(n.get(i).getTrabajadorbbdd().getFechaAlta().equals(ListaTrabajadores.get(k).getFechaAlta())){
                                    auxd=true;
                                    
                                    ListaNomina.get(j).setTrabajadorbbdd(ListaTrabajadores.get(k));
                                }
                            }
                         
                }
                }

                if(auxd){
                    ListaNomina.get(j).setAccidentesTrabajoEmpresario(n.get(i).getAccidentesTrabajoEmpresario());
                    ListaNomina.get(j).setBaseEmpresario(n.get(i).getBaseEmpresario());
                    ListaNomina.get(j).setCosteTotalEmpresario(n.get(i).getCosteTotalEmpresario());
                    ListaNomina.get(j).setDesempleoEmpresario(n.get(i).getDesempleoEmpresario());
                    ListaNomina.get(j).setDesempleoTrabajador(n.get(i).getDesempleoTrabajador());
                    ListaNomina.get(j).setFogasaempresario(n.get(i).getFogasaempresario());
                    ListaNomina.get(j).setFormacionEmpresario(n.get(i).getFormacionEmpresario());
                    ListaNomina.get(j).setFormacionTrabajador(n.get(i).getFormacionTrabajador());
                    ListaNomina.get(j).setImporteAccidentesTrabajoEmpresario(n.get(i).getImporteAccidentesTrabajoEmpresario());
                    ListaNomina.get(j).setImporteComplementoMes(n.get(i).getImporteComplementoMes());
                    ListaNomina.get(j).setImporteDesempleoEmpresario(n.get(i).getImporteDesempleoEmpresario());
                    ListaNomina.get(j).setImporteDesempleoTrabajador(n.get(i).getImporteDesempleoTrabajador());
                    ListaNomina.get(j).setImporteFogasaempresario(n.get(i).getImporteFogasaempresario());
                    ListaNomina.get(j).setImporteFormacionEmpresario(n.get(i).getImporteFogasaempresario());
                    ListaNomina.get(j).setImporteFormacionTrabajador(n.get(i).getImporteFormacionTrabajador());
                    ListaNomina.get(j).setImporteTrienios(n.get(i).getImporteTrienios());
                    ListaNomina.get(j).setImporteIrpf(n.get(i).getImporteIrpf());
                    ListaNomina.get(j).setImporteSalarioMes(n.get(i).getImporteSalarioMes());
                    ListaNomina.get(j).setNumeroTrienios(n.get(i).getNumeroTrienios());
                    ListaNomina.get(j).setSeguridadSocialEmpresario(n.get(i).getSeguridadSocialEmpresario());
                    ListaNomina.get(j).setSeguridadSocialTrabajador(n.get(i).getSeguridadSocialTrabajador());
                    ListaNomina.get(j).setValorProrrateo(n.get(i).getValorProrrateo());
                    sesion.update(ListaNomina.get(j));
                    //sesion.getTransaction().commit();
                }
                } 
                    
                
            }
            
            if(noExiste){
                boolean auxc=false;
                //System.out.println("10");
                //boolean salir=true;
                boolean auxd=false;
                for(int k=0;k<ListaTrabajadores.size();k++){
                      
                    
                    if(n.get(i).getTrabajadorbbdd().getNifnie().equals(ListaTrabajadores.get(k).getNifnie() )){
                            
                            if(n.get(i).getTrabajadorbbdd().getNombre().equals(ListaTrabajadores.get(k).getNombre())){
                                
                                
                                if(n.get(i).getTrabajadorbbdd().getFechaAlta().equals(ListaTrabajadores.get(k).getFechaAlta())){
                                    auxd=true;
                                    
                                    n.get(i).setTrabajadorbbdd(ListaTrabajadores.get(k));
                                }
                            }
                        //} 
                }
                }

                if(auxd){    
                sesion.save(n.get(i));
                //if(salir){
                //System.out.println("1");
                noExiste=true;
                auxd=false;
                sesion.save(n.get(i));
                
                }
            }
        }
        
        
        if (!sesion.getTransaction().wasCommitted())
                    sesion.getTransaction().commit();;
                
        
        HibernateUtil.shutdown();

    }
}
