import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearOrganizarInspeccionar {

    //Creamos la estancia de las carpetas de forma general
    public final static File base = new File ("datos/ud1");

    public static void main(String[] args){

        //tenemos que acostumbrarnos a poner siempre el get Property
        System.out.println("Ejecutado desde" + System.getProperty("user.dir"));

        //------------- MKDIR DE FORMA ERRONEA ------------

        //hay que acostumbrarse a hacerlo con boolean porque está mejor guardas las variables
        //no va a crear nada porque mkdir sin s no puede crear mas de una carpeta a la vez
        boolean fallomkdir = base.mkdir();

        //comprobamos que con un if que no se han ejecutado y que nos muestre la ruta en lugar que si
        if(!fallomkdir){
            System.out.println("No se han podido crear todas las carperas. Fallo a proposito");

        }else {
            System.out.println("Se han creado en " + base.getPath());
        }

        //----------- MKDIRS DE FORMA CORRECTA -----------------

        //lo volvemos a guardar en una variable, y como devuelve true o false lo guardamos en boolean
        boolean basecreada = base.mkdirs();

        //comprobamos con un if y vamos si se ha creado o ya estaba creada
        //en la primera pasada dirá que la ha creado y la ruta y la segunda pasada
        //dirá que ya existian y donde
        if(basecreada){
            System.out.println("Las carpetas se han creado en: " + base.getAbsolutePath());
        }else{
            System.out.println("Las carpetas ya existian en: " + base.getAbsolutePath());
        }

        //--------------- CREAMOS CARTA.TXT CON CREATENEWFILE()------------------
        //Cremos la estancia del fichero carta.txt
        File texto = new File(base, "carta.txt");

        //lo creamos fuera de su try para poder usarlo File textonuevo = new File(base, "cartanueva.txt");
        File textonuevo = new File(base, "cartanueva.txt");
        //Para crearlo lo tenemos que meter en un try, y despues dentro de otro mas
        try{
            //creamos dentro tantos try como metodos usemos
            try{
                //lo volvemos a guardar en una variable
                boolean textocreado = texto.createNewFile();
                //usamos If para comprobar si se ha creado
                if(textocreado){
                    System.out.println("El texto carta se ha creado en :" + texto.getAbsolutePath());
                }else {
                    System.out.println("El texto carta ya existia en: " + texto.getAbsolutePath());
                }
                //al igual que en el otro, al principio dará un que se ha creado y la ruta
                //y la segunda pasada dira que ya existia y la ruta
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //--------------ESCRIBIMOS DOS LINEAS EN EL FICHERO-------------------
            //metodo nuevo, try nuevo
            try {
                //lo primero que haces es crear una variable de tipo FileWriter, que nos ayudara a
                //escribir en ella, creamos la estancia para despues usarla
                FileWriter maquinaescribir = new FileWriter(texto);
                //para escribir en ella solo tenemos que hacerlo de la siguiente forma:
                //(importante poner los \n en mi opinion para que se escriba debajo)
                //TENER CUIDADO porque FileWriter por defecto VACIA el fichero
                maquinaescribir.write("Primera prueba primera linea \n");
                maquinaescribir.write("Segunda linea, espero que salga bien \n");
                maquinaescribir.close();
                //si o si hay que cerrar el FileWriter cuando se usa, exactamente no se porque pero si no lo cerraba
                //no se me escribia en el fichero
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //----------------------------RENOMBRAR----------------------
            //otro metodo otro try
            try {
                //instancioamos el nuevo nombre la creamos arriba para poder usarla despues
                //guardamos en variable booleana como antes
                boolean renombre = texto.renameTo(textonuevo);

                //comprobamos con if
                if(renombre){
                    System.out.println("Se ha cambiado con exito");
                }else{
                    System.out.println("No se ha cambiado el nombre y no lanza excepcion");
                }
                //IMPORTANTE, TENER EL FICHERO CERRADO O SINO NO SE CAMBIA EL NOMBRE
                //Hay que tener en cuenta que la primera vez se crea carta.txt, y luego le cambia el nombre a esa
                //pero la segunda como vuelve a pasar por la creacion de carta.txt, como esa ya no existe, vuelve a crear
                //un nuevo archivo primero con ese nombre y no cambia el nombre
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //---------------DELETE()-------------------
            try {
                //instanciamos un nuevo fichero para despues borrarlo
                File textoborrar = new File(base, "borrar.txt");
                //creamos el nuevo fichero, esta vez no vamos a comprobar por if si se ha creado o
                //si no que vamos a tener abierta y verlo directamente
                textoborrar.createNewFile();
                //ahora creamos una bariable para borrarlo en boolean para depues comprobarlo con if
                //usamos .delete() para borrar el archivo
                boolean borrar = textoborrar.delete();
                //comprobamos con if
                if(borrar){
                  System.out.println("Se ha borrado con exito el nuevo archivo");
                }else{
                    System.out.println("No se ha podido borrar el nuevo archivo");
                }
                //para verlo bien sino queremos poner el comprobador hay que hacer debugger para ver el paso por paso
                //como lo crea y lo borra
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //---------------MOSTRAMOS DE LAS SIGUIENTES FORMAS -----------------
            try{
                System.out.println("Nombre de nuestro fichero: " + textonuevo.getName());
                System.out.println("La ruta absoluta es: " + textonuevo.getAbsolutePath());
                System.out.println("Y su tamaño es de: " + textonuevo.length() + " byte");

                //---------FORMATEAMOS-----------
                //convertimos en dato la ultoma vez que se modifico usando la librería Date y el metodo lastModificate()
                Date primerafecha = new Date(textonuevo.lastModified());
                //usamos SimpleDateFormar para formatearlo
                SimpleDateFormat formateo = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                //para poder imprimirlo, usamos un string para verlo mucho mejor, guardamos en una variabl y luego imprimimos
                String modificado = formateo.format(primerafecha);
                //imprimimos
                System.out.println("la ultima modificacion fue: " + modificado);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //----------------RECUENTO DE TODO LO QUE TENGO-----------------
            try {
                //para ello vamos a necesitar meter todos los archivos
                //usamos array [] y listFiles()
                File[] cuentas = base.listFiles();
                int contador = 0;

                //y con if lo contamos vamos recorriendo
                if(cuentas != null){
                    for(File cuenta : cuentas){
                        String tipodato = cuenta.isDirectory() ? "Carpeta" : "Fichero";
                        long bytes = cuenta.length();
                        double kb = bytes / 1024.0;
                        System.out.println(cuenta.getName() + " " + tipodato + " " + bytes + " " + kb);
                        contador++;
                    }
                }
                System.out.println("Total de entradas en la carpeta: " + contador);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
