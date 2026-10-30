package be.vdab.dance.festivals;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;

    public MyRunner(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @Override
    public void run(String... args) {
        var scanner = new Scanner(System.in);
        System.out.print("Geef het id van het te annuleren festival: ");
        var id = scanner.nextLong();
        festivalService.annuleer(id);
    }
}
