package org.tech.dto;

public class UpdateProfileRequest {
    private String name;
    private String department;
    private String section;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest( String name, String department, String section) {
        this.name = name;
        this.department = department;
        this.section = section;
    }

    // Getters and setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }
}
