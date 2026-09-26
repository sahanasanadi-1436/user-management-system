package UserManagementSystem.dto;

public class TaskResponse {

    private Long id;
    private String title;
    private Long assignedUserId;
    private String assignedUserName;
    private String createdBy;

    public TaskResponse() {
    }

    public TaskResponse(
            Long id,
            String title,
            Long assignedUserId,
            String assignedUserName,
            String createdBy) {

        this.id = id;
        this.title = title;
        this.assignedUserId = assignedUserId;
        this.assignedUserName = assignedUserName;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}