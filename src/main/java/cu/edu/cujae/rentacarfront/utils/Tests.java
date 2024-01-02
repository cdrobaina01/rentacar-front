package cu.edu.cujae.rentacarfront.utils;

import cu.edu.cujae.rentacarfront.dto.TouristDTO;

public class Tests {
    public static void printTourist(TouristDTO tourist){
        System.out.println("Nombre: " + tourist.getName());
        System.out.println("Pasaporte: " + tourist.getPassport());
        System.out.println("Edad: " + tourist.getAge());
        System.out.println("Pais: " + tourist.getCountry());
        System.out.println("Genero: " + tourist.getGender());
        System.out.println("Email: " + tourist.getEmail());
        System.out.println("Telefono: " + tourist.getPhone());
    }
}
