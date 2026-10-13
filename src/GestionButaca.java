import java.io.IOException;
import java.io.RandomAccessFile;

public class GestionButaca {

    // Definimos el tamaño total del bloque.
    // Por qué: Si cambiamos el tamaño de los datos en el futuro, solo cambiamos este número.
    // Cálculo: int(4) + char[10](20) + long(8) = 32 bytes.
    private static final int TAMANO_REGISTRO = 32;

    public static void main(String[] args) {

        System.out.println("--- 1. ESCRITURA DE LAS 5 BUTACAS ---");

        // El bloque try-with-resources (con paréntesis) asegura que el fichero
        // se cierre solo (raf.close()) cuando termine, liberando la memoria.
        try (RandomAccessFile raf = new RandomAccessFile("butacas.dat", "rw")) {

            // Vaciamos el archivo a 0 bytes.
            // Por qué: Para evitar que se acumulen butacas de pruebas anteriores.
            raf.setLength(0);

            String[] nombres = {"Laura", "Marta", "Carlos", "Ana", "Luis"};

            // Guardamos el momento exacto actual. Un 'long' requiere 8 bytes.
            long fechaActual = System.currentTimeMillis();

            // Bucle para escribir los 5 registros seguidos.
            for (int i = 0; i < 5; i++) {
                // 1. Escribimos el ID de la butaca (1, 2, 3, 4, 5).
                // El puntero avanza 4 bytes.
                raf.writeInt(i + 1);

                // 2. Escribimos el nombre.
                // Llamamos a formatearNombre() para asegurar que tenga 10 letras.
                // Al usar writeChars, escribe 10 letras x 2 bytes/letra.
                // El puntero avanza 20 bytes.
                raf.writeChars(formatearNombre(nombres[i]));

                // 3. Escribimos la fecha.
                // El puntero avanza 8 bytes.
                raf.writeLong(fechaActual);

                // En cada vuelta del bucle, el puntero avanza exactamente 32 bytes (4 + 20 + 8).
            }

            // Tras 5 vueltas, el puntero debe estar en 5 * 32 = 160.
            System.out.println("Puntero actual (getFilePointer): " + raf.getFilePointer());
            System.out.println("Tamaño total del fichero (length): " + raf.length());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }


        System.out.println("\n--- 2. LECTURA DIRECTA DE LA BUTACA 5 ---");

        // Abrimos en modo "r" (solo lectura) porque no vamos a modificar nada.
        try (RandomAccessFile raf = new RandomAccessFile("butacas.dat", "r")) {

            // Calculamos matemáticamente dónde empieza el quinto bloque de datos.
            // Fórmula: (RegistroBuscado - 1) * TamañoDelRegistro.
            // (5 - 1) * 32 = 128. La butaca 5 empieza en el byte 128.
            long posicion = (5 - 1) * TAMANO_REGISTRO;

            // Movemos el cursor directamente al byte 128. Saltamos 128 bytes de golpe.
            raf.seek(posicion);

            // Leemos el entero (4 bytes). El puntero pasa del 128 al 132.
            int idButaca = raf.readInt();

            // Leemos el nombre. Como sabemos que ocupa 10 caracteres, hacemos un bucle.
            String nombreLeido = "";
            for (int i = 0; i < 10; i++) {
                // readChar() lee 2 bytes en cada vuelta.
                // El puntero pasa del 132 al 152.
                nombreLeido += raf.readChar();
            }

            // Leemos el long (8 bytes). El puntero pasa del 152 al 160.
            long fechaLeida = raf.readLong();

            // El método trim() elimina los espacios vacíos que usamos de relleno.
            System.out.println("Datos leídos -> Butaca: " + idButaca + ", Nombre: " + nombreLeido.trim());
            System.out.println("Puntero tras leer (getFilePointer): " + raf.getFilePointer());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }


        System.out.println("\n--- 3. MODIFICACIÓN DEL NOMBRE DE LA BUTACA 3 ---");

        // Abrimos en modo "rw" porque vamos a sobrescribir un dato.
        try (RandomAccessFile raf = new RandomAccessFile("butacas.dat", "rw")) {

            // Calculamos dónde empieza la butaca 3.
            // (3 - 1) * 32 = 64. Empieza en el byte 64.
            long inicioButaca3 = (3 - 1) * TAMANO_REGISTRO;

            // IMPORTANTE: Solo queremos cambiar el nombre.
            // El nombre está DESPUÉS del ID (que ocupa 4 bytes).
            // Así que al byte 64 le sumamos 4 bytes para saltarnos el ID.
            long posicionNombre = inicioButaca3 + 4;

            // Movemos el cursor al byte 68.
            raf.seek(posicionNombre);

            // Sobrescribimos el nombre con el nuevo texto formateado a 10 letras.
            // Esto aplasta los 20 bytes antiguos y los cambia por 20 bytes nuevos.
            // El puntero pasa del 68 al 88. Las fechas y el ID quedan intactos.
            raf.writeChars(formatearNombre("NuevoNom"));

            System.out.println("Puntero tras modificar (getFilePointer): " + raf.getFilePointer());
            System.out.println("Tamaño total (length): " + raf.length());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para estandarizar el tamaño de los Strings.
     * Por qué se usa: Para el acceso aleatorio todos los registros deben medir lo mismo.
     * Si guardamos "Ana", sobra espacio. Este método lo convierte en "Ana       " (10 letras).
     */
    private static String formatearNombre(String nombre) {
        StringBuilder sb = new StringBuilder(nombre);
        // Fuerza al texto a medir exactamente 10 caracteres. Rellena con vacíos o recorta si es muy largo.
        sb.setLength(10);
        return sb.toString();
    }
}

/*
new RandomAccessFile(ruta, modo): Abre o crea un fichero binario. El modo "rw" significa
Read/Write (lectura y escritura), y "r" es Read (solo lectura). Lo usamos porque es la única clase
en Java que permite saltar a cualquier parte del fichero sin tener que leerlo desde el principio.

setLength(long tamaño): Fuerza al fichero a tener un tamaño exacto en bytes. Lo usamos con setLength(0)
al principio para borrar cualquier dato viejo de ejecuciones anteriores y empezar con un fichero totalmente
vacío.

seek(long posicion): Mueve el "cursor" (puntero) del disco duro a un byte específico. Es el núcleo del
acceso aleatorio. Lo usamos para saltar directamente a la butaca 5 o a la butaca 3 sin procesar el resto
de datos.

writeInt(int v) / readInt(): Escribe o lee un número entero. Lo usamos para el ID de la butaca porque
Java siempre reserva 4 bytes exactos para los datos de tipo int.

writeInt(int v) / readInt(): Escribe o lee un número entero.
Lo usamos para el ID de la butaca porque Java siempre reserva 4 bytes exactos para los datos de tipo int

writeChars(String s): Coge un texto y escribe sus caracteres uno a uno. En Java, cada carácter (char)
ocupa 2 bytes. Lo usamos para el nombre, y como forzamos a que el texto tenga siempre 10 caracteres,
sabemos que siempre escribirá exactamente 20 bytes.

readChar(): Lee un único carácter (2 bytes) y avanza el puntero. Lo usamos dentro de un bucle for de 10
vueltas para reconstruir el nombre (2 bytes x 10 = 20 bytes).

writeLong(long v) / readLong(): Escribe o lee un número entero largo. Lo usamos para la fecha
System.currentTimeMillis()) porque los milisegundos son números gigantes, y Java reserva 8 bytes exactos
para el tipo long.

getFilePointer(): Nos dice en qué byte exacto está parado el cursor en ese momento. Lo usamos para
comprobar matemáticamente que, después de leer o escribir, nos hemos movido exactamente los bytes que
esperábamos.

length(): Devuelve el peso total del fichero en el disco duro. Lo usamos para demostrar que el archivo
crece exactamente de 32 en 32 bytes por cada registro añadido.
 */