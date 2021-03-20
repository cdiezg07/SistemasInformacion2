
import java.util.*;
import modelo.Empresas;
import modelo.EmpresasDAO;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.NominaDAO;
import modelo.Trabajadorbbdd;
import modelo.TrabajadorbbddDAO;

/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */ /**
 *
 * @author carlos
 */

public class SistemasInformacion2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Scanner leer = new Scanner(System.in);

       
        System.out.println("Introduce un DNI: ");
        String dni = leer.nextLine();

        TrabajadorbbddDAO tdao = new TrabajadorbbddDAO();
        //Devuelve el trabajador solicitado
        Trabajadorbbdd tbd = tdao.buscarTrabajador(dni);

        if (tbd == null) {
            System.out.print("El dni no se encuentra en la base de datos.\n");
            HibernateUtil.shutdown();

        } else {

            System.out.println("Nombre y apellidos: " + tbd.getNombre() + " " + tbd.getApellido1() + " " + tbd.getApellido2());
            System.out.println("NIF: " + tbd.getNifnie());
            System.out.println("Categoria: " + tbd.getCategorias().getNombreCategoria());
            System.out.println("Empresa: " + tbd.getEmpresas().getNombre());

            int cont = 1;
            //obtengo todas las nominas 
            Iterator<Nomina> iter = tbd.getNominas().iterator();
            while(iter.hasNext()){
                Nomina nom = (Nomina) iter.next();
                System.out.println("Nomina" + cont + ":  " + nom.getMes() + "/" + nom.getAnio() + ",  " + nom.getBrutoNomina());
                cont++;
            }
            System.out.println("---------------------------------");
            //////////////////////////////
            // EJERCICIO 2
            //////////////////////////////
            //Cargo la lista de Empresas
            System.out.println("Nombres de las que hay que modificar:");
            EmpresasDAO edao = new EmpresasDAO();
            List<Empresas> ListaEmpresas = edao.getEmpresas(tbd.getEmpresas().getIdEmpresa());
            for (Empresas e : ListaEmpresas) {
                System.out.println(e.getNombre());
            }
            //Actualizo los datos de las empresas
            edao.editarEmpresas2021(ListaEmpresas);
            System.out.println("---------------------------------");
            //////////////////////////////
            // EJERCICIO 3
            //////////////////////////////
            NominaDAO ndao = new NominaDAO();

            //Creo una lista con todos los idTrabajador de trabajadores que no trabajen en la empresa tbd.getEmpresas().getIdEmpresa()
            List<Trabajadorbbdd> ListaIdTrabajadoresToDelete = tdao.getTrabajadoresToDelete(tbd.getEmpresas().getIdEmpresa());

            //elimino nominas asociadas a idTrabajador de la lista
            for (Trabajadorbbdd t : ListaIdTrabajadoresToDelete) {
                ndao.eliminarNominas(t.getIdTrabajador());
            }

            //elimino trabajadores de la lista
            tdao.eliminarTrabajador(tbd.getEmpresas().getIdEmpresa());

            HibernateUtil.shutdown();
        }

    }

}
