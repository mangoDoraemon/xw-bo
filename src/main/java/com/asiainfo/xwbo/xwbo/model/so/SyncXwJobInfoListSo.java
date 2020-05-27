package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-20 10:06
 */
@Data
public class SyncXwJobInfoListSo extends SignSo{
    private List<SyncXwJobInfoSo> list;
}
