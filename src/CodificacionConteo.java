import java.io.*;

public class CodificacionConteo {


    public static void main(String[] args){

        //instanciamos
        File archivo = new File("frases.txt");

        //-----------------ESCRIBIMOS FRASES CON FILEOUTPUTSTREAM -----------------
        try {
            try (FileOutputStream escribir = new FileOutputStream(archivo)) {

                // guardamos que vamos a escribir
                String frase = "Programación\nmañana\ncamión\n";

                // Convertimos el String a un array de bytes usando la codificación UTF-8
                byte[] bytesfrase = frase.getBytes("UTF-8");

                escribir.write(bytesfrase);
                escribir.close();

                System.out.println("Hemos creado y eescrito el archivo en UTF-8\n");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //------------------LEEMOS CON BUFFERREADER EN UTF-8-----------------
            try (FileInputStream leebinario = new FileInputStream(archivo);
                 InputStreamReader traductor = new InputStreamReader(leebinario);
                 BufferedReader lector = new BufferedReader(traductor)){
                //instanciamos
                //va directo y lee el archivo directamente en formato binario

                //traduce los byte de antes y los traduce a caracters

                //usamos bufferedReader


                String linea;
                // Leemos línea a línea
                while ((linea = lector.readLine()) != null) {
                    System.out.println(linea);
                }

                lector.close();
                //salto de linea jump
                System.out.println();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //---------------LEER CUADNO CP1252-----------
            try (FileInputStream leebinario2 = new FileInputStream(archivo);
                 InputStreamReader traductorerroneo = new InputStreamReader(leebinario2, "Cp1252");
                 BufferedReader lectorCp1252 = new BufferedReader(traductorerroneo)){
                //volvermos a ahcerlo lo mismo que arriba

                // Declaramos una codificación diferente a la que usamos para escribir



                String lineaMala;
                while ((lineaMala = lectorCp1252.readLine()) != null) {
                    System.out.println(lineaMala);
                }
                //Las tildes y la 'ñ' se muestran mal, porque Cp1252 interpreta los bytes de UTF-8 de manera erronea

                lectorCp1252.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //-----------CUENTA LINEA PALABRAS Y VOCALES--------------
            try(FileInputStream leebinario3 = new FileInputStream(archivo);
                InputStreamReader traduce3 = new InputStreamReader(leebinario3, "UTF-8");
                BufferedReader lectorContador = new BufferedReader(traduce3);){

                //volevemos hacer lo de arriba


                int contadorLineas = 0;
                int contadorlPalabras = 0;
                int contadorVocales = 0;
                String lineamomento;

                //usamos esta variable para comparar las vocales
                String vocalesValidas = "aeiouáéíóúAEIOUÁÉÍÓÚ";

                while ((lineamomento = lectorContador.readLine()) != null) {
                    contadorLineas++;

                    // Dividimos la línea por los espacios para contar palabras
                    String[] palabras = lineamomento.split(" ");
                    contadorlPalabras += palabras.length;

                    // Recorremos la línea letra por letra para buscar vocales
                    for (int i = 0; i < lineamomento.length(); i++) {
                        char letra = lineamomento.charAt(i);
                        // Si la letra actual está dentro de nuestro string de vocales, sumamos 1
                        if (vocalesValidas.indexOf(letra) != -1) {
                            contadorVocales++;
                        }
                    }
                }
                //cerramos commo siempre
                lectorContador.close();

                System.out.println("Líneas totales: " + contadorLineas);
                System.out.println("Palabras totales: " + contadorlPalabras);
                System.out.println("Vocales totales: " + contadorVocales);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //--------------------teoria------------------
            /*
                Los ordenadores solo entienden números (bytes), no letras. La "codificación" es el diccionario que traduce un símbolo (como la 'á' o la 'ñ') a un byte específico.
                Usamos getBytes("UTF-8") al escribir para asegurarnos de que nuestras letras se traducen a bytes usando el estándar internacional UTF-8, evitando que Java use la codificación por defecto del sistema operativo (que podría ser distinta en Windows, Linux o Mac).
                Al leer, debemos declarar exactamente la misma codificación (UTF-8) en el InputStreamReader. Si no lo hacemos y usamos otra distinta (como Cp1252), el lector buscará los bytes en el "diccionario" equivocado, traduciéndolos mal y mostrando símbolos raros en lugar de las tildes y las eñes (lo que se conoce como mojibake).
             */

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



}
