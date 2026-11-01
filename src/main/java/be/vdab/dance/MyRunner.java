package be.vdab.dance;

import be.vdab.dance.festivals.Boeking;
import be.vdab.dance.festivals.BoekingService;
import be.vdab.dance.festivals.FestivalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.math.BigDecimal;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;
    private final BoekingService boekingService;

    public MyRunner(FestivalService festivalService, BoekingService boekingService) {
        this.festivalService = festivalService;
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

        maakBoeking(scanner);
        System.out.print("Boekingsoverzicht tonen? (j/n): ");
        if (scanner.nextLine().equalsIgnoreCase("j")) {
            toonBoekingOverzicht();
        }

        annuleerBoeking(scanner);
        toonAantalBoekingenPerFestival();
        toonFestivalsMetReclameBudget(scanner);
    }

    private void maakBoeking(Scanner scanner) {
        System.out.print("Geef je naam: ");
        var naam = scanner.nextLine();
        System.out.print("Geef het aantal tickets: ");
        var aantalTickets = scanner.nextInt();
        System.out.print("Geef het id van het festival: ");
        var festivalId = scanner.nextLong();
        scanner.nextLine();

        try {
            boekingService.create(new Boeking(0, naam, aantalTickets, festivalId));
            System.out.println("Boeking aangemaakt.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void toonBoekingOverzicht() {
        System.out.println("Overzicht van de boekingen:");
        boekingService.findBoekingOverzicht()
                .forEach(boeking -> System.out.println(
                        boeking.boekingId() + ": "
                                + boeking.naam() + ": "
                                + boeking.festivalNaam() + ": "
                                + boeking.aantalTickets()));
    }

    private void annuleerBoeking(Scanner scanner) {
        System.out.print("Geef het id van de te annuleren boeking: ");
        var boekingId = scanner.nextLong();
        boekingService.annuleerBoeking(boekingId);
        System.out.println("Boeking geannuleerd.");
    }

    private void toonAantalBoekingenPerFestival() {
        System.out.println("Aantal boekingen per festival:");
        boekingService.findAantalBoekingenPerFestival()
                .forEach(festival -> System.out.println(
                        festival.festivalId() + ": "
                                + festival.naam() + ": "
                                + festival.aantalBoekingen()));
    }

    private void toonFestivalsMetReclameBudget(Scanner scanner) {
        System.out.print("Geef een minimum reclamebudget: ");
        var vanaf = scanner.nextBigDecimal();
        festivalService.findMetReclameBudgetVanaf(vanaf)
                .forEach(festival -> System.out.println(
                        festival.naam() + ": " + festival.reclameBudget() + "€"));
    }
}
