package com.galive.logic.network.ws.command;

public class StopRecordCommand extends BaseCommand {

    private String clientId = "";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
