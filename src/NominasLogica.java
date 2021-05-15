package pruebas;

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
        int[] antiguedadMensual;
        boolean añoIncompleto=false;
         
    public NominasLogica(String[] salarioComple, String prorrateo, HashMap<String, String> trienios, HashMap<String, String> irpf, HashMap<String, String> Cuotas, Date fechaAlta, Date fechaNomina){
        //public NominasLogica(){
        this.salarioBase = Float.parseFloat(salarioComple[0]);
        /*this.salarioBase = 12000;
        this.complemento = 1000;
        this.trienios = new HashMap<String, String>();
        trienios.put("0", "0");
        trienios.put("1", "15");
        trienios.put("2", "25");
        trienios.put("3", "45");
        trienios.put("4", "60");
        trienios.put("5", "70");
        trienios.put("6", "83");
        trienios.put("7", "90");
        trienios.put("8", "100");
        trienios.put("9", "112");
        trienios.put("10", "125");
        trienios.put("11", "140");
        trienios.put("12", "160");
        trienios.put("13", "170");
        trienios.put("14", "182");
        trienios.put("15", "190");
        trienios.put("16", "202");
        trienios.put("17", "215");
        trienios.put("18", "230");*/
        this.Cuotas = Cuotas;
        this.irpf = irpf;
        antiguedadMensual = new int[14];
        if(prorrateo.equals("SI")){
            this.prorrateo = true;
        }else
            this.prorrateo = false;
        //prorrateo=true;
//        LocalDate d1 = LocalDate.parse("2008-06-01", DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDate d2 = LocalDate.parse("2021-12-01", DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate localDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //LocalDate localDate = LocalDate.of(2013, 10, 1);  
        LocalDate localDate1 = fechaNomina.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //LocalDate localDate1 =LocalDate.of(2013, 12, 1);;
        System.out.println(localDate.toString());
                System.out.println(localDate1.toString());

        Period period = Period.between(localDate, localDate1);

        System.out.println("Salario base anual:\t" + salarioBase);
        System.out.println("Complemento anual:\t" + complemento);

        System.out.println("\nAños:\t\t" + period.getYears());
        System.out.println("Trienios:\t" + calcularTrienios(period));
        // El importe bruto se puede/debe hacer buscando en el excel, no a fuerza bruta
        System.out.println("Importe:\t" + importeBruto(localDate, period)+ "€/Año");
        System.out.println();

        calcularNomina(salarioBase, complemento, importeBruto(localDate, period), localDate1.getMonthValue(), localDate.getMonthValue());
    }
    
    public int calcularTrienios(Period period) {
    	if(period.getYears()==0) añoIncompleto=true;
        return Math.abs(period.getYears()) / 3;
    }

    public int importeBruto(LocalDate fechaAlta, Period period) {
        int trienioSuperior;
        int trienioInferior = 0;
       
        int trienios = Math.abs(period.getYears()) / 3;
        if(trienios == 0)
            return 0; 
        if(Math.abs(period.getYears()) % 3 == 0){
            int mesInicio = fechaAlta.getMonthValue();
            int numExtras = 0;
            int mesesTrienioSuperior = 12-mesInicio;
            if(mesesTrienioSuperior>0){
                if(mesesTrienioSuperior>6){
                   numExtras += 2; 
                }else{
                    numExtras += 1; 
                }
            }
            trienioSuperior = (mesesTrienioSuperior+numExtras)*Integer.parseInt(this.trienios.get((trienios)+""));
//            for(int i=(mesesTrienioSuperior+numExtras); i>=(14-mesesTrienioSuperior-numExtras); i--){
//                this.antiguedadMensual[i] = Integer.parseInt(this.trienios.get((trienios)+""));
//            }
            int i=13;
            while(i>=(14-mesesTrienioSuperior-numExtras)){
                this.antiguedadMensual[i] = Integer.parseInt(this.trienios.get((trienios)+""));
                i--;
            }
            if(trienios-1!=0){
                trienioInferior = (14-mesesTrienioSuperior-numExtras)*Integer.parseInt(this.trienios.get((trienios-1)+""));
//                for(i=(14-mesesTrienioSuperior-numExtras-1); i>=0; i--){
//                    this.antiguedadMensual[i] = Integer.parseInt(this.trienios.get((trienios-1)+""));
//                }
                while(i>=0){
                    this.antiguedadMensual[i] = Integer.parseInt(this.trienios.get((trienios)+""));
                    i--;
                }
            }
            return trienioInferior+trienioSuperior;
        }else{
            for(int i=14-1; i>=0; i--){
                    this.antiguedadMensual[i] = Integer.parseInt(this.trienios.get((trienios)+""));
            }
            return 14*Integer.parseInt(this.trienios.get(trienios+""));
        }
       
    }

    public void calcularNomina(float salarioBase, float complemento, float antiguedadAnual, int mesNomina, int mesFechaAlta) {
        float salarioBaseMensual = salarioBase / 14;
        float complementoMensual = complemento / 14;
        float antiguedadMensual = 0;
        float antiguedad = 0;
        if(mesNomina==12 || mesNomina<6){
            antiguedadMensual = this.antiguedadMensual[5]; 
        }else{
            antiguedadMensual = this.antiguedadMensual[11]; 
        }
       
        float prorrateoExtra;
        if(this.antiguedadMensual[mesNomina-1]!=0){
            prorrateoExtra = (salarioBaseMensual + complementoMensual + antiguedadMensual) / 6;
        }else{
            prorrateoExtra = (salarioBaseMensual + complementoMensual) / 6;
        }
        float brutoMensual = 0;
        brutoMensual = salarioBaseMensual + complementoMensual + this.antiguedadMensual[mesNomina-1] + prorrateoExtra;
        /*if (this.prorrateo)
            brutoMensual = salarioBaseMensual + complementoMensual + this.antiguedadMensual[mesNomina-1] + prorrateoExtra;
        else
            brutoMensual = salarioBaseMensual + complementoMensual + this.antiguedadMensual[mesNomina-1];*/

        float cuotaSSocial = Float.parseFloat(Cuotas.get("Cuota obrera general TRABAJADOR").replace(",", "."))/100;
        //float cuotaSSocial = 0.047f;
        float cuotaDesempleo = Float.parseFloat(Cuotas.get("Cuota desempleo TRABAJADOR").replace(",", "."))/100;
        //float cuotaDesempleo = 0.016f;
        float cuotaFormacion = Float.parseFloat(Cuotas.get("Cuota formación TRABAJADOR").replace(",", "."))/100;
        //float cuotaFormacion =0.01f;
        float contingencias = Float.parseFloat(Cuotas.get("Contingencias comunes EMPRESARIO").replace(",", "."))/100;
        //float contingencias = 0.23f;
        float fogasa = Float.parseFloat(Cuotas.get("Fogasa EMPRESARIO").replace(",", "."))/100;
        //float fogasa = 0.002f;
        float desempleo = Float.parseFloat(Cuotas.get("Desempleo EMPRESARIO").replace(",", "."))/100;
        //float desempleo =0.067f;
        float formacion = Float.parseFloat(Cuotas.get("Formacion EMPRESARIO").replace(",", "."))/100;
        //float formacion =0.006f;
        float brutoMensualExtra = 0;
        float brutoAnual = 0;
        //Calcular el bruto anual siempre con prorrateo
        
        // estos pagos se liquidan 12 veces al año
        if(añoIncompleto) {
        	if (this.prorrateo) {
        		brutoAnual = brutoMensual*(mesNomina-mesFechaAlta);
        		brutoMensualExtra = brutoMensual*(mesFechaAlta/mesNomina);
        	}else {
        		
        		if((mesNomina-mesFechaAlta)<6) {
        			//System.out.println("si");
        			float aux=(float) (mesFechaAlta%6)/6;
        			brutoAnual += brutoMensual*aux;
        			//System.out.println(mesFechaAlta%6+"/"+aux);
        		}
        		if((mesNomina-mesFechaAlta)>=6) {
        			//System.out.println("no");
        			brutoAnual += brutoMensual + brutoMensual*((mesFechaAlta%6)/6);
        		}
        		brutoAnual += brutoMensual*(mesNomina-mesFechaAlta);
        		brutoMensualExtra = brutoMensual + prorrateoExtra;
        	}
    	}else if (this.prorrateo){
            if(mesFechaAlta>=7 && mesFechaAlta<=11){
                brutoAnual = salarioBase + complemento + antiguedadAnual + (this.antiguedadMensual[13]/6);
            }else{
                brutoAnual = salarioBase + complemento + antiguedadAnual;
            }
            brutoMensualExtra = brutoMensual;
           // brutoAnual = brutoMensual*12;
        } else {
        	//no me fio
            brutoAnual = salarioBase + complemento + antiguedadAnual;
            brutoMensualExtra = brutoMensual + prorrateoExtra;
            //brutoAnual = brutoMensual*14;
        }
        
        float cuotaIRPF = getIrpf(brutoAnual)/100;
        //float cuotaIRPF =0.0f;
        float seguridadSocial = brutoMensual * cuotaSSocial;
        float cdesempleo = brutoMensual * cuotaDesempleo;
        float cformacion = brutoMensual * cuotaFormacion;
        // este pago se hace con el brutoMensual
        float IRPF = brutoMensual * cuotaIRPF;
        
        float contingenciasE=contingencias*brutoMensual;
        float fogasaE=fogasa*brutoMensual;
        float desempleoE=desempleo*brutoMensual;
        float formacionE=formacion*brutoMensual;
        float liquidoMensual = brutoMensual - seguridadSocial - desempleo - formacion - IRPF;
        float costeTotal = liquidoMensual + formacionE + desempleoE + fogasaE + contingenciasE;

        System.out.println("Bruto anual:\t" + brutoAnual);
        System.out.println("Salario base mensual:\t" + salarioBaseMensual);
        System.out.println("Complemento mensual:\t" + complementoMensual);
        System.out.println("Antigüedad:\t\t" + antiguedadMensual);
        System.out.println("\nBruto cada mes:\t\t" + brutoMensual + "\n");
        //System.out.println("\nProrrateo extra:\t" + prorrateoExtra + "\n");

        //System.out.println("AUX:\t\t\t" + brutoMensualExtra);
        System.out.println("S. Social:\t\t" + seguridadSocial);
        System.out.println("Desempleo:\t\t" + cdesempleo);
        System.out.println("Formacion:\t\t" + cformacion);
        System.out.println("IRPF:\t\t\t" + IRPF);
        System.out.println("\nLiquido mensual:\t" + liquidoMensual + "\n");
        
        System.out.println("Costes empresario");
        System.out.println("Contingencias Empresario:\t"+contingenciasE);
        System.out.println("Fogasa Empresario:\t\t"+fogasaE);
        System.out.println("Desempleo Empresario:\t\t"+desempleoE);
        System.out.println("Formacion Empresario:\t\t"+formacionE);
        System.out.println("Total Empresario:\t\t"+costeTotal);
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
