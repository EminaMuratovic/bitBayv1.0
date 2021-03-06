package controllers;

import models.User;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

// TODO: Auto-generated Javadoc
/**
 * The Class Verification.
 */
public class Verification extends Controller {

	/**
	 * Verificate email.
	 *
	 * @param confirmation String the confirmation
	 * @return the result
	 */
	public static Result verificateEmail(String confirmation) {
		Logger.info("Verification started");

		User u = User.findByConfirmation(confirmation);
		if (confirmation == null) {
			flash("error", Messages.get("error"));
			return redirect("/");
		}
		if (User.confirm(u)) {
			Logger.info("Verification successfully confirmed");
			flash("success", Messages.get("Successfully verificated"));
			return redirect("/login");
		} else {
			Logger.debug("conf fail");
			flash("errorLink", Messages.get("error.confirm"));
			return redirect("/");
		}

	}

}
