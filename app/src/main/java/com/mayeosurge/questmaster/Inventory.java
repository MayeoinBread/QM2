package com.mayeosurge.questmaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {
    List<InvItem> inventoryList;

    public Inventory(){
        inventoryList = new ArrayList<>();
    }

    public InvItem findItemById(int id){
        for (InvItem i:inventoryList) {
            if(i.id == id)
                return i;
        }
        return null;
    }

    public int findItemPos(InvItem item){
        return inventoryList.indexOf(item);
    }

    public void add(InvItem item, int num){
        if(findItemById(item.id)!= null){
            inventoryList.get(findItemPos(findItemById(item.id))).addQty(num);
        }else{
            item.qty = num;
            inventoryList.add(item);
        }
    }

    public void delete(InvItem item, int num){
        InvItem it = findItemById(item.id);
        if(it != null){
            if(it.qty > num)
                inventoryList.get(findItemPos(it)).delQty(num);
            else
                inventoryList.remove(findItemPos(it));
        }
    }
}
