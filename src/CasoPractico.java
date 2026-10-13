import java.io.File;
import java.io.IOException;

public class CasoPractico {

    // Variables globales (estáticas) para llevar el recuento final (Paso 5)
    static int totalFicheros = 0;
    static int totalCarpetas = 0;
    static long totalBytes = 0;

    public static void main(String[] args) {
        // --- 1. Crear directorios ---
        System.out.println("--- 1. CREACIÓN DE CARPETAS ---");

        // El objeto File solo representa la ruta, no crea nada en el disco todavía.
        File entrada = new File("practica/entrada");
        File salida = new File("practica/salida");
        File copia = new File("practica/copia");

        // mkdirs() devuelve true si crea las carpetas. 
        // Al llamar a entrada.mkdirs(), crea tanto "practica" como "entrada".
        boolean creadas = entrada.mkdirs();

        // Como "practica" ya existe, usamos mkdir() (o mkdirs()) para las hermanas.
        salida.mkdirs();
        copia.mkdirs();

        System.out.println("Carpetas creadas: " + creadas);

        // --- 2. Mover un fichero con renameTo() ---
        System.out.println("\n--- 2 y 3. MOVER FICHERO Y MOSTRAR RUTAS ---");

        // Primero, creamos un fichero temporal en la raíz del proyecto para poder moverlo
        File ficheroOrigen = new File("fichero_prueba.txt");
        try {
            ficheroOrigen.createNewFile(); // Lo creamos físicamente
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de prueba.");
        }

        // Definimos la ruta de destino dentro de la carpeta 'entrada'
        File ficheroDestino = new File(entrada, "fichero_prueba.txt");

        // renameTo() mueve (o renombra) el fichero. Devuelve true si tiene éxito.
        boolean movido = ficheroOrigen.renameTo(ficheroDestino);
        System.out.println("¿Fichero movido con éxito? " + movido);

        // --- 3. Mostrar getName() y getAbsolutePath() ---
        // getName() devuelve solo el nombre del archivo (con su extensión)
        System.out.println("Nombre del fichero: " + ficheroDestino.getName());
        // getAbsolutePath() devuelve la ruta completa desde la raíz de tu disco duro
        System.out.println("Ruta absoluta: " + ficheroDestino.getAbsolutePath());

        // --- 4. Listar recursivamente ---
        System.out.println("\n--- 4. LISTADO RECURSIVO ---");
        File directorioPractica = new File("practica");
        // Llamamos a nuestro método recursivo, pasándole el directorio y un nivel inicial de sangría (0)
        listarRecursivo(directorioPractica, 0);

        // --- 5. Mostrar recuentos totales ---
        System.out.println("\n--- 5. TOTALES ---");
        System.out.println("Total carpetas: " + totalCarpetas);
        System.out.println("Total ficheros: " + totalFicheros);
        System.out.println("Total bytes: " + totalBytes + " (" + (totalBytes / 1024.0) + " KB)");

        // --- 6. Borrado recursivo y prueba de delete() ---
        System.out.println("\n--- 6. BORRADO DE CARPETA NO VACÍA ---");
        // Intentamos borrar 'entrada' directamente. Como contiene 'fichero_prueba.txt', no está vacía.
        boolean borradoFallo = entrada.delete();
        System.out.println("¿Se borra con delete() una carpeta con contenido? " + borradoFallo);
        // Respuesta: NO. delete() devuelve false y no borra nada si la carpeta tiene contenido.

        // Llamamos al borrado recursivo real para limpiarlo
        borrarRecursivo(entrada);
        System.out.println("¿Y usando un borrado recursivo manual? " + !entrada.exists());
    }

    /**
     * Método recursivo para listar el contenido de un directorio.
     */
    public static void listarRecursivo(File elemento, int nivel) {
        // Generamos la sangría según la profundidad en el árbol
        String sangria = "";
        for (int i = 0; i < nivel; i++) {
            sangria += "  "; // Dos espacios por cada nivel de profundidad
        }

        // Distinguimos si es carpeta o fichero
        if (elemento.isDirectory()) {
            System.out.println(sangria + "[CARPETA] " + elemento.getName());
            totalCarpetas++; // Sumamos al contador global

            // listFiles() devuelve un array de objetos File que contiene la carpeta
            File[] hijos = elemento.listFiles();

            // MUY IMPORTANTE: comprobar que no sea null (por falta de permisos, por ejemplo)
            if (hijos != null) {
                // Si tiene contenido, recorremos cada hijo
                for (File hijo : hijos) {
                    // Nos llamamos a nosotros mismos (recursividad) aumentando el nivel de sangría
                    listarRecursivo(hijo, nivel + 1);
                }
            }
        } else if (elemento.isFile()) {
            // Si es un fichero, mostramos su nombre y tamaño
            long tamano = elemento.length(); // length() devuelve el tamaño en bytes
            System.out.println(sangria + "[FICHERO] " + elemento.getName() + " - " + tamano + " bytes");

            totalFicheros++; // Sumamos al contador de ficheros
            totalBytes += tamano; // Sumamos los bytes al total
        }
    }

    /**
     * Método recursivo para borrar un directorio y todo su contenido.
     */
    public static void borrarRecursivo(File elemento) {
        if (elemento.isDirectory()) {
            File[] hijos = elemento.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    borrarRecursivo(hijo); // Borramos los hijos primero
                }
            }
        }
        // Una vez vaciada (si era carpeta) o directamente (si era fichero), borramos el elemento
        elemento.delete();
    }
}