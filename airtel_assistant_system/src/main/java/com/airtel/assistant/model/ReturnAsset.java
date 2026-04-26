package com.airtel.assistant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "returns_table")
public class ReturnAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer returnId;

    private Integer assignId;
    private LocalDate returnDate;
    private String conditionOnReturn;
    private String remarks;

    public ReturnAsset() {}
}