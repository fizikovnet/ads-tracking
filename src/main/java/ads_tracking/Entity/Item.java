package ads_tracking.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Item implements Identified, Serializable {

    public static final AtomicInteger itemsState = new AtomicInteger(0);

    private int id;
    private User seller;
    private String title;
    private String description;
    private double startPrice;
    private int timeLeft;
    private LocalDateTime startBiddingDate;
    private boolean buyItNow;
    private int bidIncrement;
    private Bid bestOfferBid;

    public Item(int id) {
        this.id = id;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User user) {
        this.seller = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public LocalDateTime getStartBiddingDate() {
        return startBiddingDate;
    }

    public void setStartBiddingDate(LocalDateTime startBiddingDate) {
        this.startBiddingDate = startBiddingDate;
    }

    public boolean isBuyItNow() {
        return buyItNow;
    }

    public void setBuyItNow(boolean buyItNow) {
        this.buyItNow = buyItNow;
    }

    public int getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(int bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public Bid getBestOfferBid() {
        return bestOfferBid;
    }

    public void setBestOfferBid(Bid bestOfferBid) {
        this.bestOfferBid = bestOfferBid;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", seller=" + seller +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", timeLeft=" + timeLeft +
                ", startBiddingDate=" + startBiddingDate +
                ", buyItNow=" + buyItNow +
                ", bidIncrement=" + bidIncrement +
                '}';
    }
}
