package be.vdab.dance.festivals;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public List<BoekingOverzicht> findBoekingOverzicht() {
        var sql = """
                select b.id as boekingId, b.naam, f.naam as festivalNaam, b.aantalTickets
                from boekingen b
                join festivals f on f.id = b.festivalId
                order by b.id
                """;
        return jdbcClient.sql(sql)
                .query(BoekingOverzicht.class)
                .list();
    }

    public List<AantalBoekingenPerFestival> findAantalBoekingenPerFestival() {
        var sql = """
                select f.id as festivalId, f.naam, count(b.id) as aantalBoekingen
                from festivals f
                left join boekingen b on b.festivalId = f.id
                group by f.id, f.naam
                order by f.id
                """;
        return jdbcClient.sql(sql)
                .query(AantalBoekingenPerFestival.class)
                .list();
    }

    public Optional<Boeking> findAndLockById(long id) {
        var sql = """
                select id, naam, aantalTickets, festivalId
                from boekingen
                where id = ?
                for update
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Boeking.class)
                .optional();
    }

    public void delete(long id) {
        var sql = """
                delete from boekingen
                where id = ?
                """;
        jdbcClient.sql(sql)
                .param(id)
                .update();
    }
}
