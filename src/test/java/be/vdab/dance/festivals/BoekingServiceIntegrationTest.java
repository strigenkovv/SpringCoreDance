package be.vdab.dance.festivals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

@JdbcTest
@Import({FestivalRepository.class, BoekingRepository.class, BoekingService.class})
@Sql("/festivals.sql")
class BoekingServiceIntegrationTest {
    private static final String BOEKINGEN_TABLE = "boekingen";

    private final BoekingService boekingService;
    private final JdbcClient jdbcClient;

    BoekingServiceIntegrationTest(BoekingService boekingService, JdbcClient jdbcClient) {
        this.boekingService = boekingService;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void createMaaktEenBoekingAan() {
        var festivalId = idVanFestival("Rock Werchter");
        var boeking = new Boeking(0, "Test Boeking", 3, festivalId);

        var boekingId = boekingService.create(boeking);

        assertThat(boekingId).isPositive();
        assertThat(ticketsVanFestival(festivalId)).isEqualTo(97);
        assertThat(countRowsInTableWhere(
                jdbcClient, BOEKINGEN_TABLE, "id=" + boekingId)).isOne();
    }

    @Test
    void createMetTeVeelTicketsGeeftEenFout() {
        var festivalId = idVanFestival("Rock Werchter");
        var boeking = new Boeking(0, "Test Mislukt", 101, festivalId);

        assertThatThrownBy(() -> boekingService.create(boeking))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(ticketsVanFestival(festivalId)).isEqualTo(100);
        assertThat(countRowsInTableWhere(
                jdbcClient, BOEKINGEN_TABLE, "naam='Test Mislukt'")).isZero();
    }

    @Test
    void findBoekingOverzichtGeeftDeGegevensVanEenBoeking() {
        var festivalId = idVanFestival("Rock Werchter");
        boekingService.create(new Boeking(0, "Test Overzicht", 2, festivalId));

        assertThat(boekingService.findBoekingOverzicht())
                .anySatisfy(boeking -> {
                    assertThat(boeking.naam()).isEqualTo("Test Overzicht");
                    assertThat(boeking.festivalNaam()).isEqualTo("Rock Werchter");
                    assertThat(boeking.aantalTickets()).isEqualTo(2);
                });
    }

    @Test
    void annuleerBoekingVerwijdertBoekingEnGeeftTicketsTerug() {
        var festivalId = idVanFestival("Rock Werchter");
        var boekingId = boekingService.create(
                new Boeking(0, "Test Annulering", 3, festivalId));

        boekingService.annuleerBoeking(boekingId);

        assertThat(ticketsVanFestival(festivalId)).isEqualTo(100);
        assertThat(countRowsInTableWhere(
                jdbcClient, BOEKINGEN_TABLE, "id=" + boekingId)).isZero();
    }

    @Test
    void findAantalBoekingenPerFestivalGeeftDeJuisteAantallen() {
        var rockWerchterId = idVanFestival("Rock Werchter");
        var pukkelpopId = idVanFestival("Pukkelpop");
        boekingService.create(new Boeking(0, "Statistiek 1", 1, rockWerchterId));
        boekingService.create(new Boeking(0, "Statistiek 2", 1, rockWerchterId));
        boekingService.create(new Boeking(0, "Statistiek 3", 1, pukkelpopId));

        var statistiek = boekingService.findAantalBoekingenPerFestival();

        assertThat(statistiek)
                .extracting(AantalBoekingenPerFestival::festivalId)
                .isSorted();
        assertThat(statistiek).anySatisfy(eenFestival -> {
            assertThat(eenFestival.festivalId()).isEqualTo(rockWerchterId);
            assertThat(eenFestival.naam()).isEqualTo("Rock Werchter");
            assertThat(eenFestival.aantalBoekingen()).isEqualTo(2);
        });
        assertThat(statistiek).anySatisfy(eenFestival -> {
            assertThat(eenFestival.festivalId()).isEqualTo(pukkelpopId);
            assertThat(eenFestival.naam()).isEqualTo("Pukkelpop");
            assertThat(eenFestival.aantalBoekingen()).isEqualTo(1);
        });
    }

    private long idVanFestival(String naam) {
        return jdbcClient.sql("select id from festivals where naam = ?")
                .param(naam)
                .query(Long.class)
                .single();
    }

    private int ticketsVanFestival(long festivalId) {
        return jdbcClient.sql("select ticketsBeschikbaar from festivals where id = ?")
                .param(festivalId)
                .query(Integer.class)
                .single();
    }
}
