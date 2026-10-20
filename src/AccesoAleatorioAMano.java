import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AccesoAleatorioAMano {


    //Primero definimos la estructua de la topologia del registro
    public static final int tamano_numero_butaca = 4; //es un entero por p tanto int = 4 bytes
    public static final int tamano_nombre = 20; //es un char[10] cada letra son 2 bytes por lo tando 10*2=20bytes
    public static final int tamano_fecha = 8; //al final la fecha es un numero de milisegundo, por lo tando es un long,
    //un long es el doble de int = 32bis = 4bytes por lo tando long = 64bist = 8bytes

    //El tamaño total del registro se calcula sumando cada uno de los butes que los compone
    private static final int tamano_registro = tamano_numero_butaca + tamano_nombre + tamano_fecha; // 32 bytes

    public static void main(String[] args){

        //directamente lo metemos en un try, porque la clase RandomAccessFie lanza excepcion
        //usamos el parametrro rw para poder escribir y leer el archivo, ademas así tambien creamos el archivo
        //ya que con la clade de RandomAccessFile se crea directamente en el constructor usando rw
        try(RandomAccessFile raf = new RandomAccessFile("butaca.dat", "rw")){

            //escribimos las cinco primeras butacas, por lo que lo vamos a hacer con un for, en el
            //debemos escribir todos los bytes que ocupa cada bucata y asi con las 5 que hay que escribir
            for(int i = 1; i <= 5; i++){
                //lo primero que tenemos que hacer es escribir el numero
                // Al usar writeInt(), te aseguras de que ese número ocupe sus 4 bytes matemáticos.
                raf.writeInt(i);

                //escribimos ahora el nombre
            }




        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
