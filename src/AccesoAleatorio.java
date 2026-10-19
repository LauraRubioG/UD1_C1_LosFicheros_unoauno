import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class AccesoAleatorio {

    private static final int TAMANO_BUTACA = 4;   // int = 4 bytes
    private static final int TAMANO_NOMBRE = 20;  // 10 chars * 2 bytes = 20 bytes
    private static final int TAMANO_FECHA = 8;    // long = 8 bytes
    private static final int TAMANO_REGISTRO = TAMANO_BUTACA + TAMANO_NOMBRE + TAMANO_FECHA; // 32 bytes

    public static void main(String[] args){

        // Usamos "rw" (read/write) para poder leer, escribir y desplazarnos por el fichero
        try (RandomAccessFile raf = new RandomAccessFile("butacas.dat", "rw")) {

            // --- 1. Escribir cinco butacas ---
            System.out.println("--- 1. ESCRITURA DE 5 BUTACAS ---");
            //date coje la hora exacta en ese momento, y getTime lo que hace es que la convierte e un numero muy largp
            //para que se pueda meter en un archivo binario
            long fechaActual = new Date().getTime();

            for (int i = 1; i <= 5; i++) {
                //Aquí escribes el identificador de la butaca. Como i va cambiando en cada vuelta,
                // en la primera vuelta esto escribe un 1 en el disco duro, en la segunda escribe un 2, etc.
                // Al usar writeInt(), te aseguras de que ese número ocupe sus 4 bytes matemáticos.
                raf.writeInt(i); // Escribe 4 bytes

                // Formatear el nombre para que ocupe exactamente 10 caracteres
                // Aquí simplemente estás fabricando el texto que vas a guardar.
                // Unes la palabra "Cliente" con el número de la vuelta actual.
                // En la primera vuelta, esta variable guardará el texto "Cliente1".
                String nombre = "Cliente" + i;
                //Esta es la parte más importante. El texto "Cliente1" solo tiene 8 letras.
                // Si lo escribes tal cual, solo ocuparía 16 bytes y romperías toda la estructura del fichero.
                //metodo extra (que seguramente está definido más abajo en tu clase) que se encarga de
                // arreglar esto: coge el texto "Cliente1", le añade dos espacios en blanco al final
                // ("Cliente1  ") para llegar a las 10 letras exactas
                escribirNombreFormateado(raf, nombre); // Escribe 20 bytes
                // coges la variable fechaActual (el número gigante de milisegundos que sacaste antes con
                // getTime()) y lo escribes con writeLong(), ocupando sus 8 bytes.
                raf.writeLong(fechaActual); // Escribe 8 bytes
            }

            // --- 4. Comprobación inicial de tamaño y puntero ---
            System.out.println("Después de escribir 5 registros:");
            System.out.println("Longitud del fichero: " + raf.length() + " bytes (5 * 32 = 160)");
            System.out.println("Posición del puntero: " + raf.getFilePointer());
            System.out.println("¿Cuadra con 32 bytes/registro?: " + (raf.length() == 5 * TAMANO_REGISTRO));


            // --- 2. Mostrar nombre de la butaca 5 sin leer las anteriores ---
            System.out.println("\n--- 2. LEER NOMBRE DE LA BUTACA 5 ---");
            // Cálculo del offset (desplazamiento). La butaca 5 es el registro índice 4 (0, 1, 2, 3, 4)
            // Nos saltamos 4 registros enteros (4 * 32 = 128 bytes)
            long offsetButaca5 = 4 * TAMANO_REGISTRO;

            // Nos saltamos el 'int' de la butaca (4 bytes) para llegar directamente al nombre
            raf.seek(offsetButaca5 + TAMANO_BUTACA); // Nos movemos al byte 128 + 4 = 132

            String nombreLeido = leerNombre(raf);
            System.out.println("Nombre en la butaca 5: " + nombreLeido.trim());
            System.out.println("Puntero tras leer 10 chars (20 bytes): " + raf.getFilePointer()); // 132 + 20 = 152


            // --- 3. Cambiar nombre de la butaca 3 sin tocar las demás ---
            System.out.println("\n--- 3. MODIFICAR NOMBRE DE LA BUTACA 3 ---");
            // La butaca 3 es el registro índice 2. Saltamos 2 registros enteros.
            long offsetButaca3 = 2 * TAMANO_REGISTRO; // 2 * 32 = 64 bytes

            // Nos saltamos el número de butaca para sobreescribir solo el nombre
            raf.seek(offsetButaca3 + TAMANO_BUTACA); // Nos movemos al byte 64 + 4 = 68

            escribirNombreFormateado(raf, "MartaModif");

            System.out.println("Nombre modificado con éxito.");
            System.out.println("Puntero tras sobreescribir el nombre: " + raf.getFilePointer()); // 68 + 20 = 88
            System.out.println("Longitud del fichero (no debe cambiar): " + raf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe un String asegurando que ocupa exactamente 10 caracteres (20 bytes).
     * Si es más corto, rellena con espacios. Si es más largo, lo trunca.
     */
    private static void escribirNombreFormateado(RandomAccessFile raf, String nombre) throws IOException {
        // os String normales son rígidos: una vez que creas el texto "Cliente1", no puedes modificar su tamaño directamente.
        //Un StringBuffer, en cambio, es como una caja de texto de plastilina. Te permite meter un texto dentro y luego moldearlo (recortarlo o estirarlo)
        // simplemente estamos metiendo el nombre original (por ejemplo, "Cliente1") dentro de esa caja de plastilina.
        StringBuffer sb = new StringBuffer(nombre);
        // "Me da igual lo que haya dentro de la caja de plastilina, quiero que a partir de este instante mida exactamente 10 caracteres".
        sb.setLength(10); // Fuerza la longitud a 10. Rellena con caracteres nulos si falta.
        // si el texto es largo te rellena los huecos con "caracteres nulos"
        //pero si se pasa de 10 te corta todo lo que sobra a partir de 10

        // writeChars escribe cada carácter usando 2 bytes (codificación UTF-16)
        // toString() para volver a convertirla en un texto normal y corriente.
        // Como el texto tiene 10 letras clavadas y sabemos que Java gasta 2 bytes por cada letra
        // la multiplicación es perfecta: 10 letras × 2 bytes = 20 bytes físicos escritos en el disco duro.
        raf.writeChars(sb.toString());
    }

    /**
     * Lee exactamente 10 caracteres (20 bytes) y devuelve el String formado.
     */
    private static String leerNombre(RandomAccessFile raf) throws IOException {
        char[] nombreChars = new char[10];
        for (int i = 0; i < 10; i++) {
            nombreChars[i] = raf.readChar(); // Lee 2 bytes por vuelta
        }
        return new String(nombreChars);
    }



    }



