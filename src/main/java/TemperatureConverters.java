public interface TemperatureConverters {

    static double FtoC(double f){
        return (f-32) * 5.0 / 9.0;

    }

    static double CtoF(double c){
        return (c * 9.0 / 5.0) + 32;
    }
}
