package com.lisa.manager;

import com.lisa.bean.Bean_business_child;
import com.lisa.bean.Bean_business_group;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理图层分组的信息的模型
 *
 * Created by WANT on 2017/9/25.
 */
public class LayerGroup {

    /**
     * 图层列表信息
     */
    private static List<Bean_business_group> mRenderlist = null;
    private static List<Bean_business_group> mTemp = null;

    /**
     * 获取图层分组信息
     * @return
     */
    public static List<Bean_business_group> GetRenderList(){
        if(mRenderlist == null||mRenderlist.size() == 0) {
            if (mRenderlist == null){
                mRenderlist = new ArrayList<>();
            }
            synchronized (mRenderlist) {
                if(mRenderlist.size() == 0) {
                    initRenderList();
                }
            }
        }
        return mRenderlist;
    }

    private static void initRenderList() {
        mRenderlist.clear();
        //5大分组
        Bean_business_group group1 = new Bean_business_group();
        Bean_business_group group2 = new Bean_business_group();
        Bean_business_group group3 = new Bean_business_group();
        Bean_business_group group4 = new Bean_business_group();
        Bean_business_group group5 = new Bean_business_group();

        List<Bean_business_child> childList1 = new ArrayList<>();
        List<Bean_business_child> childList2 = new ArrayList<>();
        List<Bean_business_child> childList3 = new ArrayList<>();
        List<Bean_business_child> childList4 = new ArrayList<>();
        List<Bean_business_child> childList5 = new ArrayList<>();

        //11个小类型
        Bean_business_child child1 = new Bean_business_child();
        Bean_business_child child2 = new Bean_business_child();
        Bean_business_child child3 = new Bean_business_child();
        Bean_business_child child4 = new Bean_business_child();
        Bean_business_child child5 = new Bean_business_child();
        Bean_business_child child6 = new Bean_business_child();
        Bean_business_child child7 = new Bean_business_child();
        Bean_business_child child8 = new Bean_business_child();
        Bean_business_child child9 = new Bean_business_child();
        Bean_business_child child10 = new Bean_business_child();
        Bean_business_child child11 = new Bean_business_child();

        //基础信息分组
        group1.setMC("基础信息");
        child1.setMC("基础信息");
        child1.setRenderShow(true);
        child1.setBM("1");
        childList1.add(child1);
        group1.setSon(childList1);
        mRenderlist.add(group1);

        //高程数据分组
        group2.setMC("高程");
        child2.setMC("等高线");
        child2.setRenderShow(true);
        child2.setBM("2");
        childList2.add(child2);
        child3.setMC("高程点");
        child3.setRenderShow(true);
        child3.setBM("3");
        childList2.add(child3);
        group2.setSon(childList2);
        mRenderlist.add(group2);

        //房屋类
        group3.setMC("房屋类");
        child4.setMC("辅助房屋");
        child4.setRenderShow(true);
        child4.setBM("4");

        childList3.add(child4);
        child5.setMC("主要房屋--居民点");
        child5.setRenderShow(true);
        child5.setBM("5");

        childList3.add(child5);
        child6.setMC("主要房屋--居民点标记");
        child6.setRenderShow(true);
        child6.setBM("6");

        childList3.add(child6);
        group3.setSon(childList3);
        mRenderlist.add(group3);

        //红线类
        group4.setMC("红线类");
        child7.setMC("可研红线枢纽区");
        child7.setRenderShow(true);
        child7.setBM("7");

        childList4.add(child7);
        child8.setMC("可研红线水库区");
        child8.setRenderShow(true);
        child8.setBM("8");

        childList4.add(child8);
        child9.setMC("河道红线");
        child9.setRenderShow(true);
        child9.setBM("9");

        childList4.add(child9);
        child10.setMC("已征范围红线");
        child10.setRenderShow(true);
        child10.setBM("10");

        childList4.add(child10);
        group4.setSon(childList4);
        mRenderlist.add(group4);


        //辅助地物类
        group5.setMC("辅助地物类");
        child11.setMC("辅助地物类");
        child11.setRenderShow(false);
        child11.setBM("11");

        childList5.add(child11);
        group5.setSon(childList5);
        mRenderlist.add(group5);

        //列表缓冲
        mTemp = mRenderlist;
    }



}
