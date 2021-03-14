
import java.util.*;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.NominaDAO;
import modelo.Trabajadorbbdd;
import modelo.TrabajadorbbddDAO;
import org.hibernate.Hibernate;
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

        //Con esta consulta estaria trayendo todas los datos de todas las tabas de la base de datos
        System.out.println("Introduce un DNI: ");
        String dni = leer.nextLine();

        TrabajadorbbddDAO tdao = new TrabajadorbbddDAO();
        Trabajadorbbdd tbd = tdao.buscarTrabajador(dni);

        System.out.println("Nombre y apellidos: " + tbd.getNombre() + " " + tbd.getApellido1() + " " + tbd.getApellido2());
        System.out.println("NIF: " + tbd.getNifnie());
        System.out.println("Categoria: " + tbd.getCategorias().getIdCategoria());
        System.out.println("Empresa: " + tbd.getEmpresas().getNombre());

        NominaDAO ndao = new NominaDAO();
        List<Nomina> ListaNomina = ndao.getNominas(tbd.getIdTrabajador());
        int cont = 1;
        for (Nomina nom : ListaNomina) {

            System.out.println("Nomina" + cont + "  " + nom.getMes() + "/" + nom.getAnio() + ",  " + nom.getBrutoNomina());

            cont++;
        }
        System.out.println("---------------------------------");
        
        
        //////////////////////////////
        // EJERCICIO 3
        //////////////////////////////
        /*System.out.println("\n\n\n\nEliminando nominas...\nEleminando trabajadores...\nBase de datos actualizada!");
        System.out.println("Id Empresa empresa: " + tbd.getEmpresas().getIdEmpresa()); 
        System.out.println("Id Trabajador: " + tbd.getIdTrabajador());*/
        
        
        //Creo una lista con todos los idTrabajador de trabajadores que no trabajen en la empresa tbd.getEmpresas().getIdEmpresa()
        List<Integer> ListaIdTrabajadoresToDelete = tdao.getTrabajadoresToDelete(tbd.getEmpresas().getIdEmpresa());
        
        //elimino nominas asociadas a idTrabajador de la lista
        for (int i = 0; i < ListaIdTrabajadoresToDelete.size(); i++) {
            ndao.eliminarNominas(ListaIdTrabajadoresToDelete.get(i));
            tdao.eliminarTrabajador(ListaIdTrabajadoresToDelete.get(i));
        }
        //elimino trabajadores de la lista
        
        
        HibernateUtil.shutdown();
    }

}
