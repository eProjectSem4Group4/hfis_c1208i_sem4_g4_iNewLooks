/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.AccountController;
import controller.ElevatorController;
import controller.FeedbackController;
import controller.RequestController;
import entity.Account;
import entity.Elevator;
import entity.Feedback;
import entity.Filter;
import entity.Project;
import entity.Request;
import entity.Task;
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
import model.ProjectModel;
import model.TaskModel;

/**
 *
 * @author thanb_000
 */
@ManagedBean(name = "hpmanager")
@SessionScoped
public class HomePageManager implements Serializable {

    //<editor-fold desc="Managed property & controllers">
    @ManagedProperty(value = "#{login}")
    private Login login;

    public void setLogin(Login login) {
        this.login = login;
    }
    private TaskModel taskModel;

    public TaskModel getTaskModel() {
        if (taskModel == null) {
            taskModel = new TaskModel();
        }
        return taskModel;
    }
    
    private ProjectModel projectModel;

    public ProjectModel getProjectModel() {
        if (projectModel == null)
            projectModel = new ProjectModel();
        return projectModel;
    }
    
    
    private AccountController accountController;
    private ElevatorController elevatorController;
    private RequestController requestController;
    private FeedbackController feedbackController;

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

    public FeedbackController getFeedbackController() {
        if (feedbackController == null) {
            feedbackController = new FeedbackController();
        }
        return feedbackController;
    }
    //</editor-fold>
    //<editor-fold desc="View state">
    private boolean showAdminPage = false;
    private boolean showUserPage = true;
    public final byte PAGE_USER_DEFAULT = 0;
    public final byte PAGE_USER_REPORT = 1;
    public final byte PAGE_USER_SENDREQUEST = 2;
    public final byte PAGE_USER_SENDFEEDBACK = 6;
    public final byte PAGE_USER_VIEWREQUEST = 8;
    public final byte PAGE_USER_VIEWPROJECT = 9;
    public final byte PAGE_ADMIN_VIEWALLUSER = 3;
    public final byte PAGE_ADMIN_ADDELEVATOR = 4;
    public final byte PAGE_ADMIN_VIEWREQUEST = 5;
    public final byte PAGE_ADMIN_VIEWFEEDBACK = 7;
    // last = 9
    private byte pageMode = PAGE_USER_DEFAULT;

    private void changeValuePageMode(byte nValue) {
        if (nValue != PAGE_USER_SENDREQUEST) {
            this.getSendrequest_ElevatorChosed().setId(0);
            this.getSendrequest_ElevatorChosed().setBasePrice(0);
            this.getSendrequest_ElevatorChosed().setFloorPrice(0);
            this.getSendrequest_ElevatorChosed().setDescription(null);
            this.getSendrequest_Elevator().setTotalPrice(0);
            this.getSendrequest_Elevator().setFloorCount(1);
            this.getSendrequest_Elevator().setSystemCount(1);
            this.getSendrequest_Elevator().setAddress(null);
        }
        if (nValue != PAGE_ADMIN_ADDELEVATOR) {
            this.getAddElevator().setName(null);
            this.getAddElevator().setDescription(null);
            this.getAddElevator().setCountry(null);
            this.getAddElevator().setBasePrice(0);
            this.getAddElevator().setFloorPrice(0);
            this.getAddElevator().setProducer(null);
        }
        if (nValue != PAGE_ADMIN_VIEWREQUEST) {
            this.viewrequest_selectedRequest = null;
            this.viewrequest_selectedRequestSender = null;
        }
        if (nValue != PAGE_USER_SENDFEEDBACK) {
            this.mfbFeedback = null;
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
        return this.showUserPage;
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
    //</editor-fold>

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

    //<editor-fold desc="SEND REQUEST">
    //BEGIN================= SEND REQUEST
    public boolean isUser_ViewSendRequestMode() {
        return this.pageMode == PAGE_USER_SENDREQUEST;
    }

    public void showPageUserSendRequest() {
        makeUserPageShow(PAGE_USER_SENDREQUEST);
    }
    private List<Elevator> elevators = null;
    private List<Elevator> elevatorsFilter;

    public List<Elevator> getElevatorsFilter() {
        return elevatorsFilter;
    }

    public void setElevatorsFilter(List<Elevator> elevatorsFilter) {
        this.elevatorsFilter = elevatorsFilter;
    }
    private Request sendrequest_Elevator;
    private Elevator sendrequest_ElevatorChosed;
    private String sendrequest_message;

    public List<Elevator> getElevators() {
        if (this.elevators == null || this.elevators.isEmpty()) {
            try {
                this.elevators = getElevatorController().getAllElevators();
            } catch (SQLException | ClassNotFoundException | CustomException ex) {
                Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
                this.elevators = new ArrayList<Elevator>();
            }
        }
        return this.elevators;
    }

    public void sendRequest() {
        try {
            if (!getElevatorController().isElevator(this.sendrequest_ElevatorChosed.getId())) {
                this.sendrequest_message = "Elevator does not exists!";
                return;
            }
            if (getSendrequest_Elevator().getFloorCount() < 0) {
                this.sendrequest_message = "Floor count must not lower than 1!";
                return;
            }
            if (getSendrequest_Elevator().getSystemCount() < 0) {
                this.sendrequest_message = "System count must not lower than 1!";
                return;
            }
            if (getSendrequest_Elevator().getAddress().length() > 3000) {
                this.sendrequest_message = "Address length must not be empty and lower than 3000 characters! (" + getSendrequest_Elevator().getAddress().length() + " chars detected)";
                return;
            }
            getSendrequest_Elevator().setUserId(login.getUser().getId());
            getSendrequest_Elevator().setElevatorId(this.sendrequest_ElevatorChosed.getId());
            getSendrequest_Elevator().setTotalPrice(getTotalRequestPrice());
            getSendrequest_Elevator().setDone(false);
            getSendrequest_Elevator().setProcessing(false);
            getRequestController().createRequest(getSendrequest_Elevator());
            this.sendrequest_message = "Request sent";
        } catch (ClassNotFoundException | CustomException | SQLException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.sendrequest_message = "An error occured: " + ex.getMessage();
        }
    }

    public void elevatorTypeChangeListener(ValueChangeEvent event) {
        Integer id = (Integer) event.getNewValue();
        changeSelectedElevatorInfor(id);
    }

    public Elevator getSendrequest_ElevatorChosed() {
        if (this.sendrequest_ElevatorChosed == null) {
            this.sendrequest_ElevatorChosed = new Elevator();
        }
        return this.sendrequest_ElevatorChosed;
    }

    public void setSendrequest_ElevatorChosed(Elevator sendrequest_ElevatorChosed) {
        this.sendrequest_ElevatorChosed = sendrequest_ElevatorChosed;
    }

    public Request getSendrequest_Elevator() {
        if (this.sendrequest_Elevator == null) {
            this.sendrequest_Elevator = new Request();
        }
        return this.sendrequest_Elevator;
    }

    public void setSendrequest_Elevator(Request sendrequest_Elevator) {
        this.sendrequest_Elevator = sendrequest_Elevator;
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
        return (getSendrequest_ElevatorChosed().getBasePrice() + getSendrequest_ElevatorChosed().getFloorPrice() * getSendrequest_Elevator().getFloorCount()) * getSendrequest_Elevator().getSystemCount();
    }
    private final byte NO_FILTER = 0;
    private final byte FILTER_BY_BASEPRICE = 1;
    private final byte FILTER_BY_FLOORPRICE = 2;
    private final byte FILTER_BY_MAXWEIGHT = 3;
    private final byte FILTER_BY_MAXHUMAN = 4;
    private List<Filter> sendrequest_filterList = null;
    private byte sendrequest_filter = NO_FILTER;

    public List<Filter> getSendrequest_filterList() {
        if (this.sendrequest_filterList == null) {
            this.sendrequest_filterList = new ArrayList<Filter>();
            this.sendrequest_filterList.add(new Filter(NO_FILTER, "No additional detail"));
            this.sendrequest_filterList.add(new Filter(FILTER_BY_MAXWEIGHT, "Show carry weight"));
            this.sendrequest_filterList.add(new Filter(FILTER_BY_MAXHUMAN, "Show carry human"));
            this.sendrequest_filterList.add(new Filter(FILTER_BY_BASEPRICE, "Show base price"));
            this.sendrequest_filterList.add(new Filter(FILTER_BY_FLOORPRICE, "Show floor price"));
        }
        return this.sendrequest_filterList;
    }

    public byte getSendrequest_filter() {
        return sendrequest_filter;
    }

    public void setSendrequest_filter(byte sendrequest_filter) {
        this.sendrequest_filter = sendrequest_filter;
        getElevators();
        for (Elevator e : elevators) {
            e.setFilter(sendrequest_filter);
        }
    }

    public void selectElevator(int eleId) {
        this.sendrequest_ElevatorChosed.setId(eleId);
        changeSelectedElevatorInfor(eleId);
    }

    public void changeSelectedElevatorInfor(Integer iNewId) {
        try {
            for (Elevator e : getElevators()) {
                if (e.getId() == iNewId) {
                    getSendrequest_ElevatorChosed().setBasePrice(e.getBasePrice());
                    getSendrequest_ElevatorChosed().setFloorPrice(e.getFloorPrice());
                    getSendrequest_ElevatorChosed().setDescription(e.getDescription());
                    getSendrequest_ElevatorChosed().setMaxWeight(e.getMaxWeight());
                    getSendrequest_ElevatorChosed().setMaxHuman(e.getMaxHuman());
                    getSendrequest_Elevator().setTotalPrice(getTotalRequestPrice());
                    return;
                }
            }
        } catch (Exception ex) {
        }
        getSendrequest_ElevatorChosed().setBasePrice(0);
        getSendrequest_ElevatorChosed().setFloorPrice(0);
        getSendrequest_ElevatorChosed().setDescription(null);
        getSendrequest_ElevatorChosed().setMaxWeight(0);
        getSendrequest_ElevatorChosed().setMaxHuman(0);
        getSendrequest_Elevator().setTotalPrice(0);
    }

    public void filterTypeChangeListener(ValueChangeEvent event) {
        try {
            getElevators();
            int elevatorsCount = elevators.size();
            if (elevatorsCount < 2) {
                return;
            }
            boolean bNotSorted = true;
            while (bNotSorted) {
                for (int i = 0; i < (elevatorsCount - 1); i++) {
                    Elevator e1 = elevators.get(i);
                    Elevator e2 = elevators.get(i + 1);
                    if (i == (elevatorsCount - 2)) { // end
                        if (!e1.isGreater(e2)) {
                            elevators.set(i, e2);
                            elevators.set(i + 1, e1);
                        } else {
                            bNotSorted = false;
                        }
                    } else {
                        if (!e1.isGreater(e2)) {
                            elevators.set(i, e2);
                            elevators.set(i + 1, e1);
                            break;
                        }
                    }
                }
                if (!bNotSorted) {
                    break;
                }
            }
        } catch (Exception ex) {
        }
    }
    //END================= SEND REQUEST
    //</editor-fold>

    //<editor-fold desc="ADD ELEVATOR">
    //BEGIN================= ADD ELEVATOR
    public boolean isAdmin_AddElevatorMode() {
        return this.pageMode == PAGE_ADMIN_ADDELEVATOR;
    }

    public void showPageAdminAddElevator() {
        makeAdminPageShow(PAGE_ADMIN_ADDELEVATOR);
    }
    private String addelevator_message;
    private Elevator addElevator;

    public Elevator getAddElevator() {
        if (addElevator == null) {
            addElevator = new Elevator();
        }
        return addElevator;
    }

    public void setAddElevator(Elevator addElevator) {
        this.addElevator = addElevator;
    }

    public void createElevator() {
        try {
            if (getAddElevator().getName().length() > 128) {
                this.addelevator_message = "Elevator name's length must be lower than 128 characters (" + getAddElevator().getName().length() + " chars detected)";
                return;
            }
            if (getAddElevator().getDescription().length() > 3000) {
                this.addelevator_message = "Elevator description's length must be lower than 3000 characters (" + getAddElevator().getDescription().length() + " chars detected)";
                return;
            }
            if (getAddElevator().getBasePrice() <= 0 || this.getAddElevator().getFloorPrice() <= 0) {
                this.addelevator_message = "Elevator's price must be greater than zero!";
                return;
            }

            getElevatorController().createElevator(getAddElevator());
            this.addelevator_message = "Elevator added!";
            this.addElevator.setName(null);
            this.addElevator.setDescription(null);
            this.addElevator.setCountry(null);
            this.addElevator.setProducer(null);
            this.elevators = getElevatorController().getAllElevators();
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
    //END================= ADD ELEVATOR
    //</editor-fold>

    //<editor-fold desc="VIEW REQUEST">
    //BEGIN================= VIEW REQUEST
    public boolean isAdmin_ViewRequestMode() {
        return this.pageMode == PAGE_ADMIN_VIEWREQUEST;
    }

    public void showPageAdminViewRequest() {
        makeAdminPageShow(PAGE_ADMIN_VIEWREQUEST);
    }
    private Boolean viewrequest_requestdone;
    private Boolean viewrequest_requestprocessing;
    private List<Request> viewrequest_requestList;
    private Request viewrequest_selectedRequest;
    private Account viewrequest_selectedRequestSender;
    private String viewrequest_message;

    public void viewRequest(Request request) {
        try {
            this.viewrequest_selectedRequest = request;
            this.viewrequest_selectedRequestSender = getAccountController().getAccount(request.getUserId());
            this.viewrequest_requestdone = request.isDone();
            this.viewrequest_requestprocessing = request.isProcessing();
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
        try {
            if (this.viewrequest_selectedRequest == null) {
                this.viewrequest_message = "No request selected";
                return;
            }
            getRequestController().setRequestDone(this.viewrequest_selectedRequest.getId(), viewrequest_requestdone);
            this.viewrequest_requestdone = viewrequest_requestdone;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.viewrequest_message = "An error occured: " + ex.getMessage();
        }
    }

    public Boolean getViewrequest_requestprocessing() {
        return viewrequest_requestprocessing == null ? false : viewrequest_requestprocessing;
    }

    public void setViewrequest_requestprocessing(Boolean viewrequest_requestprocessing) {
        try {
            if (this.viewrequest_selectedRequest == null) {
                this.viewrequest_message = "No request selected";
                return;
            }
            getRequestController().setRequestStatus(this.viewrequest_selectedRequest.getId(), viewrequest_requestprocessing);
            this.viewrequest_requestprocessing = viewrequest_requestprocessing;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.viewrequest_message = "An error occured: " + ex.getMessage();
        }
    }

    public List<Request> getViewrequest_requestList() {
        try {
            if (this.pageMode == PAGE_USER_VIEWREQUEST) {
                this.viewrequest_requestList = getRequestController().getRequestModel().getRequestsOfUser(login.getUser());
            } else {
                this.viewrequest_requestList = getRequestController().getAllRequests();
            }
            for (Request r : viewrequest_requestList) {
                try {
                    r.setTaskList(getTaskModel().getAllTask(r.getId()));
                } catch (Exception ex) {
                }
            }
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
        return this.viewrequest_selectedRequest != null;
    }
    private Task viewrequest_NewTask;

    public Task getViewrequest_NewTask() {
        if (viewrequest_NewTask == null) {
            viewrequest_NewTask = new Task();
        }
        return viewrequest_NewTask;
    }

    public void setViewrequest_NewTask(Task viewrequest_NewTask) {
        this.viewrequest_NewTask = viewrequest_NewTask;
    }

    public void submitNewTask() {
        try {
            viewrequest_NewTask.setRquestId(viewrequest_selectedRequest.getId());
            viewrequest_NewTask.setDone(false);
            if (viewrequest_NewTask.getToDo() == null || viewrequest_NewTask.getToDo().isEmpty()) {
                return;
            }
            getTaskModel().createTask(viewrequest_NewTask);
            viewrequest_NewTask.setToDo("");
            viewrequest_selectedRequest = null;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            viewrequest_message = "An error occured";
        }
    }

    public void updateTask(Task task) {
        try {
            task.setDone(!task.isDone());
            getTaskModel().updateTask(task);
            viewrequest_selectedRequest = null;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            viewrequest_message = "An error occured";
        }
    }

    public void deleteTask(Task task) {
        try {
            getTaskModel().deleteTask(task);
            viewrequest_selectedRequest = null;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            viewrequest_message = "An error occured";
        }
    }
    private List<Request> viewrequest_filterRequest;

    public List<Request> getViewrequest_filterRequest() {
        return viewrequest_filterRequest;
    }

    public void setViewrequest_filterRequest(List<Request> viewrequest_filterRequest) {
        this.viewrequest_filterRequest = viewrequest_filterRequest;
    }

    //END================= VIEW REQUEST
    //</editor-fold>
    //<editor-fold desc="SEND FEEDBACK">
    public boolean isUser_ViewSendFeedbackMode() {
        return this.pageMode == PAGE_USER_SENDFEEDBACK;
    }

    public void showPageUserSendFeedback() {
        makeUserPageShow(PAGE_USER_SENDFEEDBACK);
    }
    private Feedback mfbFeedback;

    public Feedback getMfbFeedback() {
        if (this.mfbFeedback == null) {
            this.mfbFeedback = new Feedback();
            if (login.isLoggedIn()) {
                this.mfbFeedback.setUserId(login.getUser().getId());
                this.mfbFeedback.setSenderName(login.getUser().getName());
                this.mfbFeedback.setSenderEmail(login.getUser().getEmail());
            }
        }
        return this.mfbFeedback;
    }

    public void setMfbFeedback(Feedback mfbFeedback) {
        this.mfbFeedback = mfbFeedback;
    }
    private String sendfeedback_message;

    public String getSendfeedback_message() {
        String copy = this.sendfeedback_message;
        this.sendfeedback_message = null;
        return copy;
    }

    public void setSendfeedback_message(String sendfeedback_message) {
        this.sendfeedback_message = sendfeedback_message;
    }

    public void sendfeedback_submit() {
        if (getMfbFeedback().getSenderName().isEmpty() || getMfbFeedback().getSenderName().length() > 128) {
            this.sendfeedback_message = "Tên người gửi không được để trống và phải ít hơn 128 ký tự";
            return;
        }
        if (!this.getMfbFeedback().getSenderEmail().matches("^[a-zA-Z0-9_@\\.]{1,64}") || !this.getMfbFeedback().getSenderEmail().contains("@")) {
            this.sendfeedback_message = "Sai định dạng email";
            return;
        }
        if (getMfbFeedback().getDescription().isEmpty() || getMfbFeedback().getDescription().length() > 1000) {
            this.sendfeedback_message = "Mô tả vấn đề không được để trống và phải ít hơn 1000 ký tự";
            return;
        }
        if (getMfbFeedback().getProblem().length() > 3000) {
            this.sendfeedback_message = "Mục vấn đề phải ít hơn 3000 ký tự";
            return;
        }
        if (getMfbFeedback().getComment().isEmpty() || getMfbFeedback().getComment().length() > 3000) {
            this.sendfeedback_message = "Mục nội dung cần cải thiện (góp ý) không được để trống và phải ít hơn 3000 ký tự";
            return;
        }
        this.getMfbFeedback().setUserId(login.getUser().getId());
        try {
            getFeedbackController().createFeedback(getMfbFeedback());
            setMfbFeedback(null);
            this.sendfeedback_message = "Cám ơn về phản hồi của bạn!";
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            this.sendfeedback_message = "An error occured: " + ex;
        }
    }

    //</editor-fold>
    //<editor-fold desc="VIEW FEEDBACK">
    public boolean isAdmin_ViewFeedbackMode() {
        return this.pageMode == PAGE_ADMIN_VIEWFEEDBACK;
    }

    public void showPageAdminViewFeedback() {
        makeAdminPageShow(PAGE_ADMIN_VIEWFEEDBACK);
    }
    private Boolean viewfeedback_bNotReadOnly;

    public Boolean getViewfeedback_bNotReadOnly() {
        if (viewfeedback_bNotReadOnly == null) {
            viewfeedback_bNotReadOnly = false;
        }
        return viewfeedback_bNotReadOnly;
    }

    public void setViewfeedback_bNotReadOnly(Boolean viewfeedback_bNotReadOnly) {
        this.viewfeedback_bNotReadOnly = viewfeedback_bNotReadOnly;
    }

    public List<Feedback> getFeedbackList() {
        try {
            List<Feedback> fbl = getFeedbackController().getAll(viewfeedback_bNotReadOnly);
            return fbl;
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            return new ArrayList<Feedback>();
        }
    }

    public void onChangeReadState(Feedback fb) {
        System.out.println("==EVENT== " + fb.getRead());
        try {
            getFeedbackController().changeReadState(fb.getId(), !fb.getRead());
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold desc="USER VIEW REQUEST">
    public boolean isUser_ViewRequestMode() {
        return this.pageMode == PAGE_USER_VIEWREQUEST;
    }

    public void showPageUserViewRequest() {
        makeUserPageShow(PAGE_USER_VIEWREQUEST);
    }
    //</editor-fold>
    
    
    //<editor-fold desc="DEFAULT PAGE">
    public boolean isUser_ViewPageDefaultMode() {
        return this.pageMode == PAGE_USER_DEFAULT;
    }
    
    private List<Project> projectList;

    public List<Project> getProjectList() {
        try {
            return getProjectModel().getAllProjects();
        } catch (SQLException | ClassNotFoundException | CustomException ex) {
            Logger.getLogger(HomePageManager.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Project>();
        }
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
    private Project selectedProject;

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }
    
    public void showProject(Project prj){
        this.selectedProject = prj;
        makeUserPageShow(this.PAGE_USER_VIEWPROJECT);
    }
    //</editor-fold>
    
    //<editor-fold desc="USER VIEW PROJECT">
    public boolean isUser_ViewProjectMode() {
        return this.pageMode == PAGE_USER_VIEWPROJECT;
    }
    //</editor-fold>
}
