/***************************************************************************
 *
 *  COPYRIGHT (c) 2004 BY INTEGRATED CONCEPTS INTERNATIONAL LIMITED.
 *
 *  ALL RIGHTS RESERVED. NO PART OF THIS TEXT FILE MAY BE REPRODUCED,
 *  STORED IN A RETRIEVAL SYSTEM OR COMPUTER SYSTEM, OR TRANSMITTED
 *  IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL, PHOTOCOPYING,
 *  RECORDING OR OTHERWISE, WITHOUT PRIOR WRITTEN PERMISSION OF
 *  INTEGRATED CONCEPTS INTERNATIONAL LIMITED.
 *
 *  Integrated Concepts International Limited
 *  11th Broadway
 *  64-52 Lockhart Road
 *  Wanchai, Hong Kong
 *  Telephone       (852) 27788682
 *  Facsimile       (852) 27764515
 *
 ***************************************************************************/
package com.example.cqrs.demo.query.entries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.query.entries
 *
 *  File Name       : ProductEntry.java
 *
 *  Creation Date   : 2/20/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to store the account in database
 *
 *
 *  History         : 2019-02-20, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntry {

    @Id
    private String id;
    private String name;
    private Integer stock;
    private Long price;
    private Integer taskId;
    private Integer times;
}
