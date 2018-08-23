package com.qk.controller;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * IDockingController
 */
public interface IDockingController {
    int DOCKING_HEADER_HIDDEN = 1;
    int DOCKING_HEADER_DOCKING = 2;
    int DOCKING_HEADER_DOCKED = 3;

    int getDockingState(int firstVisibleGroup, int firstVisibleChild);
}
