package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "objectHashCode")
public class ObjectHashCode {

    private int id;

    private int hashCode;

    private String contractId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "hashCode", length = 32)
    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    @Column(name = "contractId", length = 32)
    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
