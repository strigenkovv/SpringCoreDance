package be.vdab.dance.festivals;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    // private final FestivalService festivalService;
    private final BoekingService boekingService;

    public MyRunner(BoekingService boekingService) {
        // this.festivalService = festivalService;
        this.boekingService = boekingService;
    }

    @Override
    public void run(String... args) {
        var scanner = new Scanner(System.in);
        /*
        System.out.print("Geef het id van het te annuleren festival: ");
        var id = scanner.nextLong();
        festivalService.annuleer(id);
        */

        System.out.print("Geef je naam: ");
        var naam = scanner.nextLine();
        System.out.print("Geef het aantal tickets: ");
        var aantalTickets = scanner.nextInt();
        System.out.print("Geef het id van het festival: ");
        var festivalId = scanner.nextLong();

        try {
            boekingService.boek(new Boeking(0, naam, aantalTickets, festivalId));
            System.out.println("Boeking aangemaakt.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
