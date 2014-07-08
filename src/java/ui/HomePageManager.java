/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.AccountController;
import controller.ElevatorController;
import controller.RequestController;
import entity.Account;
import entity.Elevator;
import entity.Request;
import exception.CustomException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

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
    private AccountController accountController;
    private ElevatorController elevatorController;
    private RequestController requestController;

    public AccountController getAccountController() {
        if (accountController == null) {
            accountController = new AccountController();
        }
        return accountController;
    }

    public ElevatorController getElevatorController() {
        if (elevatorController == null) {
            elevatorController = new ElevatorController();
        }
        return elevatorController;
    }

    public RequestController getRequestController() {
        if (requestController == null) {
            requestController = new RequestController();
        }
        return requestController;
    }
    private boolean showAdminPage = false;
    private boolean showUserPage = true;
    public final byte PAGE_USER_DEFAULT = 0;
    public final byte PAGE_USER_REPORT = 1;
    public final byte PAGE_USER_SENDREQUEST = 2;
    public final byte PAGE_ADMIN_VIEWALLUSER = 3;
    public final byte PAGE_ADMIN_ADDELEVATOR = 4;
    public final byte PAGE_ADMIN_VIEWREQUEST = 5;
    private byte pageMode = PAGE_USER_DEFAULT;

    public void logout() {
        makeUserPageShow(PAGE_USER_DEFAULT);
        login.logout();
    }

    public List<Account> getAccountList() {
        try {
            return getAccountController().getAllAccounts();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    private void changeValuePageMode(byte nValue) {
        if (nValue != PAGE_USER_SENDREQUEST) {
            this.sendrequest_elevatorChosedId = 0;
            this.sendrequest_elevatorChosedBasePrice = 0;
            this.sendrequest_elevatorChosedFloorPrice = 0;
            this.sendrequest_elevatorChosedDescription = null;
            this.sendrequest_totalPrice = 0;
            this.sendrequest_floorCount = 1;
            this.sendrequest_systemCount = 1;
            this.sendrequest_address = null;
        }
        if (nValue != PAGE_ADMIN_ADDELEVATOR) {
            this.addelevator_name = null;
            this.addelevator_description = null;
            this.addelevator_baseprice = 0;
            this.addelevator_floorprice = 0;
        }
        this.pageMode = nValue;
    }

    public boolean isShowAdminPage() {
        return showAdminPage;
    }

    public void makeAdminPageShow(byte showAdminPage) {
        if (!login.showAdminAuthReq()) {
            this.showAdminPage = true;
            this.showUserPage = false;
            changeValuePageMode(showAdminPage);
        }
    }

    public boolean isShowUserPage() {
        return showUserPage;
    }

    public void makeUserPageShow(byte showUserPage) {
        if (!login.showLoginReq()) {
            this.showUserPage = true;
            this.showAdminPage = false;
            changeValuePageMode(showUserPage);
        }
    }

    public void showPageUserReport() {
        makeUserPageShow(PAGE_USER_REPORT);
    }

    public void showPageUserDefaultMode() {
        this.showUserPage = true;
        this.showAdminPage = false;
        changeValuePageMode(PAGE_USER_DEFAULT);
    }

    public void showPageAdminViewAllUser() {
        makeAdminPageShow(PAGE_ADMIN_VIEWALLUSER);
    }

    public boolean isAdmin_ViewAllUsersMode() {
        return this.pageMode == PAGE_ADMIN_VIEWALLUSER;
    }

    public boolean isUser_ViewPageDefaultMode() {
        return this.pageMode == PAGE_USER_DEFAULT;
    }

    //BEGIN================= SEND REQUEST
    public boolean isUser_ViewSendRequestMode() {
        return this.pageMode == PAGE_USER_SENDREQUEST;
    }

    public void showPageUserSendRequest() {
        makeUserPageShow(PAGE_USER_SENDREQUEST);
    }
    private int sendrequest_floorCount = 1;
    private int sendrequest_elevatorChosedId = 0;
    private int sendrequest_systemCount = 1;
    private String sendrequest_address;
    private List<Elevator> elevators;
    private double sendrequest_elevatorChosedBasePrice;
    private double sendrequest_elevatorChosedFloorPrice;
    private String sendrequest_elevatorChosedDescription;
    private double sendrequest_totalPrice = 0;
    private String sendrequest_message;

    public List<Elevator> getElevators() {
        try {
            this.elevators = getElevatorController().getAllElevators();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.elevators = new ArrayList<Elevator>();
        }
        return this.elevators;
    }

    public void sendRequest() {
        try {
            if (!getElevatorController().isElevator(this.sendrequest_elevatorChosedId)) {
                this.sendrequest_message = "Elevator does not exists!";
                return;
            }
            if (this.sendrequest_floorCount < 0) {
                this.sendrequest_message = "Floor count must not lower than 1!";
                return;
            }
            if (this.sendrequest_systemCount < 0) {
                this.sendrequest_message = "System count must not lower than 1!";
                return;
            }
            if (this.sendrequest_address.length() > 3000) {
                this.sendrequest_message = "Address length must not be empty and lower than 3000 characters! (" + this.sendrequest_address.length() + " chars detected)";
                return;
            }
            Request request = new Request();
            request.setUserId(login.getUser().getId());
            request.setElevatorId(this.sendrequest_elevatorChosedId);
            request.setFloorCount(this.sendrequest_floorCount);
            request.setSystemCount(this.sendrequest_systemCount);
            request.setAddress(this.sendrequest_address);
            request.setTotalPrice(getTotalRequestPrice());
            request.setDone(false);
            request.setProcessing(false);
            getRequestController().createRequest(request);
            this.sendrequest_message = "Request sent";
        } catch (ClassNotFoundException | CustomException | SQLException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.sendrequest_message = "An error occured: " + ex.getMessage();
        }
    }

    public void elevatorTypeChangeListener(ValueChangeEvent event) {
        try {
            Integer id = (Integer) event.getNewValue();
            for (Elevator e : elevators) {
                if (e.getId() == id) {
                    sendrequest_elevatorChosedBasePrice = e.getBasePrice();
                    sendrequest_elevatorChosedFloorPrice = e.getFloorPrice();
                    sendrequest_elevatorChosedDescription = e.getDescription();
                    this.sendrequest_totalPrice = getTotalRequestPrice();
                    return;
                }
            }
        } catch (Exception ex) {
        }
        sendrequest_elevatorChosedBasePrice = 0;
        sendrequest_elevatorChosedFloorPrice = 0;
        sendrequest_elevatorChosedDescription = null;
        sendrequest_totalPrice = 0;
    }

    public String getSendrequest_message() {
        String copy = this.sendrequest_message;
        this.sendrequest_message = null;
        return copy;
    }

    public void setSendrequest_message(String sendrequest_message) {
        this.sendrequest_message = sendrequest_message;
    }

    private double getTotalRequestPrice() {
        return (sendrequest_elevatorChosedBasePrice + sendrequest_elevatorChosedFloorPrice * sendrequest_floorCount) * sendrequest_systemCount;
    }

    public double getSendrequest_totalPrice() {
        return sendrequest_totalPrice;
    }

    public void setSendrequest_totalPrice(double sendrequest_totalPrice) {
        this.sendrequest_totalPrice = sendrequest_totalPrice;
    }

    public int getSendrequest_floorCount() {
        return sendrequest_floorCount;
    }

    public void setSendrequest_floorCount(int sendrequest_floorCount) {
        this.sendrequest_floorCount = sendrequest_floorCount;
        this.sendrequest_totalPrice = getTotalRequestPrice();

    }

    public int getSendrequest_elevatorChosedId() {
        return sendrequest_elevatorChosedId;
    }

    public void setSendrequest_elevatorChosedId(int sendrequest_elevatorChosedId) {
        this.sendrequest_elevatorChosedId = sendrequest_elevatorChosedId;
    }

    public int getSendrequest_systemCount() {
        return sendrequest_systemCount;
    }

    public void setSendrequest_systemCount(int sendrequest_systemCount) {
        this.sendrequest_systemCount = sendrequest_systemCount;
        this.sendrequest_totalPrice = getTotalRequestPrice();
    }

    public String getSendrequest_address() {
        return sendrequest_address;
    }

    public void setSendrequest_address(String sendrequest_address) {
        this.sendrequest_address = sendrequest_address;
    }

    public double getSendrequest_elevatorChosedBasePrice() {
        return sendrequest_elevatorChosedBasePrice;
    }

    public void setSendrequest_elevatorChosedBasePrice(double sendrequest_elevatorChosedBasePrice) {
        this.sendrequest_elevatorChosedBasePrice = sendrequest_elevatorChosedBasePrice;
    }

    public double getSendrequest_elevatorChosedFloorPrice() {
        return sendrequest_elevatorChosedFloorPrice;
    }

    public String getSendrequest_elevatorChosedDescription() {
        return sendrequest_elevatorChosedDescription;
    }

    public void setSendrequest_elevatorChosedDescription(String sendrequest_elevatorChosedDescription) {
        this.sendrequest_elevatorChosedDescription = sendrequest_elevatorChosedDescription;
    }

    public void setSendrequest_elevatorChosedFloorPrice(double sendrequest_elevatorChosedFloorPrice) {
        this.sendrequest_elevatorChosedFloorPrice = sendrequest_elevatorChosedFloorPrice;
    }
    //END================= SEND REQUEST

    //BEGIN================= ADD ELEVATOR
    public boolean isAdmin_AddElevatorMode() {
        return this.pageMode == PAGE_ADMIN_ADDELEVATOR;
    }

    public void showPageAdminAddElevator() {
        makeAdminPageShow(PAGE_ADMIN_ADDELEVATOR);
    }
    private String addelevator_message;
    private String addelevator_name;
    private double addelevator_baseprice;
    private double addelevator_floorprice;
    private String addelevator_description;

    public void createElevator() {
        try {
            if (this.addelevator_name.length() > 128) {
                this.addelevator_message = "Elevator name's length must be lower than 128 characters (" + this.addelevator_name.length() + " chars detected)";
                return;
            }
            if (this.addelevator_description.length() > 3000) {
                this.addelevator_message = "Elevator description's length must be lower than 3000 characters (" + this.addelevator_description.length() + " chars detected)";
                return;
            }
            if (this.addelevator_baseprice <= 0 || this.addelevator_floorprice <= 0) {
                this.addelevator_message = "Elevator's price must be greater than zero!";
                return;
            }
            Elevator ele = new Elevator();
            ele.setName(addelevator_name);
            ele.setBasePrice(addelevator_baseprice);
            ele.setFloorPrice(addelevator_floorprice);
            ele.setDescription(addelevator_description);
            getElevatorController().createElevator(ele);
            this.addelevator_message = "Elevator added!";
            this.addelevator_name = null;
            this.addelevator_description = null;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.addelevator_message = "An error occured: " + ex.getMessage();
        }
    }

    public String getAddelevator_message() {
        String copy = this.addelevator_message;
        this.addelevator_message = null;
        return copy;
    }

    public void setAddelevator_message(String addelevator_message) {
        this.addelevator_message = addelevator_message;
    }

    public String getAddelevator_name() {
        return addelevator_name;
    }

    public void setAddelevator_name(String addelevator_name) {
        this.addelevator_name = addelevator_name;
    }

    public double getAddelevator_baseprice() {
        return addelevator_baseprice;
    }

    public void setAddelevator_baseprice(double addelevator_baseprice) {
        this.addelevator_baseprice = addelevator_baseprice;
    }

    public double getAddelevator_floorprice() {
        return addelevator_floorprice;
    }

    public void setAddelevator_floorprice(double addelevator_floorprice) {
        this.addelevator_floorprice = addelevator_floorprice;
    }

    public String getAddelevator_description() {
        return addelevator_description;
    }

    public void setAddelevator_description(String addelevator_description) {
        this.addelevator_description = addelevator_description;
    }
    //END================= ADD ELEVATOR

    //BEGIN================= VIEW REQUEST
    public boolean isAdmin_ViewRequestMode() {
        return this.pageMode == PAGE_ADMIN_VIEWREQUEST;
    }

    public void showPageAdminViewRequest() {
        makeAdminPageShow(PAGE_ADMIN_VIEWREQUEST);
    }
    private Boolean viewrequest_requestdone;
    private List<Request> viewrequest_requestList;
    private Request viewrequest_selectedRequest;
    private Account viewrequest_selectedRequestSender;
    private String viewrequest_message;
    private boolean viewrequest_showSelectedRequest = false;

    public void viewRequest(Request request) {
        try {
            this.viewrequest_selectedRequest = request;
            this.viewrequest_selectedRequestSender = getAccountController().getAccount(request.getUserId());
            this.viewrequest_requestdone = request.isDone();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.viewrequest_message = "An error occured: " + ex.getMessage();
        }
    }

    public String getViewrequest_message() {
        String copy = this.viewrequest_message;
        this.viewrequest_message = null;
        return copy;
    }

    public void setViewrequest_message(String viewrequest_message) {
        this.viewrequest_message = viewrequest_message;
    }

    public boolean isViewrequest_requestdone() {
        return viewrequest_requestdone == null ? false : viewrequest_requestdone;
    }

    public void setViewrequest_requestdone(boolean viewrequest_requestdone) {
        this.viewrequest_requestdone = viewrequest_requestdone;
    }

    public List<Request> getViewrequest_requestList() {
        try {
            this.viewrequest_requestList = getRequestController().getAllRequests();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.viewrequest_requestList = new ArrayList<Request>();
        }
        return this.viewrequest_requestList;
    }

    public Request getViewrequest_selectedRequest() {
        return viewrequest_selectedRequest;
    }

    public void setViewrequest_selectedRequest(Request viewrequest_selectedRequest) {
        this.viewrequest_selectedRequest = viewrequest_selectedRequest;
    }

    public Account getViewrequest_selectedRequestSender() {
        return viewrequest_selectedRequestSender;
    }

    public void setViewrequest_selectedRequestSender(Account viewrequest_selectedRequestSender) {
        this.viewrequest_selectedRequestSender = viewrequest_selectedRequestSender;
    }

    public boolean isViewrequest_showSelectedRequest() {
        return viewrequest_showSelectedRequest;
    }

    public void setViewrequest_showSelectedRequest(boolean viewrequest_showSelectedRequest) {
        this.viewrequest_showSelectedRequest = viewrequest_showSelectedRequest;
    }
    //END================= VIEW REQUEST
}
