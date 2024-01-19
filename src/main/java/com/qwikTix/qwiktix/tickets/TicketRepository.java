package com.qwikTix.qwiktix.tickets;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Tickets, Integer> {
}
