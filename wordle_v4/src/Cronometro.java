import java.util.concurrent.TimeUnit;

public class Cronometro implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Inicio la temporizacion");
                Thread.currentThread();
                TimeUnit.MINUTES.sleep(1);// a los 5 minutos
                Server.tiempo = true;
            } catch (InterruptedException e) {
                System.out.println("Error");
            }
        }
    }

}