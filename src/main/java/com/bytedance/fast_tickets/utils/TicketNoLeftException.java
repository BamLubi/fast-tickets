package com.bytedance.fast_tickets.utils;

public class TicketNoLeftException extends Exception{
    public TicketNoLeftException(){
        super("TICKET_NO_LEFT");
    }

    public TicketNoLeftException(String message){
        super(message);
    }
}
