package com.superbazar.Utils;

public class Urls {
    public static String BASE_URL = "https://smlawb.org/superbazaar/user/";

    /*
    Function: Slider
    Method: POST
     */
    public static String SLIDER = BASE_URL + "slider";

    /*
    Function: category
    Method: POST
     */
    public static String CATEGORY = BASE_URL + "category";

    /*
   Function: categorywiseproductdetails
   Method: POST
    */
    public static String CATEGORYWISEPRODCUTDETAILS = BASE_URL + "categorywiseproductdetails";

    /*
    Function: productdetails
    Parameter : id
    Method: POST
     */
    public static String PRODUCT_DETAILS = BASE_URL + "productdetails";

    /*
    Function: product-by-new-arrivial
    Method: POST
     */
    public static String NEW_ARRIVAL_PRODUCT = BASE_URL + "product-by-new-arrivial";


    /*
    Function: product-by-best-seller
    Method: POST
     */
    public static String BEST_SELLER_PRODUCT = BASE_URL + "product-by-best-seller";

    /*
    Function: webuser
    Method: POST
    Parameter : WebUserIP,WebUserPhone,WebUserEmail,WebUserFullName,WebUserName,WebUserPassword
    */
    public static String WEBUSER = BASE_URL + "webuser";

    /*
    Function: cart
    Method: POST
    Parameter : WebUserId,Type,ProductId,Quantity
    */
    public static String CART = BASE_URL + "cart";

    /*
    Function: cart-list
    Method: POST
    Parameter : id,type
    */
    public static String CART_LIST = BASE_URL + "cart-list";

}
