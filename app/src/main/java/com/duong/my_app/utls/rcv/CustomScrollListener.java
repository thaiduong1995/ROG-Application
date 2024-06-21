package com.duong.my_app.utls.rcv;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScrollListener extends RecyclerView.OnScrollListener implements OnStartScrollListener {

    private final OnStartScrollListener listener;

    public CustomScrollListener() {
        listener = this;
    }

    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                System.out.println("The RecyclerView is not scrolling");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                System.out.println("Scrolling now");
                listener.onStartScroll();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                System.out.println("Scroll Settling");
                break;

        }

    }

    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            System.out.println("Scrolled Right");
        } else if (dx < 0) {
            System.out.println("Scrolled Left");
        } else {
            System.out.println("No Horizontal Scrolled");
        }

        if (dy > 0) {
            System.out.println("Scrolled Downwards");
        } else if (dy < 0) {
            System.out.println("Scrolled Upwards");
        } else {
            System.out.println("No Vertical Scrolled");
        }
    }

    @Override
    public void onStartScroll() {

    }
}

interface OnStartScrollListener {
    void onStartScroll();
}
