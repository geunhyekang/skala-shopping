package com.skala.skala_shopping.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, unique = true, length = 20)
    private String customerId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Version
    private Long version;

    protected Customer() {
    }

    public Customer(String customerId, String password, long point, Role role) {
        this.customerId = customerId;
        this.password = password;
        this.point = point;
        this.role = role;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void usePoint(long amount) {
        this.point -= amount;
    }

    public void refundPoint(long amount) {
        this.point += amount;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPassword() {
        return password;
    }

    public long getPoint() {
        return point;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}