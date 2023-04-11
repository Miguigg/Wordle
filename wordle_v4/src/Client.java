import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        // declaracion de variables
        int numVidas = 6;
        boolean seguir = true;
        String resp;
        String palabra;
        Scanner myObj = new Scanner(System.in);
        ArrayList<String> arrayPalabras = lector.getInputAsArrayList("palabras.txt");


        String direccion = "";
        int port = 0;
         try{
             String input = args[0];
             port = Integer.parseInt(args[1]);
             direccion = "//" + input + ":" + port + "/";
         }catch(ArrayIndexOutOfBoundsException e){
             System.out.println("Debes introducir el servidor al que conectarte");
             System.exit(0);
         }


        System.out.println("Lista palabras:");
        for (int i = 0; i < arrayPalabras.size(); i++) {
            System.out.println(i + ". " + arrayPalabras.get(i));
        }

        try {
            System.out.println(
                    "Intrucciones:\n -Si en una letra aparece * es que la tienes en buena posicion\n" +
                            "-Si en una letra te aparece ~ es que esta presente en la palabra, pero no en esa posicion\n"
                            + "-Si aparece _ es que la letra no esta en la palabra\n" +
                            "-Justo encima puedes ver una lista de las palabras aceptadas\n" +
                            "-Tienes 6 vidas. Si pasan 5 minutos y no la adivinas o si ya la has adivinado, se resetea\n");

            System.out.println(direccion);
            Registry registry = LocateRegistry.getRegistry(port); // vamos al tablon
            Hello hello = (Hello) registry.lookup(direccion); // busca en el tablon el nombre del servidor

            do {
                if (numVidas == 0) {// si no tiene vidas, le mandamos al servidor que no hay y reseteamos el
                                    // contador para volver a empezar

                    String loose = hello.intento("", numVidas);
                    System.out.println("Has perdido, lo siento" + " " + "Y el server dice:" + " " + loose);
                    numVidas = 6;// vuelve a empezar
                }
                System.out.println("Dime una palabra de 5 letras");
                palabra = myObj.nextLine();

                if (palabra.length() == 5 && arrayPalabras.contains(palabra.toUpperCase())) {
                    String respServer = hello.intento(palabra, numVidas); // accedemos al metodo compartido

                    switch (respServer) {

                        case "ganas":
                            System.out.println("Gané!!! , Viva!!!");
                            System.out.println(
                                    "Quieres mas? s/n (ten en cuenta que hasta que pasen los 5 minutos no habra nueva palabra)");
                            resp = myObj.nextLine();
                            if (resp.toUpperCase().equals("N")) {
                                seguir = false;
                            } else {
                                System.out
                                        .println("Lo tomare como un si");
                            }
                            numVidas = 6;// reseteamos las vidas porque ya gano la partida
                            break;

                        case "tiempo":
                            System.out.println("Se acabaron los 5 minutos");
                            numVidas = 6;// reseteamos vidas porque pasaron los 5 minutos
                            break;

                        default:
                            System.out.println("Pues no acerte...");
                            numVidas--;
                            System.out
                                    .println("Te quedan" + " " + numVidas + " " + "y tu resultado es:" + " "
                                            + respServer);
                            System.out.println("Quieres mas? s/n");
                            resp = myObj.nextLine();
                            if (resp.toUpperCase().equals("N")) {
                                seguir = false;
                            } else {
                                System.out
                                        .println("Lo tomare como un si");
                            }
                            break;

                    }

                }
            } while (seguir);
            myObj.close();
        } catch (Exception e) {
            System.err.println("Servidor no encontrado, asegurate de que la direccion y el puerto son correctos");
        }
    }

}
