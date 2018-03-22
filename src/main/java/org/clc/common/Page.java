package org.clc.common;

import lombok.Data;
import org.clc.kernel.pojo.Pojo;

import java.util.List;

@Data
public class Page {
    private int total;
    private int cur_page;
    private int row_page;
    private List<Pojo> list_pojo;
}
