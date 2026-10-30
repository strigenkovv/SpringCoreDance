package be.vdab.dance.festivals;

import java.math.BigDecimal;

public final class Festival {
    private final long id;
    private final String naam;
    private final int ticketsBeschikbaar;
    private final BigDecimal reclameBudget;

    public Festival(long id, String naam, int ticketsBeschikbaar, BigDecimal reclameBudget) {
        this.id = id;
        this.naam = naam;
        this.ticketsBeschikbaar = ticketsBeschikbaar;
        this.reclameBudget = reclameBudget;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getTicketsBeschikbaar() {
        return ticketsBeschikbaar;
    }

    public BigDecimal getReclameBudget() {
        return reclameBudget;
    }
}
