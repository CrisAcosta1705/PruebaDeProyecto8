package Proyecto;

import java.util.*;

public class Login {

    public static void mostrarLogin(String[] args) {

        Map<String, String> cuentas = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

        boolean inicioSesionExitoso = false;

        while (!inicioSesionExitoso) {
            System.out.println("CREACIÓN DE CUENTA");
            System.out.print("Ingrese su nombre de usuario: ");
            String usuario = scanner.nextLine();

            System.out.print("Ingrese el nombre de la empresa: ");
            String empresa = scanner.nextLine();

            System.out.print("Ingrese su contraseña: ");
            String contraseña = scanner.nextLine();

            cuentas.put(usuario, contraseña);

            System.out.println("\nINICIO DE SESIÓN");
            System.out.print("Nombre de usuario: ");
            String usuarioLogin = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contraseñaLogin = scanner.nextLine();

            if (cuentas.containsKey(usuarioLogin) && cuentas.get(usuarioLogin).equals(contraseñaLogin)) {
                System.out.println("Inicio de sesión exitoso.");
                inicioSesionExitoso = true;
                Inventario.setEmpresa(empresa); 
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
            }
        }
    }
}