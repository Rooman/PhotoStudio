package com.photostudio.entity;

import java.time.LocalDateTime;

public final class Order {
    private int id;
    private String status;
    private LocalDateTime orderDate;
    private String email;
    private long phoneNumber;
    private String comment;

    public static class Builder {
        private Order order;

        public Builder() {
            order = new Order();
        }

        public Builder withId(int id) {
            order.id = id;
            return this;
        }

        public Builder withStatus(String status) {
            order.status = status;
            return this;
        }

        public Builder withOrderDate(LocalDateTime orderDate) {
            order.orderDate = orderDate;
            return this;
        }

        public Builder withEmail(String email) {
            order.email = email;
            return this;
        }

        public Builder withPhoneNumber(long phoneNumber) {
            order.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withComment(String comment) {
            order.comment = comment;
            return this;
        }

        public Order build() {
            return order;
        }
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", comment='" + comment + '\'' +
                '}';
    }
}
