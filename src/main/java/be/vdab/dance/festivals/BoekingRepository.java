package be.vdab.dance.festivals;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BoekingRepository {
    private final JdbcClient jdbcClient;

    public BoekingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long create(Boeking boeking) {
        var sql = """
                insert into boekingen(naam, aantalTickets, festivalId)
                values (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(boeking.getNaam(), boeking.getAantalTickets(), boeking.getFestivalId())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }
}
