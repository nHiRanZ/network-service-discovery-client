package io.apptizer.nsdclientapp;

import java.util.List;

public class PaOrderResponse {
    private List<PaOrder> paOrders;

    public PaOrderResponse() {
    }

    public PaOrderResponse(List<PaOrder> paOrders) {
        this.paOrders = paOrders;
    }

    public List<PaOrder> getPaOrders() {
        return paOrders;
    }

    public void setPaOrders(List<PaOrder> paOrders) {
        this.paOrders = paOrders;
    }

    @Override
    public String toString() {
        return "PaOrderResponse{" +
                "paOrders=" + paOrders +
                '}';
    }
}
