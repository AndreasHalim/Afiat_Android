// Generated code from Butter Knife. Do not modify!
package com.example.afiat;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.gms.common.SignInButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.inEmail = Utils.findRequiredViewAsType(source, R.id.email, "field 'inEmail'", EditText.class);
    target.inPassword = Utils.findRequiredViewAsType(source, R.id.password, "field 'inPassword'", EditText.class);
    target.btnLogin = Utils.findRequiredViewAsType(source, R.id.btn_login, "field 'btnLogin'", Button.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    target.btnSignIn = Utils.findRequiredViewAsType(source, R.id.sign_in_button, "field 'btnSignIn'", TextView.class);
    target.btnSignInGoogle = Utils.findRequiredViewAsType(source, R.id.sign_in_google, "field 'btnSignInGoogle'", SignInButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inEmail = null;
    target.inPassword = null;
    target.btnLogin = null;
    target.progressBar = null;
    target.btnSignIn = null;
    target.btnSignInGoogle = null;
  }
}
