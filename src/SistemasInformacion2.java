
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.AccesoExcel;
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

//        Scanner leer = new Scanner(System.in);
//
//       
//        System.out.println("Introduce un DNI: ");
//        String dni = leer.nextLine();
//
//        TrabajadorbbddDAO tdao = new TrabajadorbbddDAO();
//        //Devuelve el trabajador solicitado
//        Trabajadorbbdd tbd = tdao.buscarTrabajador(dni);
//
//        if (tbd == null) {
//            System.out.print("El dni no se encuentra en la base de datos.\n");
//            HibernateUtil.shutdown();
//
//        } else {
//
//            System.out.println("Nombre y apellidos: " + tbd.getNombre() + " " + tbd.getApellido1() + " " + tbd.getApellido2());
//            System.out.println("NIF: " + tbd.getNifnie());
//            System.out.println("Categoria: " + tbd.getCategorias().getNombreCategoria());
//            System.out.println("Empresa: " + tbd.getEmpresas().getNombre());
//
//            int cont = 1;
//            //obtengo todas las nominas 
//            Iterator<Nomina> iter = tbd.getNominas().iterator();
//            while(iter.hasNext()){
//                Nomina nom = (Nomina) iter.next();
//                System.out.println("Nomina" + cont + ":  " + nom.getMes() + "/" + nom.getAnio() + ",  " + nom.getBrutoNomina());
//                cont++;
//            }
//            System.out.println("---------------------------------");
//            //////////////////////////////
//            // EJERCICIO 2
//            //////////////////////////////
//            //Cargo la lista de Empresas
//            System.out.println("Nombres de las que hay que modificar:");
//            EmpresasDAO edao = new EmpresasDAO();
//            List<Empresas> ListaEmpresas = edao.getEmpresas(tbd.getEmpresas().getIdEmpresa());
//            for (Empresas e : ListaEmpresas) {
//                System.out.println(e.getNombre());
//            }
//            //Actualizo los datos de las empresas
//            edao.editarEmpresas2021(ListaEmpresas);
//            System.out.println("---------------------------------");
//            //////////////////////////////
//            // EJERCICIO 3
//            //////////////////////////////
//            NominaDAO ndao = new NominaDAO();
//
//            //Creo una lista con todos los idTrabajador de trabajadores que no trabajen en la empresa tbd.getEmpresas().getIdEmpresa()
//            List<Trabajadorbbdd> ListaIdTrabajadoresToDelete = tdao.getTrabajadoresToDelete(tbd.getEmpresas().getIdEmpresa());
//
//            //elimino nominas asociadas a idTrabajador de la lista
//            for (Trabajadorbbdd t : ListaIdTrabajadoresToDelete) {
//                ndao.eliminarNominas(t.getIdTrabajador());
//            }
//
//            //elimino trabajadores de la lista
//            tdao.eliminarTrabajador(tbd.getEmpresas().getIdEmpresa());
//
//            HibernateUtil.shutdown();
//        }
        AccesoExcel ae = new AccesoExcel();
        try {
            // ae.acceso();
            ArrayList<Trabajadorbbdd> atb = ae.accesoHoja3();
            modEmail(atb);
            modDni(atb);
            String codigoCuenta = "";
            String iban = "";
            for(int j=0; j<atb.size(); j++){
                if(atb.get(j).getNombre()!=""){
                    codigoCuenta = ccc(atb.get(j).getCodigoCuenta());
                    iban = iban(atb.get(j).getIban(), codigoCuenta);
                    atb.get(j).setIban(iban);
                    atb.get(j).setCodigoCuenta(codigoCuenta);
                }
            }
            for (int i = 0; i < atb.size(); i++) {
                Trabajadorbbdd tbd = atb.get(i);
                System.out.println(tbd.getNombre() + ", " + tbd.getApellido1() + ", " + tbd.getApellido2() + ", " + tbd.getEmpresas().getNombre() + ", " + tbd.getEmail() + ", " + tbd.getNifnie()
                +", "+tbd.getCodigoCuenta()+", "+tbd.getIban());

            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private static void modDni(ArrayList<Trabajadorbbdd> atb) {
        char letras[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        for (int j = 0; j < atb.size(); j++) {
            if (atb.get(j).getNifnie() != "") {

                String cadena = atb.get(j).getNifnie();
                char cad[];
                cad = cadena.toCharArray();
                switch (cad[0]) {
                    case 'X':
                        cad[0] = '0';
                        break;
                    case 'Y':
                        cad[0] = '1';
                        break;
                    case 'Z':
                        cad[0] = '2';
                        break;
                }
                String s = "";
                for (int i = 0; i < cad.length - 1; i++) {
                    s += cad[i];
                }
                int index = Integer.parseInt(s) % 23;
                if (cad[cad.length - 1] == letras[index]) {
                } else {
                    char cad2[] = cadena.toCharArray();
                    cad2[cad2.length - 1] = letras[index];
                    String DNIfinal = String.valueOf(cad2);
                    System.out.println("DNI final: " + DNIfinal);
                    atb.get(j).setNifnie(DNIfinal);
                }

            }
        }

    }
    public static String iban(String pais, String ccc){
        int[] valorNumerico = new int[2];
        valorNumerico[0] = valorNumerico(pais.charAt(0));
        valorNumerico[1] = valorNumerico(pais.charAt(1));
        
        String cadena = ccc + valorNumerico[0] + valorNumerico[1] + "00";
        BigInteger cadenaAux = new BigInteger(cadena);
        
        BigInteger resto;
        resto = cadenaAux.remainder(new BigInteger("97"));
        
        int digito = 98 - resto.intValue();
        
        if(digito < 10)
            return pais + "0" + String.valueOf(digito) + ccc;
        else 
            return pais + String.valueOf(digito) + ccc;
    }
    
    public static String ccc(String ccc){

        int[] entidadOficina = new int[8];
        int[] dControl = new int[2];
        int[] id = new int[10];
        int suma = 0;
        
        //relleno los arrays para operar con ellos
        for(int i = 0; i < entidadOficina.length; i++){
            entidadOficina[i] = Character.getNumericValue(ccc.charAt(i));
        }
        for(int i = 0; i < dControl.length; i++){
            dControl[i] = Character.getNumericValue(ccc.charAt(i+entidadOficina.length));
        }
        for(int i = 0; i < id.length; i++){
            id[i] = Character.getNumericValue(ccc.charAt(i+entidadOficina.length+dControl.length));
        }

        //calculo el digito 1
        int[] dControlAux = new int [2];
        String digitoControl = "00";
        for(int i = 0; i<entidadOficina.length; i++){
            digitoControl += entidadOficina[i];
        }
        dControlAux[0] = calculadoraCCC(digitoControl);
        
        //calculo el digito 2
        digitoControl = "";
        for(int i = 0; i<id.length; i++){
            digitoControl += id[i];
        }
        dControlAux[1] = calculadoraCCC(digitoControl);
        
        //creo el ccc auxiliar
        String cccAux = "";
        for(int i = 0; i < entidadOficina.length; i++){
            cccAux += entidadOficina[i];
        }
        for(int i = 0; i < dControlAux.length; i++){
            cccAux += dControlAux[i];
        }
        for(int i = 0; i < id.length; i++){
           cccAux += id[i];
        }
        
        //Devuelvo el ccc correcto
        if(dControl[0] == dControlAux[0] && dControl[1] == dControlAux[1])
            return ccc;
        else
            return cccAux;
    }
    
    public static int calculadoraCCC(String digitoControl){
        int[] factores = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
        int suma = 0;
        for(int i = 0; i < factores.length; i++){
            suma += Character.getNumericValue(digitoControl.charAt(i))*factores[i];  
        }
        
        int resto = suma%11;
        int digito = 11-resto;
        
        switch (digito) {
            case 11:
                return 0;
            case 10:
                return 1;
            default:
                return digito;
        }
    }
    
    public static int valorNumerico(char i){
        switch (i) {
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
            case 'G':
                return 16;
            case 'H':
                return 17;
            case 'I':
                return 18;
            case 'J':
                return 19;
            case 'K':
                return 20;
            case 'L':
                return 21;
            case 'M':
                return 22;
            case 'N':
                return 23;
            case 'O':
                return 24;
            case 'P':
                return 25;
            case 'Q':
                return 26;
            case 'R':
                return 27;
            case 'S':
                return 28;
            case 'T':
                return 29;
            case 'U':
                return 30;
            case 'V':
                return 31;
            case 'W':
                return 32;
            case 'X':
                return 33;
            case 'Y':
                return 34;
            case 'Z':
                return 35;
            default:
                return 0;
        }
    }

    private static void modEmail(ArrayList<Trabajadorbbdd> atb) {

        ArrayList<String> emailsGenerados = new ArrayList<String>();

        for (int i = 0; i < atb.size(); i++) {
            String email = "";
            boolean incrmentado = false;
            Trabajadorbbdd tbd = atb.get(i);

            if (tbd.getNombre() != "") {
                email = tbd.getNombre().substring(0, 1) + tbd.getApellido1().substring(0, 1);
                if (tbd.getApellido2() != "") {
                    email += tbd.getApellido2().substring(0, 1);
                }

                int cont = 0;
                while (cont < emailsGenerados.size()) {
                    if (emailsGenerados.get(cont).substring(0, 3).equals(email)) {
                        int num = Integer.parseInt(emailsGenerados.get(cont).substring(3, 5));
                        num++;
                        incrmentado = true;
                        if (num < 10) {
                            email += "0" + String.valueOf(num);
                        } else {
                            email += String.valueOf(num);
                        }

                    }

                    cont++;
                }

                if (!incrmentado) {
                    email += "00";
                }

                email += "@";
                email += tbd.getEmpresas().getNombre();
                email += ".com";
                tbd.setEmail(email);
                emailsGenerados.add(email);
            }

        }
    }

}
