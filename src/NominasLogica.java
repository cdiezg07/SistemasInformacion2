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
         
    public NominasLogica(String[] salarioComple, String prorrateo, HashMap<String, String> trienios, HashMap<String, String> irpf, HashMap<String, String> Cuotas, Date fechaAlta, Date fechaNomina){
        
        this.salarioBase = Float.parseFloat(salarioComple[0]);
        this.complemento = Float.parseFloat(salarioComple[1]);
        this.trienios = trienios;
        this.Cuotas = Cuotas;
        this.irpf = irpf;
        antiguedadMensual = new int[14];
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
        System.out.println("Importe:\t" + importeBruto(localDate, period)+ "€/Año");
        System.out.println();

        calcularNomina(salarioBase, complemento, importeBruto(localDate, period), localDate1.getMonthValue(), localDate.getMonthValue());
    }
    
    public int calcularTrienios(Period period) {
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
//        if (this.prorrateo)
//            brutoMensual = salarioBaseMensual + complementoMensual + antiguedadMensual + prorrateoExtra;
//        else
//            brutoMensual = salarioBaseMensual + complementoMensual + antiguedadMensual;
//
//        float cuotaSSocial = Float.parseFloat(Cuotas.get("Cuota obrera general TRABAJADOR").replace(",", "."))/100;
//        float cuotaDesempleo = Float.parseFloat(Cuotas.get("Cuota desempleo TRABAJADOR").replace(",", "."))/100;
//        float cuotaFormacion = Float.parseFloat(Cuotas.get("Cuota formación TRABAJADOR").replace(",", "."))/100;
        
        float brutoMensualExtra = 0;
        float brutoAnual = 0;
        //Calcular el bruto anual siempre con prorrateo
        
        // estos pagos se liquidan 12 veces al año
        if (this.prorrateo){
            if(mesFechaAlta>=7 && mesFechaAlta<=11){
                brutoAnual = salarioBase + complemento + antiguedadAnual + (this.antiguedadMensual[13]/6);
            }else{
                brutoAnual = salarioBase + complemento + antiguedadAnual;
            }
//            brutoMensualExtra = brutoMensual;
//            brutoAnual = brutoMensual*12;
        } else {
            brutoAnual = salarioBase + complemento + antiguedadAnual;
//            brutoMensualExtra = brutoMensual + prorrateoExtra;
//            brutoAnual = brutoMensual*14;
        }
//        float cuotaIRPF = getIrpf(brutoAnual)/100;
//        float seguridadSocial = brutoMensualExtra * cuotaSSocial;
//        float desempleo = brutoMensualExtra * cuotaDesempleo;
//        float formacion = brutoMensualExtra * cuotaFormacion;
//        // este pago se hace con el brutoMensual
//        float IRPF = brutoMensual * cuotaIRPF;
//
//        float liquidoMensual = brutoMensual - seguridadSocial - desempleo - formacion - IRPF;
//
        System.out.println("Bruto anual:\t" + brutoAnual);
        System.out.println("Salario base mensual:\t" + salarioBaseMensual);
        System.out.println("Complemento mensual:\t" + complementoMensual);
        System.out.println("Antigüedad:\t\t" + antiguedadMensual);
        System.out.println("\nBruto cada mes:\t\t" + brutoMensual + "\n");
        System.out.println("\nProrrateo extra:\t" + prorrateoExtra + "\n");
//
//        System.out.println("AUX:\t\t\t" + brutoMensualExtra);
//        System.out.println("S. Social:\t\t" + seguridadSocial);
//        System.out.println("Desempleo:\t\t" + desempleo);
//        System.out.println("Formacion:\t\t" + formacion);
//        System.out.println("IRPF:\t\t\t" + IRPF);
//        System.out.println("\nLiquido mensual:\t" + liquidoMensual + "\n");
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