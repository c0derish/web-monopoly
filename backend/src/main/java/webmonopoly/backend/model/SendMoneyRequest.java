package webmonopoly.backend.model;

public class SendMoneyRequest {

    Integer amount;
    String recipient;

    public SendMoneyRequest(Integer amount, String recipient) { this.amount = amount; this.recipient = recipient; }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
