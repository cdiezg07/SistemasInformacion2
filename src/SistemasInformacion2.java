
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import modelo.AccesoExcel;
import modelo.Empresas;
import modelo.EmpresasDAO;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.NominaDAO;
import modelo.Trabajadorbbdd;
import modelo.TrabajadorbbddDAO;
import modelo.XMLGenerator;

/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */ /**
 *
 * @author carlos
 */

public class SistemasInformacion2 {

    static boolean cuentaCorrecta = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException {
        // TODO code application logic here

        Scanner leer = new Scanner(System.in);

       
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
        
        System.out.println("Introduce una fecha para la generacion de nominas: ");
        String fecha = leer.nextLine();
        Date date=new SimpleDateFormat("MM/yyyy").parse(fecha);
       // System.out.println(date.toString());
        AccesoExcel ae = new AccesoExcel();
        ae.accesoHoja1();
        ae.accesoHoja2();
        System.out.println(ae.getCategorias());
        System.out.println(ae.getTrienios());
        System.out.println(ae.getBrutoAnual());
        System.out.println(ae.getCuotas());
        try {
            ArrayList<Trabajadorbbdd> atb = ae.accesoHoja3();
            modEmail(atb);
            modDni(atb);
            modCCC(atb);
            ae.cargarNuevosDatos(atb);
            
            //Generacion Nominas
            ArrayList<Trabajadorbbdd> nominasTrabajadores = nominasArealizar(atb, date);            
            for(int i=0; i<nominasTrabajadores.size(); i++){            
               System.out.println(nominasTrabajadores.get(i).getNombre());
            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    private static ArrayList<Trabajadorbbdd> nominasArealizar(ArrayList<Trabajadorbbdd> atb, Date fecha){
        ArrayList<Trabajadorbbdd> nominasTrabajadores = new ArrayList<Trabajadorbbdd>();
        for(int i=0; i<atb.size(); i++){
            if(atb.get(i).getFechaAlta().compareTo(fecha) < 0){
                nominasTrabajadores.add(atb.get(i));
            }
        }
        return nominasTrabajadores;
    }
    
    private static void modCCC(ArrayList<Trabajadorbbdd> atb) throws Exception {
        XMLGenerator xml = new XMLGenerator("Cuentas", "ErroresCCC");
        //Modficar codigo cuenta
        String codigoCuenta = "";
        String iban = "";
        String cccErroneo = "";
        for (int j = 0; j < atb.size(); j++) {
            if (!atb.get(j).getNombre().equals("")) {
                cccErroneo = atb.get(j).getCodigoCuenta();
                codigoCuenta = ccc(atb.get(j).getCodigoCuenta());
                iban = iban(atb.get(j).getIban(), codigoCuenta);
                atb.get(j).setIban(iban);
                atb.get(j).setCodigoCuenta(codigoCuenta);

                if (!cuentaCorrecta) {
                    ArrayList<String> key = new ArrayList<String>();
                    ArrayList<String> valor = new ArrayList<String>();
                    key.add("Nombre");
                    key.add("Apellidos");
                    key.add("Empresa");
                    key.add("CCCErroneo");
                    key.add("IBANCorrecto");

                    valor.add(atb.get(j).getNombre());
                    valor.add(atb.get(j).getApellido1() + " " + atb.get(j).getApellido2());
                    valor.add(atb.get(j).getEmpresas().getNombre());
                    valor.add(cccErroneo);
                    valor.add(atb.get(j).getIban());

                    xml.addItem("Cuenta", key, valor, atb.get(j).getIdTrabajador() + "");
                }

            }
        }
        xml.generate();
    }

    private static void modDni(ArrayList<Trabajadorbbdd> atb) throws Exception {
        char letras[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        XMLGenerator xmlDNI = new XMLGenerator("Trabajadores", "Errores");
        ArrayList<String> DNI = new ArrayList<String>();
        for (int j = 0; j < atb.size(); j++) {
            if (!atb.get(j).getNifnie().equals("")) {
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
                String DNIfinal = cadena;
                if (cad[cad.length - 1] == letras[index]) {

                } else {
                    char cad2[] = cadena.toCharArray();
                    cad2[cad2.length - 1] = letras[index];
                    DNIfinal = String.valueOf(cad2);
                    //System.out.println("DNI final: " + DNIfinal);
                    atb.get(j).setNifnie(DNIfinal);
                }
                if (DNI.isEmpty()) {
                    DNI.add(DNIfinal);
                } else {
                    boolean repetido = true;
                    for (int i = 0; i < DNI.size(); i++) {
                        if (DNI.get(i).equals(DNIfinal)) {
                            ArrayList<String> key = new ArrayList<String>();
                            ArrayList<String> valor = new ArrayList<String>();
                            key.add("Nombre");
                            key.add("PrimerApellido");
                            key.add("SegundoApellido");
                            key.add("Empresa");
                            key.add("Categoria");

                            valor.add(atb.get(j).getNombre());
                            valor.add(atb.get(j).getApellido1());
                            valor.add(atb.get(j).getApellido2());
                            valor.add(atb.get(j).getEmpresas().getNombre());
                            valor.add(atb.get(j).getCategorias().getNombreCategoria());

                            xmlDNI.addItem("Trabajador", key, valor, atb.get(j).getIdTrabajador() + "");
                            repetido = false;
                        }
                    }
                    if (repetido) {
                        DNI.add(DNIfinal);
                    }
                }

            } else {
                ArrayList<String> key = new ArrayList<String>();
                ArrayList<String> valor = new ArrayList<String>();
                key.add("Nombre");
                key.add("PrimerApellido");
                key.add("SegundoApellido");
                key.add("Empresa");
                key.add("Categoria");

                valor.add(atb.get(j).getNombre());
                valor.add(atb.get(j).getApellido1());
                valor.add(atb.get(j).getApellido2());
                valor.add(atb.get(j).getEmpresas().getNombre());
                valor.add(atb.get(j).getCategorias().getNombreCategoria());

                xmlDNI.addItem("Cuenta", key, valor, atb.get(j).getIdTrabajador() + "");

            }
        }
        xmlDNI.generate();
        xmlDNI = null;
    }

    private static String iban(String pais, String ccc) {
        int[] valorNumerico = new int[2];
        valorNumerico[0] = valorNumerico(pais.charAt(0));
        valorNumerico[1] = valorNumerico(pais.charAt(1));

        String cadena = ccc + valorNumerico[0] + valorNumerico[1] + "00";
        BigInteger cadenaAux = new BigInteger(cadena);

        BigInteger resto;
        resto = cadenaAux.remainder(new BigInteger("97"));

        int digito = 98 - resto.intValue();

        if (digito < 10) {
            return pais + "0" + String.valueOf(digito) + ccc;
        } else {
            return pais + String.valueOf(digito) + ccc;
        }
    }

    private static String ccc(String ccc) {

        int[] entidadOficina = new int[8];
        int[] dControl = new int[2];
        int[] id = new int[10];
        int suma = 0;

        //relleno los arrays para operar con ellos
        for (int i = 0; i < entidadOficina.length; i++) {
            entidadOficina[i] = Character.getNumericValue(ccc.charAt(i));
        }
        for (int i = 0; i < dControl.length; i++) {
            dControl[i] = Character.getNumericValue(ccc.charAt(i + entidadOficina.length));
        }
        for (int i = 0; i < id.length; i++) {
            id[i] = Character.getNumericValue(ccc.charAt(i + entidadOficina.length + dControl.length));
        }

        //calculo el digito 1
        int[] dControlAux = new int[2];
        String digitoControl = "00";
        for (int i = 0; i < entidadOficina.length; i++) {
            digitoControl += entidadOficina[i];
        }
        dControlAux[0] = calculadoraCCC(digitoControl);

        //calculo el digito 2
        digitoControl = "";
        for (int i = 0; i < id.length; i++) {
            digitoControl += id[i];
        }
        dControlAux[1] = calculadoraCCC(digitoControl);

        //creo el ccc auxiliar
        String cccAux = "";
        for (int i = 0; i < entidadOficina.length; i++) {
            cccAux += entidadOficina[i];
        }
        for (int i = 0; i < dControlAux.length; i++) {
            cccAux += dControlAux[i];
        }
        for (int i = 0; i < id.length; i++) {
            cccAux += id[i];
        }

        //Devuelvo el ccc correcto
        if (dControl[0] == dControlAux[0] && dControl[1] == dControlAux[1]) {
            cuentaCorrecta = true;
            return ccc;
        } else {
            cuentaCorrecta = false;
            return cccAux;
        }
    }

    private static int calculadoraCCC(String digitoControl) {
        int[] factores = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
        int suma = 0;
        for (int i = 0; i < factores.length; i++) {
            suma += Character.getNumericValue(digitoControl.charAt(i)) * factores[i];
        }

        int resto = suma % 11;
        int digito = 11 - resto;

        switch (digito) {
            case 11:
                return 0;
            case 10:
                return 1;
            default:
                return digito;
        }
    }

    private static int valorNumerico(char i) {
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

        ArrayList<String> emailsGenerados3Letras = new ArrayList<String>();
        ArrayList<String> emailsGenerados2Letras = new ArrayList<String>();
        int numMasAlto = 0;
        int num = 0;
        Trabajadorbbdd tbd = null;

        //Cogemos todos los emails ya hechos
        emailsGenerados(atb, emailsGenerados2Letras, emailsGenerados3Letras);

        for (int i = 0; i < atb.size(); i++) {
            String email = "";
            boolean incrmentado = false;
            numMasAlto = 0;
            tbd = atb.get(i);

            if (tbd.getEmail().equals("")) {
                if (!tbd.getNombre().equals("")) {
                    email = tbd.getNombre().substring(0, 1) + tbd.getApellido1().substring(0, 1);
                    if (!tbd.getApellido2().equals("")) {
                        email += tbd.getApellido2().substring(0, 1);
                    }

                    int cont = 0;
                    //Comprobamos si la persona tiene 1 apellido o 2
                    if (email.length() == 2) {
                        num = 0;
                        while (cont < emailsGenerados2Letras.size()) {
                            String prueba = emailsGenerados2Letras.get(cont);
                            if (prueba.substring(0, 2).equals(email) && prueba.substring(prueba.indexOf("@") + 1, prueba.indexOf(".")).equals(tbd.getEmpresas().getNombre())) {
                                num = Integer.parseInt(prueba.substring(2, 4));

                                if (num > numMasAlto) {
                                    numMasAlto = num;
                                }

                                numMasAlto++;
                                incrmentado = true;
                                if (numMasAlto < 10) {
                                    email += "0" + String.valueOf(numMasAlto);
                                } else {
                                    email += String.valueOf(numMasAlto);
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
                        emailsGenerados2Letras.add(email);

                    } else {
                        num = 0;
                        while (cont < emailsGenerados3Letras.size()) {
                            String prueba = emailsGenerados3Letras.get(cont);
                            if (prueba.substring(0, 3).equals(email) && prueba.substring(prueba.indexOf("@") + 1, prueba.indexOf(".")).equals(tbd.getEmpresas().getNombre())) {
                                num = Integer.parseInt(prueba.substring(3, 5));

                                if (num > numMasAlto) {
                                    numMasAlto = num;
                                }
                                numMasAlto++;
                                incrmentado = true;
                                if (numMasAlto < 10) {
                                    email += "0" + String.valueOf(numMasAlto);
                                } else {
                                    email += String.valueOf(numMasAlto);
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
                        emailsGenerados3Letras.add(email);

                    }

                }
            }
        }
    }

    private static void emailsGenerados(ArrayList<Trabajadorbbdd> atb, ArrayList<String> emailsGenerados2Letras, ArrayList<String> emailsGenerados3Letras) {
        for (int i = 0; i < atb.size(); i++) {
            if (!atb.get(i).getEmail().equals("")) {
                if (isNumeric(String.valueOf(atb.get(i).getEmail().charAt(2)))) {
                    emailsGenerados2Letras.add(atb.get(i).getEmail());
                } else {
                    emailsGenerados3Letras.add(atb.get(i).getEmail());
                }
            }
        }
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
