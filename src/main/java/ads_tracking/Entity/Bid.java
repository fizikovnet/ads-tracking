package ads_tracking.Entity;

import java.io.Serializable;

public class Bid implements Identified, Serializable {

    private int id;
    private Item item;
    private User bidder;
    private double bid;

    public Bid(int id) {
        this.id = id;
    }

    public Bid() {
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", item=" + item +
                ", bidder=" + bidder +
                ", bid=" + bid +
                '}';
    }
}
