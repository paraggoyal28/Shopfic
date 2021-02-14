package com.example.demo.service;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.model.ProductDetailed;
import com.example.demo.model.Rating;

import java.util.List;
import java.util.Map;

public class ProductService {
    public int getNewPid() {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        return pd.pidMaker();
    }
    public Map<String, Map<String, Integer>> productList() {
        Map<String, Map<String, Integer>> hm = null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        hm = pd.getList();
        return hm;
    }
    public List<Product> productSeller(int sid) {
        List<Product> list = null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        list = pd.getProductsSid(sid);
        return list;
    }
    public List<Product> productsLatest() {
        List<Product> list = null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        list = pd.getProductsLatest();
        return list;
    }
    public List<Product> productsCategory(String category) {
        // gives list of the products on basis of the category specified
        List<Product> list = null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        list = pd.getProductsCategory(category);
        return list;
    }
    public List<Product> productsSubcategory(String category, String subcategory) {
        // gives list of the products on basis of category+subcategory specified
        List<Product> list = null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        list = pd.getProductsSubcategory(category, subcategory);
        return list;
    }
    public List<Product> productsSearch(String pattern) {
        // gives list of products on search basis
        List<Product> list =  null;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        list = pd.getProductsSearch(pattern);
        return list;
    }
    public ProductDetailed getProductDetailed(int pid) {
        // gives detailed info of one product
        ProductDetailed prod;
        ProductDAO pd = new ProductDAO();
        pd.connect();
        prod = pd.getProductDetailed(pid);
        return prod;
    }
    public void addImage(int pid, List<String> images) {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        pd.updateImage(images, pid);
    }
    public int addProduct(ProductDetailed p) {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        return pd.addProduct(p);
        /*
        if(pd.addProduct(p))System.out.println("Added successfully"); // return true on success addition
        else System.out.println("Product addition failed");
         */
    }
    public void updateProduct(ProductDetailed prod) {
        //TODO
        ProductDAO pd = new ProductDAO();
        pd.connect();
        pd.updateProduct(prod);
    }
    public void deleteProduct(int pid) {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        pd.deleteProduct(pid);
    }
    public Rating productRating(int pid) {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        Rating rating = pd.getProductRating(pid);
        return rating;
    }
    public void setProductRating(Comment c) {
        ProductDAO pd = new ProductDAO();
        pd.connect();
        pd.addRating(c);
    }


}
