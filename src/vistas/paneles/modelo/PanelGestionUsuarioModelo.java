package vistas.paneles.modelo;
import otros.Usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PanelGestionUsuarioModelo {

    private static final String RUTA_USUARIOS = "datos/FichaUsuario.txt";
    private static final String RUTA_PERMISOS = "datos/usuarios.txt";

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        // 1. Cargamos los permisos de usuarios.txt en un Map
        Map<String, String> mapaPermisos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_PERMISOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 3) {
                    mapaPermisos.put(partes[1], partes[2]); // correo -> rol
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Leemos el fichero FichaUsuario.txt
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_USUARIOS))) {
            String linea;
            String correo = "", dni = "", codigoPostal = "", fechaNacimiento = "", telefono = "";

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Correo: ")) {
                    correo = linea.replace("Correo: ", "").trim();
                } else if (linea.startsWith("DNI: ")) {
                    dni = linea.replace("DNI: ", "").trim();
                } else if (linea.startsWith("Código Postal: ")) {
                    codigoPostal = linea.replace("Código Postal: ", "").trim();
                } else if (linea.startsWith("Fecha de Nacimiento: ")) {
                    fechaNacimiento = linea.replace("Fecha de Nacimiento: ", "").trim();
                } else if (linea.startsWith("Teléfono: ")) {
                    telefono = linea.replace("Teléfono: ", "").trim();
                } else if (linea.startsWith("-------------------------")) {
                    // 3. Asignamos el permiso si existe, si no, lo marcamos como "usuario"
                    String permisos = mapaPermisos.getOrDefault(correo, "usuario");
                    usuarios.add(new Usuario(correo, dni, codigoPostal, fechaNacimiento, telefono, permisos));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public void eliminarUsuario(String correo) {
        try {
            // 1. Eliminar de FichaUsuario.txt
            List<String> ficha = Files.readAllLines(Paths.get("datos/FichaUsuario.txt"));
            List<String> nuevaFicha = new ArrayList<>();
            boolean eliminar = false;
            for (String linea : ficha) {
                if (linea.startsWith("Correo: ") && linea.contains(correo)) {
                    eliminar = true;
                    continue;
                }
                if (eliminar) {
                    if (linea.startsWith("-------------------------")) {
                        eliminar = false;
                    }
                    continue;
                }
                nuevaFicha.add(linea);
            }
            Files.write(Paths.get("datos/FichaUsuario.txt"), nuevaFicha);//Correcto

            // 2. Eliminar de usuarios.txt
            eliminarLineaExacta(correo, "datos/usuarios.txt");//Correcto

            // 3. Eliminar de favoritos.txt
            eliminarLineaQueEmpiezaCon(correo, "datos/Favoritos.txt");//Correcto

            // 4. Eliminar de biografias.txt
            eliminarLineaQueEmpiezaCon(correo, "datos/biografias.txt");//Correcto

            // 5. Eliminar de solicitudes.txt
            eliminarSolicitudesDelUsuario(correo, "datos/solicitudes.txt");

            // 6. Eliminar de solicitudesAceptadas.txt
            eliminarSolicitudesDelUsuario(correo, "datos/solicitudesRevisadas.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Elimina una línea completa que contiene exactamente el correo
    private void eliminarLineaExacta(String correo, String ruta) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(ruta));
        List<String> nuevas = lineas.stream()
                .filter(line -> !line.split(":")[0].equals(correo))
                .toList();
        Files.write(Paths.get(ruta), nuevas);
    }

    // Elimina línea que empieza por el correo
    private void eliminarLineaQueEmpiezaCon(String correo, String ruta) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(ruta));
        List<String> nuevas = lineas.stream()
                .filter(line -> !line.startsWith(correo + ":"))
                .toList();
        Files.write(Paths.get(ruta), nuevas);
    }

    // Elimina todas las líneas que contengan el correo (caso solicitudes)
    private void eliminarSolicitudesDelUsuario(String correo, String ruta) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(ruta));
        List<String> nuevas = new ArrayList<>();

        for (String linea : lineas) {
            // Solo eliminamos si la línea contiene específicamente "Usuario: <correo>" como parte de una solicitud
            if (!linea.contains("Usuario: " + correo + "<br>")) {
                nuevas.add(linea);
            }
        }

        Files.write(Paths.get(ruta), nuevas);
    }

    public void cambiarPermisos(String correo) {
        try {
            Path path = Paths.get("datos/usuarios.txt");
            List<String> lineas = Files.readAllLines(path);
            List<String> nuevas = new ArrayList<>();

            for (String linea : lineas) {
                if (linea.startsWith(correo + ":")) {
                    String[] partes = linea.split(":");
                    if (partes.length == 3) {
                        String nuevoRol = partes[2].equals("admin") ? "usuario" : "admin";
                        nuevas.add(partes[0] + ":" + partes[1] + ":" + nuevoRol);
                    } else {
                        nuevas.add(linea);
                    }
                } else {
                    nuevas.add(linea);
                }
            }

            Files.write(path, nuevas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
