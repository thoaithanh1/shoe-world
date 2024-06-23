package root.app.contant;

public enum OrderContant {

    PENDING(1),
    SHIPPING(2),
    DELIVERED(3),
    RETUNRED(4),
    CANCELLED(5);

    public Integer value;

    OrderContant(Integer value) {
        this.value = value;
    }
}
