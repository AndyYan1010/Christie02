package com.example.administrator.christie.InformationMessege;

import com.example.administrator.christie.entity.MainMenuEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/17 13:34
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class IconInfo implements Serializable {
    private List<MainMenuEntity> list = new ArrayList<MainMenuEntity>();

    public List getList() {
        return list;
    }

}
