package vistas.paneles.modelo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PanelAnimalAdminModelo {

    public int contarSolicitudes(String idAnimal) {
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("datos/solicitudes.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.contains(idAnimal)) {
                    contador++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }

    public boolean actualizarAnimal(String idAnimal, String tipo, String raza, String nombre, String edad) {
        try (BufferedReader br = new BufferedReader(new FileReader("datos/animales.txt"))) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("datos/animales_temp.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes[0].equals(idAnimal)) {
                    linea = idAnimal + ":" + tipo + ":" + raza + ":" + nombre + ":" + edad;
                }
                bw.write(linea);
                bw.newLine();
            }
            bw.close();
            Files.move(new File("datos/animales_temp.txt").toPath(), new File("datos/animales.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAnimal(String idAnimal) {
        try (BufferedReader br = new BufferedReader(new FileReader("datos/animales.txt"))) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("datos/animales_temp.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (!partes[0].equals(idAnimal)) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
            bw.close();
            Files.move(new File("datos/animales_temp.txt").toPath(), new File("datos/animales.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarImagen(String idAnimal, File nuevaImagen) {
        try {
            File destino = new File("src/resources/animales/" + idAnimal + "_image.png");
            Files.copy(nuevaImagen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
