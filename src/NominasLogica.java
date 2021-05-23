import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map.Entry;
import java.util.Scanner;
/**
 *
 * @author carlos, sergio, mario
 */
public class NominasLogica {

        HashMap<String, String> trienios; 
        HashMap<String, String> Cuotas; 
        HashMap<String, String> irpf;
        
        float cuotaIRPF;
    public NominasLogica(String[] salarioComple, String prorrateo, HashMap<String, String> trienios, HashMap<String, String> irpf, HashMap<String, String> Cuotas, LocalDate fechaAltaTrabajador, int mesNomina, int anyoNomina){
        /*
         * Manejo de fechas
         */
        int mes = normalizarMes(mesNomina);
        int anyo = normalizarAnyo(mes, anyoNomina);
        System.out.println();

        LocalDate fechaAlta = LocalDate.of(fechaAltaTrabajador.getYear(), fechaAltaTrabajador.getMonth(), 1);
        LocalDate fechaNomina = LocalDate.of(anyo, mes, 1);
        LocalDate fechaAnual;
        if (mes != 1)
            fechaAnual = LocalDate.of(anyo + 1, 1, 1);
        else
            fechaAnual = LocalDate.of(anyo, 1, 1);
        Period periodNomina = Period.between(fechaAlta, fechaNomina); // periodo usado para calcular la nomina
        Period periodAnual = Period.between(fechaAlta, fechaAnual); // periodo usado para calcular el bruto anual

        boolean isDateCorrect = realizacionNomina(periodNomina);
        if (isDateCorrect) {
            System.out.println("Fecha correcta. Se realizara la nomina");
            System.out.println("Anyos:" + periodNomina.getYears() + " Meses:" + periodNomina.getMonths() + " Dias:"
                    + periodNomina.getDays());
            System.out.println();

            /*
             * Salario del trabajador
             */
            float salarioBase = Float.parseFloat(salarioComple[0]);
            float complementos = Float.parseFloat(salarioComple[1]);

            /*
             * Calcular trienios del trabajador
             */
            int numTrieniosNomina = periodNomina.getYears() / 3; // trienios usados en la nomina
            int numTrieniosAnual = periodAnual.getYears() / 3;// trienios usados para calcular el bruto anual

            /*
             * Bruto anual teniendo en cuenta opcion de prorrateo
             */
            boolean isNominaProrrateada = isNominaProrrateada(prorrateo);
            float brutoAnual = calcularBrutoAnual(salarioBase, complementos, numTrieniosAnual, periodAnual, isNominaProrrateada);
            System.out.println("Bruto anual: " + brutoAnual);

            /*
             * Valores de la nomina, como si tuviera prorrateo
             */
            float salarioBaseMensual = salarioBase / 14f;
            float complementosMensual = complementos / 14f;
            float importePorTrieniosMensual = importePorTrienios(numTrieniosNomina);
            float prorrateoExtra = calcularProrrateoExtra(salarioBaseMensual, complementosMensual,
                    importePorTrieniosMensual);
            float brutoMensualProrrateado = salarioBaseMensual + complementosMensual + importePorTrieniosMensual
                    + prorrateoExtra;
            float brutoMensual = salarioBaseMensual + complementosMensual + importePorTrieniosMensual;
            float nomina = 0;
            float nominaExtra = 0;
            float empresario = 0;
            this.irpf = irpf;
            cuotaIRPF = getIrpf(brutoAnual);

            if (isNominaProrrateada) {
                /*
                 * Parte trabajador
                 */
                brutoMensualProrrateado = salarioBaseMensual + complementosMensual + importePorTrieniosMensual
                        + prorrateoExtra;
                nomina = calcularNomina(salarioBaseMensual, complementosMensual, importePorTrieniosMensual,
                        brutoMensualProrrateado, brutoMensualProrrateado, prorrateoExtra, numTrieniosNomina);
                System.out.println("\t\t\t\t\t\t\tLiquido a percibir\t\t\t" + nomina);

                /*
                 * Parte especifica empresario
                 */
                empresario = parteEspecificaEmpresario(brutoMensualProrrateado);
                System.out.println(
                        "\nCOSTE TOTAL TRABAJADOR\t\t\t\t\t\t\t\t\t\t" + (empresario + brutoMensualProrrateado));
            } else {
                /*
                 * Parte trabajador
                 */
                brutoMensual = salarioBaseMensual + complementosMensual + importePorTrieniosMensual;
                nomina = calcularNomina(salarioBaseMensual, complementosMensual, importePorTrieniosMensual,
                        brutoMensual, brutoMensualProrrateado, 0, numTrieniosNomina);
                System.out.println("\t\t\t\t\t\t\tLiquido a percibir\t\t\t" + nomina);

                /*
                 * Parte especifica empresario
                 */
                empresario = parteEspecificaEmpresario(brutoMensualProrrateado);
                System.out.println("\nCOSTE TOTAL TRABAJADOR\t\t\t\t\t\t\t\t\t\t" + (empresario + brutoMensual));

                if (fechaNomina.getMonthValue() == 7 || fechaNomina.getMonthValue() == 1) {
                    /*
                     * Parte trabajador
                     */
                    nominaExtra = calcularNominaExtra(salarioBaseMensual, complementosMensual,
                            importePorTrieniosMensual, brutoMensual, 0, numTrieniosNomina, periodAnual, fechaNomina);
                    System.out.println("\t\t\t\t\t\t\tLiquido a percibir\t\t\t" + nominaExtra);
                    /*
                     * Parte especifica empresario
                     */
                    empresario = parteEspecificaEmpresario(0);
                    System.out.println("\nCOSTE TOTAL TRABAJADOR\t\t\t\t\t\t\t\t\t\t" + (empresario + brutoMensual));
                }
            }
        } else {
            System.out.println("Las fechas son incorrectas");
        }
    }
    
     /**
     * Normaliza el mes de la nomina.
     * 
     * @param mes mes de la nomina
     * @return Si queremos la nomina de Diciembre, devuelve enero, para contar el
     *         mes de diciembre completo
     */
    private int normalizarMes(int mes) {
        if (mes == 12)
            return 1;
        else
            return mes + 1;
    }

    /**
     * Normaliza el anyo de la nomina.
     * 
     * @param mes  mes de la nomina
     * @param anyo anyo de la nomina
     * @return Si queremos la nomina de Diciembre 2021, devuelve 2022
     */
    private int normalizarAnyo(int mes, int anyo) {
        if (mes == 1)
            return anyo + 1;
        else
            return anyo;
    }

    /**
     * Realizacion nomina
     * 
     * @param period atributo con la diferencia entre fechas
     * @return true(se puede realizar) si la fecha de alta es anterior a la nomina,
     *         false si es la misma fecha o posterior
     */
    private boolean realizacionNomina(Period period) {
        return !(period.isNegative() || period.isZero());
    }

    /**
     * Importe bruto por trienio
     * 
     * @param trienios numero de trienios del trabajador
     * @return el importe que recibe por los trienios trabajados
     */
    private int importePorTrienios(int trienios) {
        switch (trienios) {
            case 0:
                return 0;
            case 1:
                return 15;
            case 2:
                return 25;
            case 3:
                return 45;
            case 4:
                return 60;
            case 5:
                return 70;
            case 6:
                return 83;
            case 7:
                return 90;
            case 8:
                return 100;
            case 9:
                return 112;
            case 10:
                return 125;
            case 11:
                return 140;
            case 12:
                return 160;
            case 13:
                return 170;
            case 14:
                return 182;
            case 15:
                return 190;
            case 16:
                return 202;
            case 17:
                return 215;

            default:
                return 230;
        }
    }

    /**
     * Calcula el bruto anual del trabajador
     * 
     * @param salarioBase  del trabajador
     * @param complementos segun categoria
     * @param numTrienios  trabajados
     * @param period       periodo trabajado
     * @return el bruto anual del trabajador
     */
    private float calcularBrutoAnual(float salarioBase, float complementos, int numTrienios, Period period, boolean isNominaProrrateada) {
        int trienioAnterior = 0;
        if (numTrienios - 1 < 0)
            trienioAnterior = 0;
        else {
            trienioAnterior = importePorTrienios(numTrienios - 1);
        }

        if (numTrienios > 0) { // Con antiguedad
            if (period.getYears() % 3 == 0 || ((period.getYears()-1)%3 == 0 ) && period.getMonths() == 0) { // Con cambio de trienios
                int auxMonths = period.getMonths();
                if(((period.getYears()-1)%3 == 0 ) && period.getMonths() == 0){// caso especial para cambio de trienios el 1 de enero
                    auxMonths = 12;
                }
                if(isNominaProrrateada){ // con la nomina prorrateada
                    if (auxMonths > 7) { // cambio anterior extra de junio ---------------------------------------------------------------- si algun bruto no funciona poner 7
                        return salarioBase + complementos + (importePorTrienios(numTrienios) * (auxMonths - 1))
                                + (trienioAnterior * (13 - auxMonths)) + (importePorTrienios(numTrienios)/6f*7) + (importePorTrienios(numTrienios)/6f*5);
                    } else { // cambio posterior extra de junio
                        return salarioBase + complementos + (importePorTrienios(numTrienios) * (auxMonths - 1))
                                + (trienioAnterior * (13 - auxMonths)) + ((importePorTrienios(numTrienios)/6f)*7) + ((trienioAnterior/6f)*5);
                    }
                } else { // con la nomina sin prorratear
                    if (auxMonths > 7) { // cambio anterior extra de junio ---------------------------------------------------------------- si algun bruto no funciona poner 7
                        return salarioBase + complementos + (importePorTrienios(numTrienios) * (auxMonths - 1))
                                + (trienioAnterior * (13 - auxMonths)) + importePorTrienios(numTrienios)
                                + importePorTrienios(numTrienios);
                    } else { // cambio posterior extra de junio
                        return salarioBase + complementos + (importePorTrienios(numTrienios) * (auxMonths - 1))
                                + (trienioAnterior * (13 - auxMonths)) + importePorTrienios(numTrienios)
                                + trienioAnterior;
                    }
                }
            } else { // Sin cambio de trienios
                return salarioBase + complementos + (importePorTrienios(numTrienios) * 14);
            }
        } else { // Sin antiguedad
            if (period.getYears() > 0) { // Anyo completo
                float extraPosterior = 0;
                
                if(isNominaProrrateada && (period.getYears()+1)%3 == 0 && period.getMonths()+5 > 12){
                    extraPosterior = importePorTrienios(numTrienios+1)/6f;
                }
                return salarioBase + complementos + extraPosterior;
            } else { // Anyo no completo, empezo a trabajar el mismo anyo
                if(isNominaProrrateada){ // Con la nomina prorrateada
                    return ((salarioBase + complementos) / 12f) * period.getMonths();
                } else { // Sin prorrateo                    
                    if(period.getMonths() > 6) { //anterior extra de junio
                        //nominas completas + la extra que no se llega a cobrar completa
                        return ((salarioBase+complementos)/14f)*(period.getMonths()+1)+((salarioBase+complementos)/14f)*(period.getMonths()-7)/6f;
                    } else { //posterior extra de junio
                        //nominas completas + la extra que no se llega a combrar completa
                        return ((salarioBase+complementos)/14f)*(period.getMonths())+((salarioBase+complementos)/14f)*(period.getMonths()-1)/6f;
                    }
                }
            }
        }
    }

    /**
     * Obtiene si la nomina se prorratea o no
     * 
     * @param prorrateo cadena que indica si o no
     * @return devuelve true si el prorrateo es si, false si es no
     */
    private boolean isNominaProrrateada(String prorrateo) {
        return prorrateo.equals("SI");
    }

    /**
     * Calcula la parte mensual correspondiente a la paga extra
     * 
     * @param salarioBaseMensual        salario base del trabajador al mes
     * @param complementosMensual       complemento mensual
     * @param importePorTrieniosMensual importe recibido por los trienios en la
     *                                  empresa
     * @return parte de la nomina extra prorrateada
     */
    private float calcularProrrateoExtra(float salarioBaseMensual, float complementosMensual,
            float importePorTrieniosMensual) {
        return (salarioBaseMensual / 6f) + (complementosMensual / 6f) + (importePorTrieniosMensual / 6f);
    }

    /**
     * Calculo de la nomina
     * 
     * @param salarioBaseMensual        salario base del trabajador al mes
     * @param complementosMensual       complemento mensual
     * @param importePorTrieniosMensual importe recibido por los trienios en la
     *                                  empresa
     * @param brutoMensual              cobrado al mes(puede estar prorrateado o no)
     * @param brutoSobrePagos           es el brutoMensual prorrateado, que se usa
     *                                  para calcular S.Social, desempleo y
     *                                  formacion
     * @param prorrateoExtra            parte de la nomina extra prorrateada
     * @param numTrienios               numero de trienios del trabajador
     * @return cuantia de la nomina
     */
    private float calcularNomina(float salarioBaseMensual, float complementosMensual,
            float importePorTrieniosMensual, float brutoMensual, float brutoSobrePagos, float prorrateoExtra,
            int numTrienios) {
        float sSocial = brutoSobrePagos * (4.7f / 100);
        float desempleo = brutoSobrePagos * (1.6f / 100);
        float formacion = brutoSobrePagos * (0.1f / 100);
        float IRPF = brutoMensual * (cuotaIRPF / 100);

        System.out.println("\nConceptos\t\t\tCantidad\t\tImp. Unitario\t\tDevengo\t\tDeduccion");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println(
                "Salario Base\t\t\t" + "30 dias\t\t\t" + salarioBaseMensual / 30 + "\t\t" + salarioBaseMensual);
        System.out.println("Prorrateo\t\t\t" + "30 dias\t\t\t" + prorrateoExtra / 30 + "\t\t\t" + prorrateoExtra);
        System.out.println(
                "Complemento\t\t\t" + "30 dias\t\t\t" + complementosMensual / 30 + "\t\t" + complementosMensual);
        System.out.println("Antigüedad\t\t\t" + numTrienios + " trienios\t\t" + importePorTrieniosMensual / numTrienios
                + "\t\t\t" + importePorTrieniosMensual);

        System.out.println("Contingencias generales\t\t" + "04.70% de " + brutoSobrePagos + "\t\t\t\t\t\t" + sSocial);
        System.out.println("Desempleo\t\t\t" + "01.60% de " + brutoSobrePagos + "\t\t\t\t\t\t" + desempleo);
        System.out.println("Cuota formación\t\t\t" + "00.10% de " + brutoSobrePagos + "\t\t\t\t\t\t" + formacion);
        System.out.println("IRPF\t\t\t\t" + cuotaIRPF + "% de " + brutoMensual + "\t\t\t\t\t\t" + IRPF);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println("Total deducciones\t\t\t\t\t\t\t\t\t\t" + (sSocial + desempleo + formacion + IRPF));
        System.out.println("Total devengos\t\t\t\t\t\t\t\t\t" + brutoMensual);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        return brutoMensual - sSocial - desempleo - formacion - IRPF;
    }

    /**
     * 
     * @param salarioBaseMensual        salario base del trabajador al mes
     * @param complementosMensual       complemento mensual
     * @param importePorTrieniosMensual importe recibido por los trienios en la
     *                                  empresa
     * @param brutoMensual              cobrado al mes(puede estar prorrateado o no)
     * @param brutoSobrePagos           es el brutoMensual prorrateado, que se usa
     *                                  para calcular S.Social, desempleo y
     *                                  formacion
     * @param prorrateoExtra            parte de la nomina extra prorrateada
     * @param numTrienios               numero de trienios del trabajador
     * @param period                    periodo entre fechas de alta y nomina
     * @param fechaNomina               fecha de la nomina
     * @return liquido a percibir en la nomina extra
     */
    private float calcularNominaExtra(float salarioBaseMensual, float complementosMensual,
            float importePorTrieniosMensual, float brutoMensual, float prorrateoExtra, int numTrienios, Period period,
            LocalDate fechaNomina) {

        float auxBrutoMensual = 0; //auxiliar para trabajar con el brutoMensual
        if (period.getYears() == 0) { //menos de un anyo trabajado
            if (fechaNomina.getMonthValue() == 7) { // extra de junio --------------------------------------------------------------------- dudoso
                auxBrutoMensual = brutoMensual * (period.getMonths() - 6) / 6f;
                salarioBaseMensual = salarioBaseMensual * (period.getMonths() - 6) / 6f;
                complementosMensual = complementosMensual * (period.getMonths() - 6) / 6f;
            } else { // extra de diciembre
                if (period.getMonths() > 6) { // todo el semestre trabajado --------------------------------------------------------------------- dudoso
                    auxBrutoMensual = brutoMensual;
                } else { //menos del semestre trabajado
                    auxBrutoMensual = brutoMensual * (period.getMonths() - 1) / 6f;
                    salarioBaseMensual = salarioBaseMensual * (period.getMonths() - 1) / 6f;
                    complementosMensual = complementosMensual * (period.getMonths() - 1) / 6f;
                }
            }
        } else { //mas de un anyo
            auxBrutoMensual = brutoMensual;
        }

        float IRPF = auxBrutoMensual * (cuotaIRPF / 100);

        System.out.println(
                "\n\n//////////////////////////\n\tPaga Extra\n//////////////////////////\n\nConceptos\t\t\tCantidad\t\tImp. Unitario\t\tDevengo\t\tDeduccion");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println(
                "Salario Base\t\t\t" + "30 dias\t\t\t" + salarioBaseMensual / 30 + "\t\t" + salarioBaseMensual);
        System.out.println("Prorrateo\t\t\t" + "30 dias\t\t\t" + prorrateoExtra / 30 + "\t\t\t" + prorrateoExtra);
        System.out.println(
                "Complemento\t\t\t" + "30 dias\t\t\t" + complementosMensual / 30 + "\t\t" + complementosMensual);
        System.out.println("Antigüedad\t\t\t" + numTrienios + " trienios\t\t" + importePorTrieniosMensual / numTrienios
                + "\t\t\t" + importePorTrieniosMensual);

        System.out.println("Contingencias generales\t\t" + "04.70% de 00.00\t\t\t\t\t\t\t00.00");
        System.out.println("Desempleo\t\t\t" + "01.60% de 00.0\t\t\t\t\t\t\t00.00");
        System.out.println("Cuota formación\t\t\t" + "0010% de 00.00\t\t\t\t\t\t\t00.00");
        System.out.println("IRPF\t\t\t\t" + cuotaIRPF + "% de " + auxBrutoMensual + "\t\t\t\t\t\t" + IRPF);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println("Total deducciones\t\t\t\t\t\t\t\t\t\t" + (IRPF));
        System.out.println("Total devengos\t\t\t\t\t\t\t\t\t" + auxBrutoMensual);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");

        return auxBrutoMensual - IRPF;
    }

    /**
     * Calculo de la parte especifica del empresario
     * 
     * @param brutoSobrePagos bruto mensual sobre el que se calculan los gastos
     * @return devuelve la parte especifica del empresario
     */
    private float parteEspecificaEmpresario(float brutoSobrePagos) {
        float sSocial = brutoSobrePagos * (23.6f / 100);
        float desempleo = brutoSobrePagos * (6.7f / 100);
        float formacion = brutoSobrePagos * (0.6f / 100);
        float accidentes = brutoSobrePagos * (1f / 100);
        float fogasa = brutoSobrePagos * (0.2f / 100);

        System.out.println(
                "\n---------------------------------------------------------------------------------------------------------");
        System.out.println("Calculo empresario: BASE\t\t\t\t\t\t\t\t\t" + brutoSobrePagos);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Contingencias comunes empresario 23.6%\t\t\t\t\t\t\t\t" + sSocial);
        System.out.println("Desempleo 06.70%\t\t\t\t\t\t\t\t\t\t" + desempleo);
        System.out.println("Formación 00.60%\t\t\t\t\t\t\t\t\t\t" + formacion);
        System.out.println("Accidentes de trabajo 01.00%\t\t\t\t\t\t\t\t\t" + accidentes);
        System.out.println("FOGASA 00.20%\t\t\t\t\t\t\t\t\t\t\t" + fogasa);
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println(
                "Total empresario\t\t\t\t\t\t\t\t\t\t" + (sSocial + desempleo + formacion + accidentes + fogasa));

        return sSocial + desempleo + formacion + accidentes + fogasa;
    }

    /**
     * Busca la cuota de irpf
     * @param sueldo sueldo que cobra el trabajador
     * @return cuota de irpf
     */
    private float getIrpf(float sueldo){
        float auxSueldo = sueldo % 1000;
        int aux;
        if(sueldo<12000){
            return 0f;
        } else{
            if(auxSueldo==0){
                aux = (int)(auxSueldo);
                return Float.parseFloat(irpf.get((int)sueldo+"").replace(",", "."));
            }else{
                aux = (int)(sueldo+1000-auxSueldo);
                return Float.parseFloat(irpf.get(aux+"").replace(",", "."));  
            }
        }
        
    }
}
