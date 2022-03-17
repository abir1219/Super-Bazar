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
    Function: product-search
    Method: POST
    Parameter:search_data

     */
    public static String PRODUCT_SEARCH = BASE_URL + "product-search";


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
    Parameter : id,ProductId,Quantity
    */
    public static String CART = BASE_URL + "cart";

    /*
    Function: add-to-wishlist
    Method: POST
    Parameter : id,ProductId
    */
    public static String WISHLIST = BASE_URL + "add-to-wishlist";

    /*
    Function: cart-list
    Method: POST
    Parameter : id,type
    */
    public static String CART_LIST = BASE_URL + "cart-list";

    /*
    Function: wishlist-list
    Method: POST
    Parameter : id
    */
    public static String WISHLIST_LIST = BASE_URL + "wishlist-list";

    /*
    Function: logout
    Method: POST
    Parameter : id
    */
    public static String LOGOUT = BASE_URL + "logout";

    /*
   Function: login
   Method: POST
   Parameter : username,password
   */
    public static String LOGIN = BASE_URL + "login";


    /*
    Function: count-cart
    Method: POST
    Parameter : id
    */
    public static String CART_COUNT = BASE_URL + "count-cart";

    /*
    Function: update-cart
    Method: POST
    Parameter : cart_id,user_id, quantity
    */
    public static String UPDATE_CART = BASE_URL + "update-cart";

    /*
    Function: count-wishlist
    Method: POST
    Parameter : id
    */
    public static String WISHLIST_COUNT = BASE_URL + "count-wishlist";

    /*
    Function: remove-cart
    Method: POST
    Parameter : id
    */
    public static String REMOVE_CART = BASE_URL + "remove-cart";

    /*
    Function: remove-wishlist
    Method: POST
    Parameter : id
    */
    public static String REMOVE_WISHLIST = BASE_URL + "remove-wishlist";

    /*
    Function: user-address
    Method: POST
    Parameter : address_id (when edit),user_id,name,phone,pincode,address,landmark
    */
    public static String USER_ADDRESS = BASE_URL + "user-address";

    /*
    Function: address-list
    Method: POST
    Parameter : user_id,address_id
    */
    public static String ADDRESS_LIST = BASE_URL + "address-list";

    /*
    Function: order-place
    Method: POST
    Parameter : user_id,payment_type,address_id,total
    */
    public static String PLACE_ORDER = BASE_URL + "order-place";

    /*
    Function: order-list
    Method: POST
    Parameter : user_id
    */
    public static String ORDER_LIST = BASE_URL + "order-list";

    /*
    Function: order-details-list
    Method: POST
    Parameter : order_id
    */
    public static String ORDER_DETAILS_LIST = BASE_URL + "order-details-list";

    /*
   Function: address-delete
   Method: POST
   Parameter :  address_id
   */
    public static String ADDRESS_DELETE = BASE_URL + "address-delete";

    /*
   Function: user-profile-fetch
   Method: POST
   Parameter :  user_id
   */
    public static String USER_PROFILE = BASE_URL + "user-profile-fetch";

    /*
   Function: webuser-update
   Method: POST
   Parameter :  user_id,WebUserIP,WebUserPhone,WebUserEmail,WebUserFullName,WebUserName

   */
    public static String USER_PROFILE_UPDATE = BASE_URL + "webuser-update";

    /*
   Function: change-password
   Method: POST
   Parameter :  user_id, old_password, new_password
   */
    public static String CHANGE_PASSWORD = BASE_URL + "change-password";

    /*
      Function: fetch-pincode
      Method: POST
      Parameter :  pin_code
      */
    public static String FETCH_PINCODE = BASE_URL + "fetch-pincode";
}
