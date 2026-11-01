package be.vdab.dance.festivals;

public record AantalBoekingenPerFestival(
        long festivalId,
        String naam,
        long aantalBoekingen) {
}
