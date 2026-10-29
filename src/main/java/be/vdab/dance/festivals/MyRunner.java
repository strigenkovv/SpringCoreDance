package be.vdab.dance.festivals;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;

    public MyRunner(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @Override
    public void run(String... args) {
        festivalService.findUitverkocht()
                .forEach(festival -> System.out.println(festival.getNaam()));
    }
}
