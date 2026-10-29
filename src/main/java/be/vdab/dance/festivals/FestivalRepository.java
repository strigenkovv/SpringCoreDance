package be.vdab.dance.festivals;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FestivalRepository {
    private final JdbcClient jdbcClient;

    public FestivalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Festival> findAll() {
        var sql = """
                select id, naam, ticketsBeschikbaar
                from festivals
                order by naam
                """;
        return jdbcClient.sql(sql)
                .query(Festival.class)
                .list();
    }

    public List<Festival> findUitverkocht() {
        var sql = """
                select id, naam, ticketsBeschikbaar
                from festivals
                where ticketsBeschikbaar = 0
                order by naam
                """;
        return jdbcClient.sql(sql)
                .query(Festival.class)
                .list();
    }

    public void delete(long id) {
        var sql = """
                delete from festivals
                where id = ?
                """;
        jdbcClient.sql(sql)
                .param(id)
                .update();
    }

    public long create(Festival festival) {
        var sql = """
                insert into festivals(naam, ticketsBeschikbaar)
                values (?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(festival.getNaam(), festival.getTicketsBeschikbaar())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }
}
