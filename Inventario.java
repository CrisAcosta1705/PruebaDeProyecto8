package Proyecto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Inventario {
    private List<Producto> productos;
    private List<Venta> ventas;
    private static String empresa; 

    public Inventario() {
        productos = new ArrayList<>();
        ventas = new ArrayList<>(); 
    }

    public static void setEmpresa(String empresa) {
        Inventario.empresa = empresa;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void agregarProductoElectronico(ProductoElectronico productoElectronico) {
        productos.add(productoElectronico);
    }

    public void agregarProductoAlimenticio(ProductoAlimenticio productoAlimenticio) {
        productos.add(productoAlimenticio);
    }

    public void agregarVenta(Venta venta) {
        ventas.add(venta);
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        boolean salir = false;

        do {
            System.out.println("==== MENÚ DEL INVENTARIO ====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar inventario");
            System.out.println("3. Informe del inventario");
            System.out.println("4. Realizar venta");
            System.out.println("5. Eliminar producto del inventario");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        mostrarInventario();
                        break;
                    case 3:
                        generarInformeInventario(ventas);
                        break;
                    case 4:
                        realizarVenta();
                        break;
                    case 5:
                        eliminarProducto();
                        break;
                    case 6:
                        System.out.println("Saliendo del programa...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, seleccione nuevamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número válido. Inténtelo nuevamente.");
                scanner.nextLine();
                opcion = 0;
            }
        } while (!salir);

        Login.mostrarLogin(null);
    }

    public void registrarProducto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese las unidades del producto: ");
        int unidades = scanner.nextInt();
        scanner.nextLine();

        System.out.println("1. Producto electrónico");
        System.out.println("2. Producto alimenticio");
        System.out.print("Seleccione el tipo de producto: ");
        int tipoProducto = scanner.nextInt();
        scanner.nextLine();

        Producto producto;

        switch (tipoProducto) {
            case 1:
                System.out.print("Ingrese el modelo del producto electrónico: ");
                String modelo = scanner.nextLine();
                producto = new ProductoElectronico(nombre, precio, unidades, modelo);
                break;
            case 2:
                System.out.print("Ingrese la fecha de caducidad del producto alimenticio (dd/MM/yyyy): ");
                String fechaCaducidadStr = scanner.nextLine();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaCaducidad = dateFormat.parse(fechaCaducidadStr);
                    producto = new ProductoAlimenticio(nombre, precio, unidades, fechaCaducidad);
                } catch (ParseException e) {
                    System.out.println("Formato de fecha inválido. No se pudo registrar el producto alimenticio.");
                    return;
                }
                break;
            default:
                System.out.println("Opción inválida. No se pudo registrar el producto.");
                return;
        }

        agregarProducto(producto);
        System.out.println("Producto registrado exitosamente.");
    }

    public void mostrarInventario() {
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            System.out.println("==== INVENTARIO ====");
            System.out.println("Nombre\t\tPrecio\t\tUnidades\t\tTipo\t\tModelo/Fecha Caducidad");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Producto producto : productos) {
                String tipoProducto = producto instanceof ProductoElectronico ? "Electrónico" : "Alimenticio";
                String modeloFechaCaducidad;

                if (producto instanceof ProductoElectronico) {
                    modeloFechaCaducidad = ((ProductoElectronico) producto).getModelo();
                } else {
                    Date fechaCaducidad = ((ProductoAlimenticio) producto).getFechaCaducidad();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    modeloFechaCaducidad = sdf.format(fechaCaducidad);
                }

                System.out.printf("%-17s%-17s%-18s%-24s%-24s\n",
                        producto.getNombre(), producto.getPrecio(), producto.getUnidades(), tipoProducto, modeloFechaCaducidad);
            }
            System.out.println("------------------------------------------------------------------------------------------------");
        }
    }

    public void generarInformeInventario(List<Venta> ventas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("informe_inventario.txt"))) {
            bw.write("==== INFORME DEL INVENTARIO ====");
            bw.newLine();
            bw.write("Empresa: " + empresa);
            bw.newLine();
            bw.write("Fecha de generación del informe: " + LocalDate.now().format(formatter));
            bw.newLine();
            bw.newLine();

            bw.write("==== HISTORIAL DE VENTAS ====");
            bw.newLine();

            if (ventas.isEmpty()) {
                bw.write("No hay ventas registradas.");
            } else {
                for (int i = 0; i < ventas.size(); i++) {
                    Venta venta = ventas.get(i);
                    int numVenta = i + 1;

                    Producto productoEncontrado = null;
                    for (Producto producto : productos) {
                        if (producto.getNombre().equalsIgnoreCase(venta.getNombre())) {
                            productoEncontrado = producto;
                            break;
                        }
                    }

                    bw.write("Venta #" + numVenta);
                    bw.newLine();
                    bw.write("Fecha de venta: " + venta.getFechaVenta().format(formatter));
                    bw.newLine();
                    bw.write("Nombre del producto: " + venta.getNombre());
                    bw.newLine();
                    bw.write("Precio unitario de producto: " + productoEncontrado.getPrecio());
                    bw.newLine();
                    bw.write("Unidades vendidas: " + venta.getUnidades());
                    bw.newLine();
                    bw.write("Total de la venta: " + venta.getGananciasVenta());
                    bw.newLine();
                    bw.write("Existencias después de la venta: " + productoEncontrado.getUnidades());
                    bw.newLine();
                    bw.newLine();
                }
            }

            System.out.println("Informe del inventario generado exitosamente.");

        } catch (IOException e) {
            System.out.println("Error al generar el informe del inventario.");
        }
    }

    public void realizarVenta() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== REALIZAR VENTA ====");

        if (productos.isEmpty()) {
            System.out.println("No hay productos en el inventario para realizar la venta.");
            return;
        }

        mostrarInventario();

        System.out.print("Ingrese la cantidad de diferentes productos a vender: ");
        int cantidadProductos = scanner.nextInt();
        scanner.nextLine();

        if (cantidadProductos <= 0) {
            System.out.println("Cantidad inválida. La venta no se puede realizar.");
            return;
        }

        for (int i = 0; i < cantidadProductos; i++) {
            System.out.println("Producto #" + (i + 1));
            System.out.print("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();

            Producto productoEncontrado = null;
            for (Producto producto : productos) {
                if (producto.getNombre().equalsIgnoreCase(nombre)) {
                    productoEncontrado = producto;
                    break;
                }
            }

            if (productoEncontrado == null) {
                System.out.println("Producto no encontrado en el inventario.");
                continue;
            }

            System.out.print("Ingrese la cantidad a vender: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            if (cantidad > 0 && cantidad <= productoEncontrado.getUnidades()) {
                productoEncontrado.setUnidades(productoEncontrado.getUnidades() - cantidad);
                System.out.println("Venta realizada exitosamente.");

                LocalDate fechaVenta = LocalDate.now();

                double gananciasVenta = cantidad * productoEncontrado.getPrecio();
                Venta venta = new Venta(nombre, fechaVenta, cantidad, gananciasVenta, nombre);

                agregarVenta(venta); 
            } else {
                System.out.println("Cantidad inválida. La venta no se puede realizar.");
            }
        }

        if (!ventas.isEmpty()) {
        	
			generarFactura(ventas);
        }
    }

    public void generarFactura(List<Venta> ventas) {
        Scanner pantalla = new Scanner(System.in);
        System.out.println("Ingrese el nombre del cliente: ");
        String cliente = pantalla.nextLine();

        System.out.println("/////// Factura ///////");
        System.out.println("**********************");
        System.out.println("Empresa: " + empresa); 
        System.out.println("Cliente: " + cliente);

        double totalVenta = 0.0;

        for (int i = 0; i < ventas.size(); i++) {
            Venta venta = ventas.get(i);
            int numVenta = i + 1;
            totalVenta += venta.getGananciasVenta();

            Producto productoEncontrado = null;
            for (Producto producto : productos) {
                if (producto.getNombre().equalsIgnoreCase(venta.getNombre())) {
                    productoEncontrado = producto;
                    break;
                }
            }

            System.out.println("Venta #" + numVenta);
            System.out.println("Nombre del producto: " + venta.getNombre());
            System.out.println("Unidades: " + venta.getCantidad());
            System.out.println("Precio unitario: " + productoEncontrado.getPrecio());
            System.out.println("Precio total de producto: " + venta.getGananciasVenta());
            System.out.println("----------------------------------------");
        }

        System.out.println("Pago total: " + totalVenta);
    }


    public void eliminarProducto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del producto a eliminar: ");
        String nombre = scanner.nextLine();

        Producto productoEncontrado = null;

        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                productoEncontrado = producto;
                break;
            }
        }

        if (productoEncontrado != null) {
            productos.remove(productoEncontrado);
            System.out.println("Producto eliminado exitosamente.");
        } else {
            System.out.println("No se encontró un producto con ese nombre en el inventario.");
        }
    }
}