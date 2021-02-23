package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.model.ProductDetailed;
import com.example.demo.model.Rating;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductController {

    @RequestMapping(value={"/", "index"}, method=RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        ProductService ps = new ProductService();
        Map<String, Map<String, Integer>> list = ps.productList();
        // System.out.println(list);
        mv.addObject("productlist", list);
        List<Product> products = ps.productsLatest();
        mv.addObject("products", products);
        return mv;
    }

    @RequestMapping(value="category", method=RequestMethod.GET)
    public ModelAndView category(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("index");
        ProductService ps = new ProductService();
        Map<String, Map<String, Integer>> list = ps.productList();
        // System.out.println(list);
        mv.addObject("productlist", list);
        List<Product> products = ps.productsCategory(request.getParameter("category"));
        mv.addObject("products", products);
        return mv;
    }

    @RequestMapping(value="subcategory", method=RequestMethod.GET)
    public ModelAndView subCategory(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("index");
        ProductService ps = new ProductService();
        Map<String, Map<String, Integer>> list = ps.productList();
        // System.out.println(list);
        mv.addObject("productlist", list);
        List<Product> products = ps.productsSubcategory(request.getParameter("category"),
                                                        request.getParameter("subcategory"));
        mv.addObject("products", products);
        return mv;
    }

    @RequestMapping(value="search", method=RequestMethod.GET)
    public ModelAndView searchProducts(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("index");
        ProductService ps = new ProductService();
        Map<String, Map<String, Integer>> list = ps.productList();
        // System.out.println(list);
        mv.addObject("productlist", list);
        List<Product> products = ps.productsSearch(request.getParameter("pattern"));
        mv.addObject("products", products);
        return mv;
    }

    @RequestMapping(value="viewproduct", method=RequestMethod.GET)
    public ModelAndView product(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("product");
        ProductService ps = new ProductService();
        Map<String, Map<String, Integer>> list = ps.productList();
        //System.out.println(list);
        int pid = Integer.parseInt(request.getParameter("pid"));
        mv.addObject("productlist", list);
        ProductDetailed product = new ProductDetailed();
        product = ps.getProductDetailed(pid);
        mv.addObject("product", product);
        if(product.getImages().isEmpty())
            product.setImages(new ArrayList<String>() {{
                add("tmp.png");
            }});
        // Add Product Rating
        Rating rating = ps.productRating(pid);
        mv.addObject("ratings", rating);
        return mv;
    }

    @RequestMapping(value="addrating", method=RequestMethod.POST)
    public ModelAndView addRating(HttpServletRequest request,HttpServletResponse response) {
        Comment c = new Comment();
        if(request.getSession().getAttribute("uid")==null) return new ModelAndView("redirect:/login");
        int uid = Integer.parseInt(request.getSession().getAttribute("uid").toString());
        c.setComment(request.getParameter("comment"));
        c.setRating(Integer.parseInt(request.getParameter("rating")));
        c.setUid(uid);
        c.setPid(Integer.parseInt(request.getParameter("pid")));
        ProductService ps = new ProductService();
        ps.setProductRating(c);
        return new ModelAndView("redirect:/viewproduct?pid=" + c.getPid());
    }
}
