/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map.Entry;
/**
 *
 * @author carlos
 */
public class NominasLogica {

        float salarioBase = 0;
        float complemento = 0;
        HashMap<String, String> trienios; 
        boolean prorrateo = false;
        HashMap<String, String> Cuotas; 
        HashMap<String, String> irpf;
        
    public NominasLogica(String[] salarioComple, String prorrateo, HashMap<String, String> trienios, HashMap<String, String> irpf, HashMap<String, String> Cuotas, Date fechaAlta, Date fechaNomina){
        
        this.salarioBase = Float.parseFloat(salarioComple[0]);
        this.complemento = Float.parseFloat(salarioComple[1]);
        this.trienios = trienios;
        this.Cuotas = Cuotas;
        this.irpf = irpf;
        if(prorrateo.equals("SI")){
            this.prorrateo = true;
        }else
            this.prorrateo = false;
//        LocalDate d1 = LocalDate.parse("2008-06-01", DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDate d2 = LocalDate.parse("2021-12-01", DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate localDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate1 = fechaNomina.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(localDate.toString());
                System.out.println(localDate1.toString());

        Period period = Period.between(localDate, localDate1);

        System.out.println("Salario base anual:\t" + salarioBase);
        System.out.println("Complemento anual:\t" + complemento);

        System.out.println("\nAños:\t\t" + period.getYears());
        System.out.println("Trienios:\t" + calcularTrienios(period));
        // El importe bruto se puede/debe hacer buscando en el excel, no a fuerza bruta
        System.out.println("Importe:\t" + importeBruto(calcularTrienios(period)) + " €/Mes" + "\t"
                + importeBruto(calcularTrienios(period)) * 14 + "€/Año");
        System.out.println();

        calcularNomina(salarioBase, complemento, importeBruto(calcularTrienios(period)) * 14);
    }
    
    public int calcularTrienios(Period period) {
        return Math.abs(period.getYears()) / 3;
    }

    public int importeBruto(int trienios) {
        if(trienios == 0)
            return 0;
        return Integer.parseInt(this.trienios.get(trienios+""));
    }

    public void calcularNomina(float salarioBase, float complemento, float antiguedad) {
        float salarioBaseMensual = salarioBase / 14;
        float complementoMensual = complemento / 14;
        float antiguedadMensual = antiguedad / 14;
        float prorrateoExtra = (salarioBaseMensual + complementoMensual + antiguedadMensual) / 6;
        float brutoMensual = 0;
        if (this.prorrateo)
            brutoMensual = salarioBaseMensual + complementoMensual + antiguedadMensual + prorrateoExtra;
        else
            brutoMensual = salarioBaseMensual + complementoMensual + antiguedadMensual;

        float cuotaSSocial = Float.parseFloat(Cuotas.get("Cuota obrera general TRABAJADOR").replace(",", "."))/100;
        float cuotaDesempleo = Float.parseFloat(Cuotas.get("Cuota desempleo TRABAJADOR").replace(",", "."))/100;
        float cuotaFormacion = Float.parseFloat(Cuotas.get("Cuota formación TRABAJADOR").replace(",", "."))/100;
        
        float brutoMensualExtra = 0;
        float brutoAnual = 0;
        // estos pagos se liquidan 12 veces al año
        if (this.prorrateo){
            brutoMensualExtra = brutoMensual;
            brutoAnual = brutoMensual*12;
        } else {
            brutoMensualExtra = brutoMensual + prorrateoExtra;
            brutoAnual = brutoMensual*14;
        }
        float cuotaIRPF = getIrpf(brutoAnual)/100;
        float seguridadSocial = brutoMensualExtra * cuotaSSocial;
        float desempleo = brutoMensualExtra * cuotaDesempleo;
        float formacion = brutoMensualExtra * cuotaFormacion;
        // este pago se hace con el brutoMensual
        float IRPF = brutoMensual * cuotaIRPF;

        float liquidoMensual = brutoMensual - seguridadSocial - desempleo - formacion - IRPF;

        System.out.println("Bruto anual:\t" + brutoAnual);
        System.out.println("Salario base mensual:\t" + salarioBaseMensual);
        System.out.println("Complemento mensual:\t" + complementoMensual);
        System.out.println("Antigüedad:\t\t" + antiguedadMensual);
        System.out.println("\nBruto cada mes:\t\t" + brutoMensual + "\n");

        System.out.println("AUX:\t\t\t" + brutoMensualExtra);
        System.out.println("S. Social:\t\t" + seguridadSocial);
        System.out.println("Desempleo:\t\t" + desempleo);
        System.out.println("Formacion:\t\t" + formacion);
        System.out.println("IRPF:\t\t\t" + IRPF);
        System.out.println("\nLiquido mensual:\t" + liquidoMensual + "\n");
    }

    private LocalDate convertDateToLocalDateUsingOfEpochMilli(Date fechaAlta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private float getIrpf(float sueldo){
        float auxSueldo = sueldo % 1000;
        int aux = 0;
        if(auxSueldo==0){
            aux = (int)auxSueldo;
            return Float.parseFloat(irpf.get(aux+"").replace(",", "."));
        }else{
           aux = (int)(sueldo+1000-auxSueldo);
           return Float.parseFloat(irpf.get(aux+"").replace(",", "."));  
        }
    }
}
