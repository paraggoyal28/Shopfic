package com.example.demo.service;

import com.example.demo.dao.CartDao;
import com.example.demo.model.Cart;
import com.example.demo.model.Notification;

import java.util.List;

public class CartService {
    public void addCart(Cart c) {
        CartDao cd = new CartDao();
        cd.connect();
        // System.out.println("cart in service is "  + c.getPid() + ", " +  c.getUid() +  ", " + c.getCount());
        cd.addCart(c);
    }
    public List<Cart> viewCartFresh(int uid) {
        List<Cart> products;
        CartDao cd = new CartDao();
        cd.connect();
        products = cd.viewCartFresh(uid);
        return products;
    }
    public List<Cart> viewCartOld(int uid) {
        List<Cart> products;
        CartDao cd = new CartDao();
        cd.connect();
        products = cd.viewCartOld(uid);
        return products;
    }
    public void deleteCartProduct(int pid, int uid) {
        CartDao cd = new CartDao();
        cd.connect();
        cd.deleteCartProduct(pid, uid);
    }
    public void updateCartProduct(int uid, int pid, int count) {
        CartDao cd = new CartDao();
        cd.connect();
        cd.updateCartProduct(uid, pid, count);
    }
    public void buyCart(int uid, boolean cod) {
        CartDao cd = new CartDao();
        cd.connect();
        cd.buyCart(uid, cod);
    }
    // notification services
    public List<Notification> notificationProductRequestFulfilled(int sid) {
        CartDao cd = new CartDao();
        cd.connect();
        List<Notification> list = cd.getProductRequestFulfilled(sid);
        return list;
    }
    public List<Notification> notificationProductRequest(int sid) {
        CartDao cd = new CartDao();
        cd.connect();
        List<Notification> list = cd.getProductRequest(sid);
        return list;
    }
    public List<Notification> notificationProductRequestPending(int sid) {
        CartDao cd = new CartDao();
        cd.connect();
        List<Notification> list = cd.getProductRequestPending(sid);
        return list;
    }
    public List<Notification> notificationOutOfStock(int sid) {
        CartDao cd = new CartDao();
        cd.connect();
        List<Notification> list = cd.getOutOfStock(sid);
        return list;
    }
    public void mark(int sid) {
        CartDao cd = new CartDao();
        cd.connect();
        cd.mark(sid);
    }
}
