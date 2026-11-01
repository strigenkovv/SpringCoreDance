package be.vdab.dance.festivals;

public record BoekingOverzicht(
        long boekingId,
        String naam,
        String festivalNaam,
        int aantalTickets) {
}
