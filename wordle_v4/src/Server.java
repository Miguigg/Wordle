import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server implements Hello {
   public static boolean tiempo = false;
   private static String temp;// variable para ir descartando letras
   public static String direccion; // aqui guardamos la direccion del servidor generada con el nombre dado y el
                                   // puerto 4000
   private static String[] toret = { "", "", "", "", "" };// aqui generamos el resultado
   private static String secreto;// palabra a adivinar
   private static ArrayList<String> palabras = lector.getInputAsArrayList("palabras.txt");

   public static void main(String[] args) {
      try {
         String input = "";
         getpalabra();// inicializamos la palabra
         try{
            input = args[0];
         }catch( ArrayIndexOutOfBoundsException e ){
            System.out.println("Debes introducir un nomrbe para el servidor");
            System.exit(0);
         }
         direccion = "//" + input + ":" + 4000 + "/";
         System.out.println("El servidor esta montado en la direccion: " + " " + direccion);

         Hello hello = new Server();

         Hello stub = (Hello) UnicastRemoteObject.exportObject(hello, 4000); // Hace disponible 'hello' en el puerto
                                                                             // 4000

         Registry registry = LocateRegistry.createRegistry(4000); // tablon de anuncios
         registry.rebind(direccion, stub); // mete al servidor en el tablo para poder acceder a su metodo

         System.out.println("[+] Server vinculado");
         System.out.println(registry.lookup(direccion));

         // cremos el hilo cronometro
         Cronometro c = new Cronometro();
         Thread thr = new Thread(c);
         thr.start();// comenzamos a contar

      } catch (RemoteException | NotBoundException e) {
         System.err.println("Server exception:");
         e.printStackTrace();
      }

   }

   public String intento(String s, int vidas) throws RemoteException {
      String[] rst = { "", "", "", "", "" };

      if (vidas == 0) {// si le llega que el cliente ya no tiene vidas, resetea
         String loose = "Nueva palabra disponible y la anterior era:" + " " + secreto;
         getpalabra();// metemos nueva palabra
         return loose;
      }

      s = s.toUpperCase();
      if (aciertoDirecto(s)) {
         getpalabra();// metemos nueva palabra
         return "ganas";

      } else if (tiempo) {// depende del hilo
         tiempo = false;
         getpalabra();// metemos nueva palabra por fin de tiempo
         return "tiempo";

      } else {
         aciertoParcial(s);
         letraContenida(s);
         String resultado = String.join("", toret);// guardamos el valor actual del resultado
         toret = rst;// reseteamos el valor de toret para la proxima jugada
         System.out.println(resultado);
         return resultado;
      }
   }

   // metodos de comprobacion de la palabra
   public static boolean aciertoDirecto(String intento) {
      if (intento.equals(secreto)) {
         return true;
      } else {
         return false;
      }
   }

   // miramos si la alguna letra esta bien colocada
   public static void aciertoParcial(String intento) {
      temp = secreto;
      for (int j = 0; j < 5; j++) {// recorremos toda la palabra en busca de letras bien colocadas
         String s = secreto.substring(j, j + 1);
         String a = intento.substring(j, j + 1);
         if (s.equals(a)) {
            toret[j] = "*";// si acierta la posicion de la letra, le asignamos *
            temp = temp.substring(0, j) + "_" + temp.substring(j + 1);// eliminamos la letra que ya hemos cogido de la
                                                                      // variable temporal para no volver a evaluarla
         }
      }
   }

   // miramos si la letra esta en alguna posicion de la palabra
   public static void letraContenida(String intento) {
      System.out.println(temp);
      System.out.println(intento);
      for (int i = 0; i < 5; i++) {
         String a = intento.substring(i, i + 1);
         while (temp.contains(a)) {// si la palabra secreta tiene la letra
            if (!toret[i].equals("")) {// y la posicion concreta de la salida no tiene ya la letra indicada
               break;
            }
            toret[i] = "~";// asignamos esto para indicar que posicion incorrecta
            temp = temp.substring(0, i) + "_" + temp.substring(i + 1);// de nuevo, eliminamos para no evaluar varias
                                                                      // veces
         }
         if (toret[i].equals("")) {
            toret[i] = "_";// en caso de letra equivocada porque no este presente
         }
      }
   }

   // cogemos una nueva palabra del array y eliminamos
   public static void getpalabra() {
      int numPalabras = palabras.size();
      int num = (int) Math.floor(Math.random() * numPalabras);
      secreto = palabras.get(num).toUpperCase();// palabra actual
      palabras.remove(num);// eliminamos la palabra para no volverla a coger
   }
}