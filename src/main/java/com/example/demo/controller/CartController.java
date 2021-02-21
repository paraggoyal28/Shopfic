package com.example.demo.controller;


@Controller
public class CartController {
    
    @RequestMapping(value="addcart", method=RequestMethod.POST) 
    public ModelAndView addCart(HttpServletRequest request, HttpServletResponse response) {
        // fetch count, uid, pid
        Cart c = new Cart();
        HttpSession s = request.getSession();
        int uid = Integer.parseInt(session.getAttribute("uid").toString());
        c.setPid(Integer.parseInt(request.getParameter("pid")));
        c.setUid(uid);
        c.setCount(Integer.parseInt(request.getParameter("count")));
        // System.out.println(c)
        CartService cs = new CartService();
        cs.addCart(c);
        return new ModelAndView("redirect:/viewcart?view=fresh");
    }

    @RequestMapping(value="viewcart", method=RequestMethod.GET) 
    public ModelAndView viewCart(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("viewcart");
        // fetch and items into cart for given uid
        List<Cart> cart = new ArrayList<Cart>();
        
    }
}