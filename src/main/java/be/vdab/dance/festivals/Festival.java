package be.vdab.dance.festivals;

public final class Festival {
    private final long id;
    private final String naam;
    private final int ticketsBeschikbaar;

    public Festival(long id, String naam, int ticketsBeschikbaar) {
        this.id = id;
        this.naam = naam;
        this.ticketsBeschikbaar = ticketsBeschikbaar;
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
}
