package controllers;

import helpers.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

/**
 * Controls the ad application Redirects on the pages when needed
 * 
 * @author eminamuratovic
 *
 */
public class ProductApplication extends Controller {
	static Form<User> loginUser = new Form<User>(User.class);
	static Form<Product> productForm= new Form <Product>(Product.class);
	// user picks new category for his product
	@Security.Authenticated(UserFilter.class)
	public static Result pickCategory() {

		DynamicForm form = Form.form().bindFromRequest();

		String category = form.data().get("category");

		// if there is no category by that name it creates redirect to previous
		// page
		return redirect("/addproduct/" + Category.categoryId(category));
	}

	// adds additional info to product
	// creates new product
	// returns user to his home page

	@Security.Authenticated(UserFilter.class)
	public static Result addAdditionalInfo(int id) {
		//Form <Product> form=productForm.bindFromRequest();
		DynamicForm form = Form.form().bindFromRequest();
		String name = form.get("name");
//		User owner = new User(session().get("username"), form.get("password"));
		
//		DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
//		Date created = new Date();
//		int quantity = 0;// Integer.parseInt(form.data().get("quantity"));
		//double price= 100;
		double price = Double.valueOf(form.get("price"));
		
		String description = form.get("description");
//		String image_url = "";// form.data().get("image url");
		
		Product.create(name, price,
				description,id);
		return redirect("/homepage");
	}
	public static Result productPage(){
		return ok(productpage.render(Product.productList()));
	}

	public static Result category(String name) {
		return ok(category.render(name,Product.listByCategory(name)));
	}

	public static Result toPickCategory() {
		return ok(addproductcategory.render(Category.list()));
	}

	public static Result toInfo(int id) {
		
		return ok(addproduct.render(id,productForm));
	}
	
	//public static Result toDeleteProduct(){
	//	return ok(deleteproductpage.render(Product.list()));
	//}
	//method that should delete product and redirect to other products/uses delete method from Product class
	public static Result deleteProduct(int id) {
		Product.delete(id);
		return redirect("/productpage");

	}
	
	public static Result updateProduct(int id){
		return ok(updateproduct.render(Product.find(id)));
	}
	
	public static Result update (int id){
		Product updateProduct= Product.find(id);
		updateProduct.name=productForm.bindFromRequest().field("name").value();
		updateProduct.price=Double.parseDouble(productForm.bindFromRequest().field("price").value());
		updateProduct.description=productForm.bindFromRequest().field("description").value();
		Product.update(updateProduct);
		return redirect("/productpage");
	}

}
