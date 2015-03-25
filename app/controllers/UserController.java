package controllers;

import java.net.MalformedURLException;
import java.text.*;
import java.util.Date;

import helpers.*;
import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.*;
import views.html.*;


public class UserController extends Controller {

	static Form<User> userForm = new Form<User>(User.class);

	// goes to page where admin can update user
	@Security.Authenticated(AdminFilter.class)
	public static Result toUpdateUser(int id) {
		Logger.info("User update page opened");
		
		return ok(listofuserspage.render(session().get("email"), User.find(id), FAQ.all()));
	}

	// gets data from updated user
	// redirect to page where it lists all users
	@Security.Authenticated(AdminFilter.class)
	public static Result updateUser(int id) throws MalformedURLException{
		
		DynamicForm form =  Form.form().bindFromRequest();
		User updateUser= User.find(id);
		updateUser.email = form.get("email");
		updateUser.admin = Boolean.parseBoolean(form.get("admin"));
		if (!User.find(id).email.equals(updateUser.email)) {

			User.editEmailVerification(id);
		}

		updateUser.update();
		Logger.info("User with id: " + id + " has been updated");


		return redirect("/profile");
	}

	// deletes user and redirect to list of all users
	@Security.Authenticated(AdminFilter.class)
	public static Result deleteUser(int id){
		
		User.delete(id);
		Logger.warn("User with id: " + id + " has been deleted");
		return redirect("/profile");
	}

	// redirects to page with additional info
	@Security.Authenticated(UserFilter.class)
	public static Result toAdditionalInfo() {
		Logger.info(session().get("email") + " has opened additional info page");
		return ok(additionalinfo.render());
	}

	// adds additional info to user profile
	@Security.Authenticated(UserFilter.class)
	public static Result additionalInfo() throws ParseException {
		
		DynamicForm form =  Form.form().bindFromRequest();
		String email = session().get("email");
		String username = form.get("username");
		Date current = new Date();
		// SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
		// Date birth_date = format.parse(form.get("birth_date"));
		String birth_date_string = userForm.bindFromRequest()
				.field("birth_date").value();
		Date birth_date = format.parse(birth_date_string);
		Logger.debug(birth_date.toString());
		if (!birth_date.before(current)) {
			Logger.error("User " + session().get("email") + "has entered invalid date");
			flash("error", "Enter valid date!");
			return ok(additionalinfo.render());
		}
		String city = form.get("city");
		String shipping_address = form.get("shipping_address");
		String user_address = form.get("user_address");
		String gender = form.get("gender");
		if (!gender.toLowerCase().contains("m")
				&& !gender.toLowerCase().contains("f")) {
			Logger.error("User " + session().get("email") + "has entered invalid gender");
			flash("error", "Enter valid gender!");
			return ok(additionalinfo.render());
		}

		if (User.AdditionalInfo(email, username, birth_date, shipping_address,
				user_address, gender, city)) {

			User u = User.find(email);
			u.hasAdditionalInfo = true;
			u.update();
			Logger.info("User " + session().get("email") + "has added his additional info");
			return redirect("/homepage");
		}

		flash("warning", "Username already exists!");
		Logger.error("User " + session().get("email") + "has entered invalid username");
		return ok(additionalinfo.render());	
		
	}
	
	@Security.Authenticated(UserFilter.class)
	public static Result toEditInfo() {
		Logger.info("User " + session().get("email") + "has opened his additional info");
		return ok(editadditionalinfo.render(User.find(session().get("email"))));
	}

	
	@Security.Authenticated(UserFilter.class)
	public static Result editAdditionalInfo() throws ParseException {
		DynamicForm form = Form.form().bindFromRequest();
		User u = User.find(session().get("email"));
		if (!User.existsUsername(form.get("username")) || form.get("username").equals(u.username)) 
			u.username = form.get("username");
		else {
			Logger.error("User " + session().get("email") + "has entered invalid username");
			flash("error","Username already exists!");
			return ok(editadditionalinfo.render(u));
		}
		Date current = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
		if(!userForm.bindFromRequest()
				.field("birth_date").value().equals(""))
		{
			String birth_date_string = userForm.bindFromRequest()
				.field("birth_date").value();
		u.birth_date = format.parse(birth_date_string);

		if (!u.birth_date.before(current)) {
			Logger.error("User " + session().get("email") + "has entered invalid date");
			flash("error", "Enter valid date!");
			return ok(editadditionalinfo.render(u));
		}
		}
		u.city = form.get("city");
		u.shipping_address = form.get("shipping_address");
		u.user_address = form.get("user_address");
		if(!form.get("gender").equals(""))
		{u.gender = form.get("gender");
		if (!u.gender.toLowerCase().contains("m")
				&& !u.gender.toLowerCase().contains("f")) {
			Logger.error("User " + session().get("email") + "has entered invalid gender");
			flash("error", "Enter valid gender!");
			return ok(editadditionalinfo.render(u));
		}
		}
			u.update();
			Logger.info("User " + session().get("email") + "has updated his additional info");
			flash("success","Additional info updated successfuly!");
			return redirect("/homepage");

	}
	
	
	@Security.Authenticated(UserFilter.class)
	public static Result profile() {
		Logger.info("User " + session().get("email") + " has opened his profile page");
		String email = session().get("email");
		return ok(profile.render(email,User.all(), Category.list(), Product.productList(), FAQ.all()));
		
	}
	




}
