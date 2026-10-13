public class AccesoAleatorioAMano {


    //Primero definimos la estructua de la topologia del registro
    private static final int tamano_butaca = 4; //int = 4 bytes
    private static final int tamano_nombre = 20; //10 chars * 2 bytes = 20 bytes
    private static final int tamano_fecha = 8; // long = 8 bytes

    //El tamaño total del registro se calcula de forma automática en tiempo de compilacion
    private static final int tamano_registro = tamano_butaca + tamano_nombre + tamano_fecha; // 32 bytes

    public static void main(String[] args){



    }


}
