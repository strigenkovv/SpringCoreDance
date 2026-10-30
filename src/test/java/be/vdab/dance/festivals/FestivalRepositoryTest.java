package be.vdab.dance.festivals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(FestivalRepository.class)
@Sql("/festivals.sql")
class FestivalRepositoryTest {
    private static final String FESTIVALS_TABLE = "festivals";

    private final FestivalRepository festivalRepository;
    private final JdbcClient jdbcClient;

    FestivalRepositoryTest(FestivalRepository festivalRepository, JdbcClient jdbcClient) {
        this.festivalRepository = festivalRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findAllGeeftFestivalsGesorteerdOpNaam() {
        var festivals = festivalRepository.findAll();
        assertThat(festivals)
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareTo)
                .contains("Pukkelpop", "Rock Werchter", "Tomorrowland");
    }

    @Test
    void findUitverkochtGeeftAlleenUitverkochteFestivals() {
        var uitverkochteFestivals = festivalRepository.findUitverkocht();
        assertThat(uitverkochteFestivals)
                .isNotEmpty()
                .allMatch(festival -> festival.getTicketsBeschikbaar() == 0);
    }

    @Test
    void findUitverkochtGeeftFestivalsGesorteerdOpNaam() {
        assertThat(festivalRepository.findUitverkocht())
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareTo);
    }

    @Test
    void createMaaktEenFestivalAan() {
        var id = festivalRepository.create(new Festival(0, "Graspop", 25));
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE, "id=" + id)).isOne();
    }

    @Test
    void deleteVerwijdertEenFestival() {
        var id = idVanTestFestival();
        festivalRepository.delete(id);
        assertThat(countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE, "id=" + id)).isZero();
    }

    private long idVanTestFestival() {
        return jdbcClient.sql("select id from festivals where naam = 'Rock Werchter'")
                .query(Long.class)
                .single();
    }

}
