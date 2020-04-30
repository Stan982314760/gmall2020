package com.atguigu.gmall.bean;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @param
 * @return
 */
@Data
public class PmsBaseCatalog3 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String catalog2Id;
}

