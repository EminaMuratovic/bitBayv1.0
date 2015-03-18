package helpers;

import models.User;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

public class AdminFilter extends Security.Authenticator {
	


	@Override
	public String getUsername(Context ctx) {
		if(!ctx.session().containsKey("email"))
			return null;
		String email = ctx.session().get("email");
		User u = User.find(email);
		if (u != null) {
			if(u.admin == true)
				return u.email;
			else
				return null;
		}
		return null;
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect("/login");
	}
}