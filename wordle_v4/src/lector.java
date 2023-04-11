import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class lector {
    public static ArrayList<String> getInputAsArrayList(String fileName) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);// lector de las lineas del archivo
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!output.contains(data)) {// aseguramos que cada palabra este solo una vez
                    output.add(data.toUpperCase());// leer cada linea y la guarda en el array como un elemento
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer");
            e.printStackTrace();
        }
        return output;
    }

    public static int getNumElm(String fileName) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);// lector de las lineas del archivo
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!output.contains(data)) {// aseguramos que cada palabra este solo una vez
                    output.add(data);// leer cada linea y la guarda en el array como un elemento
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer");
            e.printStackTrace();
        }
        return output.size();
    }
}