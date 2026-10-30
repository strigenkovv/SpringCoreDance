package be.vdab.dance.festivals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

@JdbcTest
@Import({FestivalRepository.class, FestivalService.class})
@Sql("/festivals.sql")
class FestivalServiceTest {
    private static final String FESTIVALS_TABLE = "festivals";

    private final FestivalService festivalService;
    private final JdbcClient jdbcClient;

    FestivalServiceTest(FestivalService festivalService, JdbcClient jdbcClient) {
        this.festivalService = festivalService;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void annuleerVerdeeltBudgetEnVerwijdertFestival() {
        var id = idVanFestival("Tomorrowland");

        festivalService.annuleer(id);

        assertThat(countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE, "id=" + id))
                .isZero();
        assertThat(reclameBudgetVanFestival("Rock Werchter"))
                .isEqualByComparingTo("3500");
        assertThat(reclameBudgetVanFestival("Pukkelpop"))
                .isEqualByComparingTo("4500");
    }

    private long idVanFestival(String naam) {
        return jdbcClient.sql("select id from festivals where naam = ?")
                .param(naam)
                .query(Long.class)
                .single();
    }

    private BigDecimal reclameBudgetVanFestival(String naam) {
        return jdbcClient.sql("select reclameBudget from festivals where naam = ?")
                .param(naam)
                .query(BigDecimal.class)
                .single();
    }
}
