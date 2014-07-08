/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author thanb_000
 */
@ManagedBean(name = "hpmanager")
@SessionScoped
public class HomePageManager implements Serializable {

    @ManagedProperty(value = "#{login}")
    private Login login;

    public void setLogin(Login login) {
        this.login = login;
    }

    private boolean showAdminPage = false;
    private boolean showUserPage = true;
    
    public final byte PAGE_USER_DEFAULT = 0;
    public final byte PAGE_USER_REPORT = 1;
    public final byte PAGE_USER_SENDREQUEST = 2;
    
    public final byte PAGE_ADMIN_VIEWALLUSER = 3;
    
    private byte pageMode = PAGE_USER_DEFAULT;
    
    public void logout(){
        makeUserPageShow(PAGE_USER_DEFAULT);
        login.logout();
    }

    public boolean isShowAdminPage() {
        return showAdminPage;
    }

    public void makeAdminPageShow(byte showAdminPage) {
        if (!login.showAdminAuthReq()) {
            this.showAdminPage = true;
            this.showUserPage = false;
            this.pageMode = showAdminPage;
        }
    }

    public boolean isShowUserPage() {
        return showUserPage;
    }

    public void makeUserPageShow(byte showUserPage) {
        if (!login.showLoginReq()) {
            this.showUserPage = true;
            this.showAdminPage = false;
            this.pageMode = showUserPage;
        }
    }
    
    public void showPageUserReport(){
        makeUserPageShow(PAGE_USER_REPORT);
    }
    
    public void showPageAdminViewAllUser(){
        makeAdminPageShow(PAGE_ADMIN_VIEWALLUSER);
    }
}
