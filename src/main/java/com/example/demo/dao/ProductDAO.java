package com.example.demo.dao;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetailed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO extends MainDAO{
    @Deprecated
    public int pidMaker(){
        String query = "Select pid FROM product order by pid DESC LIMIT 1";
        Statement st;
        try {
            st=conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) return rs.getInt("pid");
        } catch(SQLException e) {
            // ToDO Auto-generated catch block
            System.out.println("In Product Dao: pidMaker" + e);
        }
        return 0;
    }

    public Map<String, Map<String, Integer>> getList() {
        // Returns list of categories and subcategories
        String query_category = "SELECT * FROM category";
        String query_subcategory = "SELECT * FROM subcategory WHERE cid=(?)";
        Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(query_category);
            while(rs.next()) {
                Map<String, Integer> sub_result = new HashMap<String, Integer>();
                // map for subcategory
                PreparedStatement pst = conn.prepareStatement(query_subcategory);
                sub_result.put("cid", rs.getInt("cid"));
                sub_result.put("count", 0);
                pst.setInt(1, rs.getInt("cid"));
                ResultSet rs2 = pst.executeQuery();
                while(rs2.next()){
                    sub_result.put(rs2.getString("name"), rs2.getInt("count"));
                    sub_result.replace("count", sub_result.get("count") + rs2.getInt("count"));
                }
                result.put(rs.getString("name"), sub_result);
            }
            return result;
        } catch(SQLException e) {
            System.out.println("In Product Dao: getList: " + e);
        }
        return null;
    }
    public boolean updateProduct(ProductDetailed prod) {
        // MUST GIVE ORIGINAL + MODIFICATION;
        int pid = prod.getPid();
        int newpid = addProduct(prod);
        updateImage(prod.getImages(), newpid);
        updateCarts(pid, newpid);
        deleteProduct(pid);
        return true;
    }
    public void updateCarts(int oldPid, int newPid) {
        String query = "UPDATE cart SET pid=? WHERE pid=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, newPid);
            pst.setInt(2, oldPid);
            pst.executeUpdate();
        } catch(SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("In UpdateCart: " + e);
        }
    }

    public ProductDetailed getProductDetailed(int pid) {
        ProductDetailed pd = new ProductDetailed();
        String query = "SELECT * FROM product WHERE pid=?";
        String query_category = "SELECT * FROM category WHERE cid=?";
        String query_subcategory = "SELECT * FROM subcategory WHERE csid=?";
        String query_images = "SELECT * FROM images WHERE pid=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, pid);
            ResultSet rs = pst.executeQuery();
            rs.next();
            pd.setPid(pid);
            pd.setName(rs.getString("name"));
            pd.setPrice(rs.getDouble("price"));
            pd.setCsid(rs.getInt("csid"));
            pd.setStock(rs.getInt("Stock"));
            pd.setSid(rs.getInt("sid"));
            pd.setShort_description(rs.getString("short_description"));
            pd.setVersion(rs.getString("version"));
            pd.setBrand(rs.getString("brand"));
            pd.setDiscount(rs.getDouble("discount"));
            pd.setFeatures(rs.getString("features"));
            pd.setCost();
            // pst.close();
            pst = conn.prepareStatement(query_subcategory);
            pst.setInt(1, pd.getCsid());
            rs=pst.executeQuery();
            rs.next();
            pd.setSubcategory(rs.getString("name"));
            //pst.close();
            pst=conn.prepareStatement(query_category);
            pst.setInt(1, rs.getInt("cid"));
            rs = pst.executeQuery();
            rs.next();
            pd.setCategory(rs.getString("name"));
            //pst.close();
            pst=conn.prepareStatement(query_images);
            pst.setInt(1, pid);
            rs=pst.executeQuery();
            List<String> list = new ArrayList<String>();
            while(rs.next()){
                list.add(rs.getString("path"));
                pd.setImage_path(rs.getString("path"));
            }
            pd.setImages(list);
            return pd;
        } catch(SQLException e){
            System.out.println("In Product Dao: getProductDetailed: " + e);
        }
        return null;
    }
    public boolean updateImage(List<String> images, int pid) {
        String query_images = "INSERT INTO images(pid, path) VALUES(?,?)";
        PreparedStatement pst;
        try{
            pst = conn.prepareStatement(query_images);
            pst.setInt(1, pid);
            for(String img: images) {
                pst.setString(2, img);
                pst.executeUpdate();
            }
            return true;
        } catch(SQLException e){
            // ToDo Auto-generated catch block
            System.out.println("In Product Dao: updateImages" + e);
        }
        return false;
    }
    public int addProduct(ProductDetailed p) {
        // insert into product and images + update subcategory
        // get csid
        int cid, csid, pid;
        String query_cid = "SELECT cid FROM category WHERE name=?";
        String query_csid = "SELECT csid FROM subcategory WHERE cid=? AND name=?";
        String query_product="INSERT INTO product(name,price,discount,features,stock,csid,brand"
                + ",version,sid,short_description) VALUES(?,?,?,?,?,?,?,?,?,?)";
        // String query_images = "INSERT INTO Images(pid, path) VALUES(?,?)";
        String query_pid = "SELECT pid FROM product ORDER BY pid DESC LIMIT 1";
        String query_subcategory = "UPDATE subcategory SET count=count+1 WHERE name=?";
        try {
            Statement st = conn.createStatement();
            PreparedStatement pst = conn.prepareStatement(query_cid);
            pst.setString(1, p.getCategory());
            ResultSet rs = pst.executeQuery();
            rs.next();
            cid = rs.getInt("cid");
            pst.close();

            pst = conn.prepareStatement(query_csid);
            pst.setInt(1, cid);
            pst.setString(2, p.getSubcategory());
            System.out.println(cid + " " + p.getSubcategory());
            rs = pst.executeQuery();
            rs.next();
            csid = rs.getInt("csid");
            pst.close();

            pst = conn.prepareStatement(query_product);
            pst.setString(1, p.getName());
            pst.setDouble(2, p.getPrice());
            pst.setDouble(3, p.getDiscount());
            pst.setString(4, p.getFeatures());
            pst.setInt(5, p.getStock());
            pst.setInt(6, csid);
            pst.setString(7, p.getBrand());
            pst.setString(8, p.getVersion());
            pst.setInt(9, p.getSid());
            pst.setString(10, p.getShort_description());
            int count = pst.executeUpdate();
            // pst.close();

            rs = st.executeQuery(query_pid);
            rs.next();
            pid = rs.getInt(1);
            /* pst = conn.prepareStatement(query_images);
            pst.setInt(1, pid);
            for(String s: p.getImages()) {
                pst.setString(2, s);
                pst.executeUpdate();
            }
            pst.close();
             */
            pst = conn.prepareStatement(query_subcategory);
            pst.setString(1, p.getSubcategory());
            pst.executeUpdate();
            //pst.close();
            //conn.close();
            return pid;
        } catch(SQLException e){
            System.out.println("In Product Dao: addProduct : "  + e);
        }
        return 0;
    }
    public boolean deleteProduct(int pid) {
        int csid;
        String query = "SELECT csid FROM product WHERE pid=?";
        String query_del = "DELETE FROM product WHERE pid=?";
        String query_subcategory="UDPATE subcategory SET count = count - 1 WHERE csid=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, pid);
            ResultSet rs = pst.executeQuery();
            System.out.println("1");
            rs.next();
            csid = rs.getInt("csid");
            pst = conn.prepareStatement(query_del);
            pst.setInt(1, pid);
            pst.executeUpdate();
            System.out.println("2");
            pst = conn.prepareStatement(query_subcategory);
            pst.setInt(1, csid);
            pst.executeUpdate();
            System.out.println("3");
            return true;
        } catch(SQLException e) {
            //TODO Auto-generated catch block
            System.out.println("In Product Dao: delete Product: " + e);
        }
        return false;
    }
    

}
