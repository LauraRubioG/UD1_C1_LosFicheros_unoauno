import java.io.*;

public class BytesIntRead {

    public static void main(String[] args){

        //----------------CREAR ORIGINAL BIEN Y ESCRIBIT UNO A UNO---------------------

        //instanciamos los ficheros de forma general para poder usarlo en todo el programa
        File original = new File("original.bin");
        File copia = new File("copia.bin");

        //---------creamos original.bien y lo rellenamos
        //siempre en try dentro de try
        try{
            try (FileOutputStream escribir = new FileOutputStream(original)){
                //Creamos un FileOutputStream para despues poder rellenarlo con bytes
                //FileOutputStream escribir = new FileOutputStream(original);

                //para rellenarlo usamos un for y lo rellenamos con 20000 byte uno a uno
                for(int i = 0; i < 20000; i++){
                    //escribimos los bytes, usamos i % 256 para que el valor no se pase del limite de un byte 0-255
                    escribir.write(i % 256);
                }

                //hayq eu cerrar el flujo, importante
                escribir.close();
                System.out.println("Hemos creado con exito el fichero con 20000 byte dentro"  + original.getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //---------------copiamos en copia.bien byte a byte con FileInputStream----------------
            //necesitamos crear un FileInputStram para leer el fichero anterior, y despues
            //volver a crear un FileOutputStream para escribir de nuevo
            //(hay que tener en cuenta que si lo creamos arriba se puede reulitizar, pero por cada try solo funciona
            // en ese try, ademas tampoco tendria sentido crearlo arriba porque siempre que se crea hayq ue cerrarlo)
            try(FileInputStream lector = new FileInputStream(original);
                FileOutputStream escritor = new FileOutputStream(copia)){
                //FileInputStream lector = new FileInputStream(original);
                //FileOutputStream escritor = new FileOutputStream(copia);

                int byteLeido;
                //contador para lo que byte que vamos copiando
                int contadorBytes = 0;
                //antes de entrar al primer byte lo guardamos en la variable
                byteLeido = lector.read();

                //creamos el bucle while, porque ahora mismo si sabemos la cantidad que tiene pero en un fututo a lo mejor no
                while(byteLeido != -1){
                    escritor.write(byteLeido);
                    contadorBytes++;
                    //leemos el siguiente byte al final del buble y lo guardammos en la misma variable
                    byteLeido = lector.read();
                }
                //cerramos
                lector.close();
                escritor.close();
                System.out.println("Se ha copiado con exito");

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            //--------------------- comprobamos que miden lo mismo------------------
            try {
                // guardamo la longitudes de variable
                long longitudOriginal = original.length();
                long longitudCopia = copia.length();
                //imprimimos los tamaños para verlo
                System.out.println("El tamaño del archivo original es: " + longitudOriginal + " bytes.");
                System.out.println("El tamaño del archivo copia es: " + longitudCopia + " bytes.");

                //los comprobamos con un if
                if (longitudOriginal == longitudCopia) {
                    System.out.println("Los dos ficheros miden lo mismo");
                } else {
                    System.out.println("No tiene la misma longitud");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //-----------leemos los 5 primeros bytes-------------
            try(FileReader lectorformaTexto = new FileReader(original);){
                //FileReader solo lee texto no binarios como punto .bin, por lo que mas tarde tendremos que convertirlo
                //FileReader lectorformaTexto = new FileReader(original);
                //leemos con bucle for poque sabemos que es 5
                for (int i = 0; i < 5; i++) {
                    int leido = lectorformaTexto.read();
                    // Lo convertimos a char para ver cómo interpreta el texto
                    //ponemos (char) y la variable que queremos convertir
                    System.out.print((char) leido + " ");
                }
                //salto de liena jump
                System.out.println();

                // Cerramos como siempre
                lectorformaTexto.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //------------- teoria --------------
            /*
            read int y no byte
                - En Java, un tipo 'byte' tiene signo y sus valores van del -128 al 127.
                - El metodo read() necesita una forma de avisar que ha llegado al final del archivo (End Of File). Para eso usa el valor -1.
                - Si read() devolviera un 'byte', al leer un dato que sea exactamente puros unos (0xFF, que en byte con signo equivale a -1), Java pensaría que es el final del archivo por error.
                - Al devolver un 'int' (que es mucho más grande), read() puede devolver los valores reales del byte como números positivos (del 0 al 255) y reservar el -1 estrictamente para indicar que el archivo se ha acabado.

             valor sale byte 0xFF y nunca es -1
                - Cuando read() lee el byte 0xFF, lo transforma y devuelve como el número entero 255.
                - Nunca lo confunde con un -1 porque el -1 en tipo 'int' tiene una representación binaria diferente (todos sus 32 bits a 1: 0xFFFFFFFF). Por tanto, la lectura de datos reales y el aviso de final de archivo nunca se cruzan.
             */

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
